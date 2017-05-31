package sanjay.navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Main4Activity extends AppCompatActivity {
    private Button add_room;
    private EditText room_name;
    private String name;
    private WebView wb;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms =  new ArrayList<>();
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().getRoot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        setTitle("Chat Rooms - group");
        add_room= (Button) findViewById(R.id.button3);
        room_name= (EditText) findViewById(R.id.editText2);
        wb= (WebView) findViewById(R.id.web);
        wb.setVisibility(View.GONE);
        listView= (ListView) findViewById(R.id.listview);
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        if(sp.getString("id","").equals("0"))
        {
            add_room.setVisibility(View.GONE);
            room_name.setVisibility(View.GONE);
        }
        arrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list_of_rooms);
        listView.setAdapter(arrayAdapter);

        request_user_name();

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(room_name.getText().toString().isEmpty()) {}
                else
                {
                    Map<String,Object> map= new HashMap<String, Object>();
                    map.put(room_name.getText().toString()," ");
                    root.updateChildren(map);
                    room_name.setText("");
                    wb.loadUrl("http://share1234.16mb.com/firebase/notify3.php");
                }
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set= new HashSet<String>();
                Iterator i= dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                set.remove("Circular");
                set.remove("Circular2");
                set.remove("Images");
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(Main4Activity.this,Chat_Room.class);
                in.putExtra("room_name",((TextView)view).getText().toString());
                in.putExtra("user name", name);
                startActivity(in);
            }
        });
    }

    private void request_user_name() {

        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        //dept=sp.getString("dept","");
        name = sp.getString("name","");
       /* final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("enter name");

        final EditText input_field = new EditText(this);
        builder.setView(input_field);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_user_name();
            }
        });
        builder.show();*/
    }
}
