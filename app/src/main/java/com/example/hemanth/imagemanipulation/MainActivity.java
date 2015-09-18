package com.example.hemanth.imagemanipulation;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import com.squareup.picasso.Picasso;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {


    ImageView image;
    String imgPath =null,imageUrl = null;
    Context context = this;
    private Uri fileUri;
    int RESULT_CAPTURE_IMAGE = 2,RESULT_LOAD_IMG = 1;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image);
        Button invertbutton = (Button) findViewById(R.id.invertbutton);
        Button savebutton = (Button) findViewById(R.id.savebutton);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasCamera())
                    Toast.makeText(context,"You Don't have a Camera",Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,RESULT_CAPTURE_IMAGE);
                }
            }
        });

        invertbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null) {
                    image.setImageBitmap(invertImage(bitmap));
                }
                else
                    Toast.makeText(context,"Click the Photograph first",Toast.LENGTH_SHORT).show();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null) {
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "InvertImage", "Description");
                    Toast.makeText(context,"Image Saved",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(context,"Image is Empty",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Bitmap invertImage(Bitmap original){
        Bitmap newbitmap = Bitmap.createBitmap(original.getWidth(),original.getHeight(),original.getConfig());
        int a,r,g,b;
        int pixelcolor;
        int height = original.getHeight();
        int width = original.getWidth();
        for(int i =0 ;i<height;i++){
            for(int j =0;j<width;j++){
                pixelcolor = original.getPixel(j,i);
                a = Color.alpha(pixelcolor);
                r = 255 - Color.red(pixelcolor);
                g = 255 - Color.green(pixelcolor);
                b = 255 - Color.blue(pixelcolor);
                newbitmap.setPixel(j,i,Color.argb(a,r,g,b));
            }
        }
        return newbitmap;
    }


    public Boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_CAPTURE_IMAGE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(bitmap);
        }
        else
            Toast.makeText(context,"You Didn't took a Picture",Toast.LENGTH_SHORT).show();
    }
}
