package tab.swipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        TextView text= (TextView) findViewById(R.id.text);
        Intent in =getIntent();
        String s=in.getStringExtra("d");
        setTitle(in.getStringExtra("a"));
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setText(s);
    }
}
