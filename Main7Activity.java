package tab.swipe;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main7Activity extends AppCompatActivity {

    EditText editText;
    String filename;
    Uri sharedText;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        editText= (EditText) findViewById(R.id.editt);
        Intent in=getIntent();
        String s=in.getStringExtra("d");
        filename=in.getStringExtra("a");
        setTitle(filename );
        editText.setText(s);
        Intent intent =getIntent();
        String action = intent.getAction();
        String type= intent.getType();

        if(Intent.ACTION_VIEW.equals(action)&& type != null){
            if("text/plain".equals(type))
            {
                try {
                    handleSendText(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
            }
        }
    }

    private void handleSendText(Intent in) throws IOException {
        sharedText = in.getData();
        count=1;
        String path = null;
        String[] projection = {MediaStore.MediaColumns.DATA};
        ContentResolver cr=getApplicationContext().getContentResolver();
        Cursor metaCursor = cr.query(sharedText,projection,null,null,null);
        if(metaCursor != null)
        {
            try
            {
                if(metaCursor.moveToFirst()) {
                    path= metaCursor.getString(0);
                }
            }finally {
                metaCursor.close();
                File f1= new File(path);
                setTitle(f1.getName());
            }
        }
        if(sharedText != null){
            StringBuffer stt= new StringBuffer();
            InputStreamReader reader= new InputStreamReader(getContentResolver().openInputStream(sharedText));
            try {
                int a= reader.read();
                while(a != -1){
                    stt.append((char)a);
                    a = reader.read();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Main7Activity.this, "File not found", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Main7Activity.this, "Some problem", Toast.LENGTH_SHORT).show();
            }
            finally {
                reader.close();
                editText.setText(stt);
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action) {
            try {
                write();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void write() throws IOException {

        if(count==0)
        {
            String data=editText.getText().toString();
            String root= Environment.getExternalStorageDirectory().getAbsolutePath();
            File f1= new File(root+"/MyNote/TextFiles");
            if(!f1.exists())
            {
                f1.mkdirs();
            }
            File file = new File(root + "/MyNote/TextFiles/"+filename);
            try {
                if(!file.exists())
                    file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter writer= new OutputStreamWriter(fos);
                try {
                    writer.write(data);
                    Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(Main7Activity.this, "File not found", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Main7Activity.this, "Some problem", Toast.LENGTH_SHORT).show();
                }
                finally {
                    writer.close();
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            OutputStreamWriter writer= new OutputStreamWriter(getContentResolver().openOutputStream(sharedText));
            String data=editText.getText().toString();
            try {
                writer.write(data);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Main7Activity.this, "File not found", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Main7Activity.this, "Some problem", Toast.LENGTH_SHORT).show();
            }
            finally {
                writer.close();
            }
        }
    }
}
