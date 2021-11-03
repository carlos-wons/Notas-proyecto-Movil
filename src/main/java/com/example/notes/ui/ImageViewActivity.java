package com.example.notes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notes.R;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    /**
     * Inicializa esta actividad, obtiene la dirección de la imagen enviada por el Activity notes
     * y la carga en la imageView.
     * @param savedInstanceState (Proporcionado por el sistema).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String res = extras.getString("imagePath");
        File imgFile = new File(res);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = findViewById(R.id.ImageViewActivity);
            imageView.setImageBitmap(myBitmap);
        }
    }
}