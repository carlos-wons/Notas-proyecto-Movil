package com.example.notes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.notes.R;

import java.io.File;

public class VideoViewActivity extends AppCompatActivity {

    MediaSessionCompat mediaSession;
    PlaybackStateCompat.Builder stateBuilder;

    /**
     * Inicializa los elementos del activity, obtiene el URI que apunta a un video enviado por el
     * ActivityNotes y lo carga en un videoView.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        Uri video = Uri.parse(extras.getString("videoPath"));
        VideoView videoView = findViewById(R.id.videoViewActivity);
        videoView.setVideoURI(video);
        //videoView.setVideoPath(video.getPath());
        videoView.start();
    }
}