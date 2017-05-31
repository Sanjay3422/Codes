package tab.swipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
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

public class Main6Activity extends AppCompatActivity {

    final Context context = this;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
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
                Toast.makeText(Main6Activity.this, "File is empty", Toast.LENGTH_SHORT).show();
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
                            Toast t=Toast.makeText(Main6Activity.this,"Filename cannot be empty",Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.CENTER|Gravity.CENTER,0,0);
                            t.show();
                        } else {
                            String filename = text1.getText().toString();
                            String data = editText.getText().toString();
                            String root= Environment.getExternalStorageDirectory().getAbsolutePath();
                            File f1= new File(root+"/MyNote/TextFiles");
                            if(!f1.exists())
                            {
                                f1.mkdirs();
                            }
                            File file = new File(root + "/MyNote/TextFiles/"+filename+".txt");
                            try {
                                if(!file.exists())
                                file.createNewFile();
                                FileOutputStream fos = new FileOutputStream(file);
                                OutputStreamWriter writer= new OutputStreamWriter(fos);
                                try {
                                    writer.write(data);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Main6Activity.this, "File not found", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Main6Activity.this, "Some problem", Toast.LENGTH_SHORT).show();
                                }
                                finally {
                                    writer.close();
                                    fos.close();
                                    Intent in= new Intent(Main6Activity.this,Main7Activity.class);
                                    in.putExtra("d",data);
                                    in.putExtra("a",filename+".txt");
                                    startActivity(in);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
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
