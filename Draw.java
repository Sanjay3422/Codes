package tab.swipe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Created by Sanjay's PC on 1/12/2017.
 */

public class Draw extends Activity implements View.OnClickListener{
        private DrawingView drawView;
    private float smallBrush, mediumBrush, largeBrush;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
    OutputStream out;
    String name;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_draw1);
            drawView = (DrawingView) findViewById(R.id.drawing);
            LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
            currPaint = (ImageButton)paintLayout.getChildAt(0);
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            smallBrush = getResources().getInteger(R.integer.small_size);
            mediumBrush = getResources().getInteger(R.integer.medium_size);
            largeBrush = getResources().getInteger(R.integer.large_size);
            drawBtn = (ImageButton)findViewById(R.id.draw_btn);
            drawBtn.setOnClickListener(this);
            eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
            eraseBtn.setOnClickListener(this);
            newBtn = (ImageButton)findViewById(R.id.new_btn);
            newBtn.setOnClickListener(this);
            saveBtn = (ImageButton)findViewById(R.id.save_btn);
            saveBtn.setOnClickListener(this);
        }

        public void paintClicked(View view){


            //use chosen color
            if(view!=currPaint){
                drawView.setErase(false);
                drawView.setBrushSize(drawView.getLastBrushSize());

                ImageButton imgView = (ImageButton)view;
                String color = view.getTag().toString();
                drawView.setColor(color);
                imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                currPaint=(ImageButton)view;
                drawView.setBrushSize(mediumBrush);
//update color
            }
        }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    //drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    //
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    //drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            final EditText input_field = new EditText(this);
            saveDialog.setView(input_field);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Enter Name to save");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    name= input_field.getText().toString();
                    if(name.isEmpty())
                    {
                        Toast.makeText(Draw.this, "Image name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                    drawView.setDrawingCacheEnabled(true);
                        drawView.buildDrawingCache();
                    String imgSaved = null;
                    //String name123= UUID.randomUUID().toString();
                        Bitmap map= drawView.getDrawingCache();
                        String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
                        File createDir = new File(root+"MyNote"+File.separator+"DrawText");
                        if(!createDir.exists()) {
                            createDir.mkdirs();
                        }
                        File file = new File(root + "MyNote/DrawText/"+name+".png");
                        try {
                            file.createNewFile();
                            OutputStream out = new FileOutputStream(file);
                            map.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();

                            imgSaved=MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(imgSaved!=null)
                        {
                            Toast savedToast = Toast.makeText(getApplicationContext(),
                                    "yah! Image saved.", Toast.LENGTH_SHORT);
                            savedToast.show();
                        }
                        else{
                            Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                    "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                            unsavedToast.show();
                        }
                    }


                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }


    }
}


