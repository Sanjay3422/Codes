package droid.mynote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        TextView text= (TextView) findViewById(R.id.text);
        Intent in =getIntent();
        String s=in.getStringExtra("d");
        setTitle(in.getStringExtra("a")+" - MyNote");
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setText(s);
    }
}
