package sanjay.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sanjay's PC on 1/9/2017.
 */
public class Chat_Room extends AppCompatActivity {

    private EditText input_msg;
    private FloatingActionButton send_msg,aa;
    private TextView chat_conversation;
    private String user_name,room_name,tem_key;
    private DatabaseReference root;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        send_msg= (FloatingActionButton) findViewById(R.id.button4);
        input_msg= (EditText) findViewById(R.id.editText4);
        chat_conversation=(TextView) findViewById(R.id.textView2);

        user_name= getIntent().getExtras().get("user name").toString();
        room_name= getIntent().getExtras().get("room_name").toString();
        setTitle("room  - "+room_name);
        root= FirebaseDatabase.getInstance().getReference().child(room_name);

        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input_msg.getText().toString().isEmpty()) {}
                else {
                    Map<String,Object> map= new HashMap<String, Object>();
                    tem_key= root.push().getKey();
                    root.updateChildren(map);
                    DatabaseReference mess= root.child(tem_key);
                    Map<String,Object> map2= new HashMap<String, Object>();
                    map2.put("name",user_name);
                    map2.put("msg", input_msg.getText().toString());
                    mess.updateChildren(map2);
                    input_msg.setText("");
                }

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

    private String chat_msg, chat_username;

    private void append_app(DataSnapshot dataSnapshot) {

        Iterator i= dataSnapshot.getChildren().iterator();
         while (i.hasNext()){
             chat_msg= (String) ((DataSnapshot)i.next()).getValue();
             chat_username= (String) ((DataSnapshot)i.next()).getValue();

             chat_conversation.append(chat_username +": "+chat_msg +"\n");


         }
    }
}
