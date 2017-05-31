package sanjay.navigation;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main7Activity extends AppCompatActivity {

    private ProgressDialog pDialog;
    JSONArray products = null;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    JSONParser jParser = new JSONParser();
    Spinner sp,sp1;
    ListView lv;
    String a,a1;
    int n,n1;
    Myadapter adapter;
    private ArrayList<String> list_of_rooms =  new ArrayList<>();
    private ArrayList<String> list_of_rooms1 =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        lv= (ListView) findViewById(R.id.lv);
        sp= (Spinner) findViewById(R.id.spinner);
        sp1= (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.two,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter1= ArrayAdapter.createFromResource(this,R.array.t,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp1.setAdapter(adapter1);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!(n==0))
                {
                    if(!(list_of_rooms==null)) list_of_rooms.clear();
                    if(!(list_of_rooms1==null)) list_of_rooms1.clear();
                    if(!(position==0))new GetLogin().execute(a, String.valueOf(position));
                }
                else
                {
                    Toast.makeText(Main7Activity.this, "Select department First", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a=parent.getItemAtPosition(position).toString();
                n=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    class GetLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Main7Activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            try {
                if(args[1].equals("1")) a1="15";
                if(args[1].equals("2")) a1="14";
                if(args[1].equals("3")) a1="10";
                String url_details = "http://share1234.16mb.com/db_get.php";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject json = jParser.makeHttpRequest(url_details, "POST", params);
                products = json.getJSONArray(TAG_PRODUCTS);
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);
                    String dept=c.getString("dept");
                    String position = c.getString("position");
                    String username = c.getString("username");
                    if (dept.equalsIgnoreCase(args[0])&& position.equalsIgnoreCase("student")&& username.startsWith(a1) ){
                        String name=c.getString("name");
                        String phone = c.getString("phone");
                        String email = c.getString("email");
                        list_of_rooms1.add(name);
                        list_of_rooms.add("Reg No  : "+ username +"\n"+"Position: "+ position+"\n"+"Phone   : "+ phone+"\n"+"Email   : "+email);
                    }
                }

            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            adapter= new Myadapter(Main7Activity.this,list_of_rooms,list_of_rooms1);
            lv.setAdapter(adapter);
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

}



