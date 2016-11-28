package droid.mynote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Main2Activity extends AppCompatActivity {
    final Context context = this;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editText= (EditText) findViewById(R.id.editText);
        Intent intent =getIntent();
        String action = intent.getAction();
        String type= intent.getType();

        if(Intent.ACTION_SEND.equals(action)&& type != null){
            if("text/plain".equals(type))
            {
                handleSendText(intent);
            }
            else {
            }
        }
    }
    private void handleSendText(Intent in) {
        String sharedText = in.getStringExtra(Intent.EXTRA_TEXT);
        if(sharedText != null){
            editText.setText(sharedText);

        }
    }

    @Override
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
            if(editText.getText().toString().isEmpty())
            {
                Toast.makeText(Main2Activity.this, "File is empty", Toast.LENGTH_SHORT).show();
            }
            else {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                final View promptsView = layoutInflater.inflate(R.layout.dialog, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final EditText text1 = (EditText) promptsView.findViewById(R.id.edit1);
                final TextView tv = (TextView) promptsView.findViewById(R.id.tv);
                alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (text1.getText().toString().isEmpty()) {
                            Toast t=Toast.makeText(Main2Activity.this,"Filename cannot be empty",Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.CENTER|Gravity.CENTER,0,0);
                            t.show();
                            //Toast.makeText(Main2Activity.this, "File cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            String filename = text1.getText().toString();
                            String data = editText.getText().toString();
                            FileOutputStream fos;
                            try {
                                fos = openFileOutput(filename, Context.MODE_PRIVATE);
                                fos.write(data.getBytes());
                                fos.close();
                                Toast.makeText(Main2Activity.this, filename + " saved successfully", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(Main2Activity.this, Main4Activity.class);
                                in.putExtra("a", filename);
                                in.putExtra("d", data);
                                startActivity(in);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(Main2Activity.this, "File not found", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(Main2Activity.this, "Storage Problem", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
