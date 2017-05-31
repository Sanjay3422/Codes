package tab.swipe;

/**
 * Created by Sanjay's PC on 1/9/2017.
 */
import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;


public class Tab1 extends Fragment{
    private ListView listView;
    SwipeRefreshLayout refreshLayout;
    String[] d={""};
    static final int REQUEST_READ_PERMISSION= 9003;
    final String root=Environment.getExternalStorageDirectory().getAbsolutePath();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main1, container, false);
        listView =(ListView)rootView.findViewById(R.id.lv);
        refreshLayout= (SwipeRefreshLayout)rootView.findViewById(R.id.swipe);
        registerForContextMenu(listView);
        requestpermission();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String filename = adapterView.getItemAtPosition(i).toString();

                Intent in = new Intent(getActivity(), Main5Activity.class);
               in.putExtra("d", file(filename));
               in.putExtra("a",filename);
                startActivity(in);

            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ShowSavedFiles();
                refreshLayout.setRefreshing(false);
            }
        });

        return rootView;


    }

   @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPos = info.position;
        final String filename = listView.getItemAtPosition(listPos).toString();
        switch (item.getItemId()) {
            case R.id.edit:
                Intent in=new Intent(getActivity(),Main7Activity.class);
                in.putExtra("d",file(filename));
                in.putExtra("a",filename);
                startActivity(in);
                return true;

            case R.id.delete:
                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setMessage("Are you sure, you want to delete?");
                alert.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        File file = new File(root + "/MyNote/TextFiles/"+filename);
                        if(file.delete())
                            Toast.makeText(getActivity(), "successfully deleted "+filename, Toast.LENGTH_SHORT).show();
                        ShowSavedFiles();
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                return true;
            case R.id.copy:
                String copy=file(filename);
                ClipboardManager clip= (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clippp= ClipData.newPlainText(" ", copy);
                clip.setPrimaryClip(clippp);
                Toast.makeText(getActivity(), "copied to clipboard", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.rename:

                final String s=file(filename);
                final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("New File Name");
                final EditText input_field = new EditText(getActivity());
                builder.setView(input_field);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (input_field.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "File Name cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {

                            String filename1 = input_field.getText().toString() + ".txt";
                            File photo = new File(root + "/MyNote/TextFiles/"+filename);
                            File newna= new File(root + "/MyNote/TextFiles/"+filename1);
                            photo.renameTo(newna);
                            ShowSavedFiles();
                            Toast.makeText(getActivity(), "renamed your file to "+filename1, Toast.LENGTH_SHORT).show();
                        }
                    }


                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                return true;
            case R.id.convert:
                String root=Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File(root + "/MyNote/TextFiles/"+filename);
                Uri uri= Uri.fromFile(file);
                Intent sh= new Intent();
                sh.setAction(Intent.ACTION_SEND);
                sh.putExtra(Intent.EXTRA_STREAM, uri);
                sh.setType("text/plain");
                startActivity(Intent.createChooser(sh,"share using"));

                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private String file(String filename) {
        StringBuffer stt= new StringBuffer();
        int s;
        String root=Environment.getExternalStorageDirectory().getAbsolutePath();
        File f1= new File(root+"/MyNote/TextFiles");
        if(!f1.exists())
        {
            f1.mkdirs();
        }
        File file = new File(root + "/MyNote/TextFiles/"+filename);
        try {
            if(!file.exists())
            file.createNewFile();
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader= new InputStreamReader(fis);
            try {
                s=reader.read();
                while(s != -1){
                    stt.append((char)s);
                    s = reader.read();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "File not found", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Some problem", Toast.LENGTH_SHORT).show();
            }
            finally {
                reader.close();
                fis.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stt.toString();
    }

    private void requestpermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {

            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_PERMISSION);
            }
            else {
                ShowSavedFiles();
            }
        }
        else {
            ShowSavedFiles();
        }
    }

   @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            ShowSavedFiles();
        }
    }

    private void ShowSavedFiles() {
        File f= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyNote/TextFiles");
        if(!f.exists())
        {
            f.mkdirs();
        }
        FilenameFilter filenamefilter= new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {

                if (!(s.startsWith(".")))
                    return true;

                return false;
            }
        };
        d=f.list(filenamefilter);
        File[] def=f.listFiles(filenamefilter);
        for(int i=0; i< def.length; i++)
        {
            for (int j=i+1;j< def.length;j++)
            {
                if (def[i].lastModified()<def[j].lastModified())
                {
                    File t= def[i];
                    def[i]=def[j];
                    def[j]=t;
                }
            }
        }
        int k=0;
        for (File f1: def)
        {
            d[k]=f1.getName();
            k++;
        }
        ArrayAdapter<String> ass= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,d);
        listView.setAdapter(ass);

    }
}
