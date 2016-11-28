package droid.mynote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    ListView list;
    final Context context = this;
    SwipeRefreshLayout swipe;
    String filename1;
    String[] SavedFiles;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        list = (ListView) findViewById(R.id.listView);
        registerForContextMenu(list);
        ShowSavedFiles();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ShowSavedFiles();
                swipe.setRefreshing(false);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = parent.getItemAtPosition(position).toString();
                StringBuffer stringbuff = new StringBuffer();
                try {
                    BufferedReader inputreader = new BufferedReader(new InputStreamReader(openFileInput(filename)));
                    String inputstring;
                    while ((inputstring = inputreader.readLine()) != null) {
                        stringbuff.append(inputstring + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent in = new Intent(MainActivity.this, Main3Activity.class);
                in.putExtra("d", stringbuff.toString());
                in.putExtra("a",filename);
                startActivity(in);

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(in);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPos = info.position;
        final String filename = list.getItemAtPosition(listPos).toString();
        switch (item.getItemId()) {
            case R.id.edit:
                StringBuffer stringbuff = new StringBuffer();
                try {
                    BufferedReader inputreader = new BufferedReader(new InputStreamReader(openFileInput(filename)));
                    String inputstring;
                    while ((inputstring = inputreader.readLine()) != null) {
                        stringbuff.append(inputstring + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent in=new Intent(MainActivity.this,Main4Activity.class);
                in.putExtra("d",stringbuff.toString());
                in.putExtra("a",filename);
                startActivity(in);
                return true;

            case R.id.delete:
                AlertDialog.Builder alert=new AlertDialog.Builder(this);
                alert.setMessage("Are you sure, you want to delete?");
                alert.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        File dir=getFilesDir();
                        final File file= new File(dir,filename);
                        file.delete();
                        ShowSavedFiles();
                        Toast.makeText(MainActivity.this, "successfully deleted "+filename, Toast.LENGTH_SHORT).show();
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

            case R.id.rename:
                StringBuffer stringbuff1 = new StringBuffer();
                try {
                    BufferedReader inputreader = new BufferedReader(new InputStreamReader(openFileInput(filename)));
                    String inputstring;
                    while ((inputstring = inputreader.readLine()) != null) {
                        stringbuff1.append(inputstring + "\n");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                final String s=stringbuff1.toString();
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptsView = layoutInflater.inflate(R.layout.dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final EditText text1 = (EditText) promptsView.findViewById(R.id.edit1);
                final TextView tv = (TextView) promptsView.findViewById(R.id.tv);
                alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (text1.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "File Name cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            String filename1 = text1.getText().toString();
                            String data = s;
                            FileOutputStream fos;
                            try {
                                fos = openFileOutput(filename1, Context.MODE_PRIVATE);
                                fos.write(data.getBytes());
                                fos.close();
                                File dir1=getFilesDir();
                                final File file1= new File(dir1,filename);
                                file1.delete();
                                ShowSavedFiles();
                                Toast.makeText(MainActivity.this, "renamed your file as "+filename1, Toast.LENGTH_SHORT).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "File not found", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Some problem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog1 = alertDialogBuilder.create();
                alertDialog1.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    void ShowSavedFiles() {
        SavedFiles = getApplicationContext().fileList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SavedFiles);
        list.setAdapter(adapter);
    }

    boolean doubleBack = false;

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            super.onBackPressed();
            return;
        }
        this.doubleBack = true;
        Toast.makeText(MainActivity.this, "Press again to Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);
    }


}
