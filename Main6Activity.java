package sanjay.navigation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main6Activity extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    FloatingActionButton fab;
    private String selectedFilePath;
    private Uri downloadUrl;
    public Uri photouri;
    public FirebaseStorage storage= FirebaseStorage.getInstance();
    public StorageReference storageRef = storage.getReferenceFromUrl("gs://share-95319.appspot.com");
    private ProgressDialog Dialog2;
    private String temkey;
    private static final int PICK_FILE_REQUEST = 1;
    String wq[];
    int k=0;
    WebView wb;
    DatabaseReference root= FirebaseDatabase.getInstance().getReference().child("Images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        wb = (WebView) findViewById(R.id.wb);
        wb.setVisibility(View.GONE);
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilepicker();

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                try {
                    ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                    Intent intent = new Intent(Main6Activity.this, DetailsActivity.class);
                    //intent.putExtra("image", item.getImage());
                    intent.putExtra("title", item.getTitle());
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    Toast.makeText(Main6Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                //Start details activity

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_app(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_app(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private String chat_msg;

    private void append_app(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            try {
                wq = chat_msg.split("/");
                File f1= new File(Environment.getExternalStorageDirectory()+"/Share/images");
                if(!f1.exists())
                    f1.mkdirs();
                String wr[]=f1.list();
                for(int j=0; j<wr.length; j++)
                {
                    if(wq[1].equals(wr[j]))
                    {
                        k=1;
                    }
                }
                if(k==1)
                {
                    k=0;
                }
                else{
                    StorageReference islandRef = storageRef.child(chat_msg);
                    File localFile;
                    localFile = new File(Environment.getExternalStorageDirectory(),"Share/images");
                    if(!localFile.exists())
                        localFile.mkdirs();
                    File file1= new File(localFile,islandRef.getName());
                    FileDownloadTask filedownload= islandRef.getFile(file1);
                    filedownload.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            gridAdapter = new GridViewAdapter(Main6Activity.this, R.layout.grid_item_layout, getData());
                            gridView.setAdapter(gridAdapter);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }


            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void realtime() {
            DatabaseReference root1= FirebaseDatabase.getInstance().getReference().child("Images");
            Map<String,Object> map1= new HashMap<String, Object>();
            temkey= root1.push().getKey();
            root1.updateChildren(map1);
            DatabaseReference mess= root1.child(temkey);
            Map<String,Object> map2= new HashMap<String, Object>();
            map2.put("Filename",downloadUrl.getLastPathSegment());
            mess.updateChildren(map2);
            wb.loadUrl("http://share1234.16mb.com/firebase/notify4.php");
        }

    private void openfilepicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    Toast.makeText(this, "no file selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                photouri = data.getData();
                selectedFilePath = FilePath.getPath(this,photouri);
                upload();

            }
        }
    }



    private void upload() {

        Uri file = Uri.fromFile(new File(selectedFilePath));
        StorageReference riversRef = storageRef.child("images2/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
        Dialog2 = new ProgressDialog(Main6Activity.this);
        Dialog2.setMessage("Uploading...");
        Dialog2.setIndeterminate(false);
        Dialog2.setCancelable(false);
        Dialog2.show();
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Dialog2.dismiss();
                Toast.makeText(Main6Activity.this, "failed to upload. please try again.", Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                Dialog2.dismiss();
                Toast.makeText(Main6Activity.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                realtime();
            }
        });
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();

       File f1 = new File(Environment.getExternalStorageDirectory() + "/Share/images");
        if (!f1.exists())
            f1.mkdirs();
        String[] fi = f1.list();
            for (int i = 0; i < fi.length; i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(f1+"/"+fi[i]);
                imageItems.add(new ImageItem(bitmap,fi[i]));
            }

        return imageItems;
    }
}
