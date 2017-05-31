package sanjay.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class
Main2Activity extends AppCompatActivity {
    EditText ed,ed2;
    private ProgressDialog pDialog;
    JSONArray products = null;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        String b=sp.getString("a", "");
        if(b.equals("1"))
        {
            Intent in=new Intent(Main2Activity.this,MainActivity.class);
            startActivity(in);
            finish();
        }
        else{
            setContentView(R.layout.activity_main2);
        Button button= (Button) findViewById(R.id.button2);
        ed= (EditText) findViewById(R.id.editText);
        ed2= (EditText) findViewById(R.id.editText3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetLogin().execute(ed.getText().toString(),ed2.getText().toString());

            }
        });

    }}
    class GetLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Main2Activity.this);
            pDialog.setTitle("Logging in");
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            String s = "";
            try {
                String url_details = "http://share1234.16mb.com/db_get.php";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject json = jParser.makeHttpRequest(url_details, "POST", params);
                products = json.getJSONArray(TAG_PRODUCTS);
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);
                    String username = c.getString("username");
                    String password = c.getString("password");
                    String dept=c.getString("dept");
                    String name=c.getString("name");
                    String id=c.getString("id");
                    String phone = c.getString("phone");
                    String email = c.getString("email");
                    String position = c.getString("position");
                    if (username.equals(args[0]) && password.equals(args[1])) {
                        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("name",name);
                        ed.putString("dept",dept);
                        ed.putString("id",id);
                        ed.putString("phone",phone);
                        ed.putString("email",email);
                        ed.putString("position",position);
                        ed.apply();
                        s="true";
                        break;
                    }
                }
                if(s.equals("true")) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(Main2Activity.this, MainActivity.class);
                            startActivity(in);
                            String a="1";
                            String notificationC="yes";
                            String notificationG="yes";
                            SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit();
                            ed.putString("a",a);
                            ed.putString("nc",notificationC);
                            ed.putString("ng",notificationG);
                            ed.putString("na",notificationC);
                            ed.apply();
                            finish();
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }
}
