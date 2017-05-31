package sanjay.navigation;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class Main5Activity extends AppCompatActivity {
    CheckBox cb,cb1,cb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        cb= (CheckBox) findViewById(R.id.checkBox);
        cb1= (CheckBox) findViewById(R.id.checkBox2);
        cb3= (CheckBox) findViewById(R.id.checkBox3);
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        if(sp.getString("nc","").equalsIgnoreCase("yes")) cb.setChecked(true);
        else cb.setChecked(false);
        if(sp.getString("ng","").equalsIgnoreCase("yes")) cb1.setChecked(true);
        else cb1.setChecked(false);
        if(sp.getString("na","").equalsIgnoreCase("yes")) cb3.setChecked(true);
        else cb3.setChecked(false);
    }

    public void onClick(View view)
    {
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        if(view.getId()==R.id.checkBox)
        {
            if((cb.isChecked()))
            {
                ed.putString("nc","yes");
                ed.apply();
            }
            else {
                ed.putString("nc","no");
                ed.apply();
            }
        }

        if(view.getId()==R.id.checkBox2)
        {
            if((cb1.isChecked()))
            {
                ed.putString("ng","yes");
                ed.apply();
            }
            else {
                ed.putString("ng","no");
                ed.apply();
            }
        }

        if(view.getId()==R.id.checkBox3)
        {
            if((cb3.isChecked()))
            {
                ed.putString("na","yes");
                ed.apply();
            }
            else {
                ed.putString("na","no");
                ed.apply();
            }
        }

    }
}
