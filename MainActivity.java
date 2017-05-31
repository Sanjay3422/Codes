package sanjay.navigation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PHOTO_REQUEST = 9002;
    ViewFlipper vf;
    private ProgressDialog Dialog2;
    ListView lv;
    static final int REQUEST_READ_PERMISSION= 9003;
    String a1;
    public FirebaseStorage storage= FirebaseStorage.getInstance();
    public StorageReference storageRef = storage.getReferenceFromUrl("gs://share-95319.appspot.com");
    String user,dept,id,phone1,email1,position;
    Myadapter adapter;
    private ArrayList<String> list_of_rooms =  new ArrayList<>();
    private ArrayList<String> list_of_rooms1 =  new ArrayList<>();
    private String text;
    private DatabaseReference root1;
    int k=0;
    String wq[];
    int l;
    DrawerLayout drawer;
    TextView name1, dept1, emaill,phonee,pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l=0;
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();
        lv= (ListView) findViewById(R.id.lv);
        adapter= new Myadapter(MainActivity.this,list_of_rooms,list_of_rooms1);
        lv.setAdapter(adapter);
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        dept=sp.getString("dept","");
        user = sp.getString("name","");
        id=sp.getString("id","");
        phone1= sp.getString("phone","");
        email1= sp.getString("email","");
        position = sp.getString("position","");
        name1 = (TextView) findViewById(R.id.textView5);
        dept1= (TextView) findViewById(R.id.textView6);
        emaill= (TextView) findViewById(R.id.textView12);
        phonee =(TextView) findViewById(R.id.textView11);
        pos = (TextView) findViewById(R.id.textView10);
        name1.setText(user);
        dept1.setText(dept);
        pos.setText("Position   : " + position);
        phonee.setText("Phone      : "+ phone1);
        emaill.setText("Email Id   : "+email1);
        if(id.equals("1")) text="Circular2";
        else text="Circular";
        ed.putString("cir",text);
        ed.apply();
        root1=FirebaseDatabase.getInstance().getReference().child(text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar sn= Snackbar.make(view,"Welcome to share! Press to Navigate",Snackbar.LENGTH_SHORT)
                       .setAction("OPEN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                drawer.openDrawer(Gravity.LEFT);
                            }
                        });
                sn.show();
            }
        });
        requestpermission();
        vf= (ViewFlipper) findViewById(R.id.viewFlipper);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v=navigationView.getHeaderView(0);
        TextView tv= (TextView) v.findViewById(R.id.textView);
        TextView tv1= (TextView) v.findViewById(R.id.textView1);
        tv.setText(user);
        tv1.setText(dept);
        navigationView.getMenu().getItem(1).setChecked(true);
        vf.setDisplayedChild(1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                a1 = adapterView.getItemAtPosition(i).toString();
                if(!(a1.equals(" "))) open();

            }
        });
        root1.addChildEventListener(new ChildEventListener() {
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

        Toast.makeText(this, "Please wait.Loading...", Toast.LENGTH_LONG).show();
    }

    public class Singlemedia implements MediaScannerConnection.MediaScannerConnectionClient{
        private MediaScannerConnection mms;
        private File mfile;

        public Singlemedia(Context context, File f2){
            mfile=f2;
            mms = new MediaScannerConnection(context, this);
            mms.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mms.scanFile(mfile.getAbsolutePath(),null);


        }

        @Override
        public void onScanCompleted(String s, Uri uri) {
            Intent in= new Intent(Intent.ACTION_VIEW);
            in.setData(uri);
            startActivity(in);
            mms.disconnect();

        }
    }

    class Myadapter extends ArrayAdapter{
        ArrayList<String> titleArray;
        ArrayList<String> descArray;

        public Myadapter (Context context, ArrayList<String> titles1, ArrayList<String> descriptions1){
            super(context, R.layout.customlist,R.id.textView3,titles1);
            this.titleArray=titles1;
            this.descArray=descriptions1;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflator=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflator.inflate(R.layout.customlist,parent,false);
            TextView mytitle= (TextView) row.findViewById(R.id.textView3);
            TextView mydescription= (TextView) row.findViewById(R.id.textView4);
            mydescription.setText(titleArray.get(position));
            mytitle.setText(descArray.get(position));
            return row;
        }

    }

    public void open()
    {
        try {
            wq = a1.split("/");
            File f1= new File(Environment.getExternalStorageDirectory()+"/Share");
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
                File send= new File(Environment.getExternalStorageDirectory()+"/Share/"+wq[1]);
                new Singlemedia(MainActivity.this,send);
                k=0;
            }
            else{
                StorageReference islandRef = storageRef.child(a1);
                File localFile;
                localFile = new File(Environment.getExternalStorageDirectory(),"Share");
                if(!localFile.exists())
                    localFile.mkdirs();
                File file1= new File(localFile,islandRef.getName());
                FileDownloadTask filedownload= islandRef.getFile(file1);
                Dialog2 = new ProgressDialog(MainActivity.this);
                Dialog2.setMessage("Please wait...");
                Dialog2.setIndeterminate(false);
                Dialog2.setCancelable(false);
                Dialog2.show();
                filedownload.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Dialog2.dismiss();
                        File send= new File(Environment.getExternalStorageDirectory()+"/Share/"+wq[1]);
                        new Singlemedia(MainActivity.this,send);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Dialog2.dismiss();
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

    private void requestpermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                }
                else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_PERMISSION);
                }
            }
            else {
                //open();

            }
        }
        else {
            //open();

        }
    }

    private void append_app(DataSnapshot dataSnapshot) {
        Set<String> set= new HashSet<String>();
        Set<String> set1= new HashSet<String>();
        Iterator i= dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            set.add((String) ((DataSnapshot)i.next()).getValue());
            set1.add((String) ((DataSnapshot)i.next()).getValue());
        }
        list_of_rooms.addAll(set);
        list_of_rooms1.addAll(set1);
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(l==0)
                {
                    Collections.reverse(list_of_rooms);
                    Collections.reverse(list_of_rooms1);
                    l=1;
                }
                adapter.notifyDataSetChanged();
            }
        },300);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        if(sp.getString("id","").equals("1"))
        getMenuInflater().inflate(R.menu.menumain, menu);
        else getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,Main5Activity.class));
            return true;
        }
        if (id == R.id.show){
            startActivity(new Intent(MainActivity.this,Main7Activity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_first_layout) {
            vf.setDisplayedChild(1);

        } else if (id == R.id.nav_second_layout) {
            startActivity(new Intent(MainActivity.this,Main6Activity.class));

        } else if (id == R.id.nav_third_layout) {

            vf.setDisplayedChild(0);

        } else if (id == R.id.nav_manage) {
            Intent in = new Intent(MainActivity.this,Main4Activity.class);
            startActivity(in);


        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this,Main5Activity.class));


        } else if (id == R.id.nav_send) {
            final AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage("Are you sure, you want to logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent in=new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(in);
                    SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("a","0");
                    ed.putString("cir"," ");
                    ed.apply();
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
