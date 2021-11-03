package com.example.notes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.notes.R;
import com.example.notes.models.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {

    private Button recordButton;
    private Button playButton;
    private Button saveButton;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private MediaRecorder recorder = null;
    private MediaPlayer   player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private String currentRecordPath;

    /**
     * Solicita permiso para grabar audio, si no se concede, se cierra el activity.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    /**
     * Comienza a grabar o termina la grabación dependiendo del valor que recibe. Lo hace llamando a los métodos
     * correspondientes.
     * @param start
     */
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    /**
     * Reproduce o para la reproducción dependiendo del valor boleano. Lo hace llamando a los métodos
     * correspondientes.
     * @param start
     */
    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    /**
     * Comienza la reproducción del audio, obteniendo la dirección de la grabación.
     */
    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    /**
     * Detiene la reproducción
     */
    private void stopPlaying() {
        player.release();
        player = null;
    }

    /**
     * Configura y comienza a grabar.
     */
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    /**
     * Para la grabación.
     */
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    /**
     * Libera la memoria
     */
    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    /**
     * Método que permite copiar un archivo a otro. Funciona para copiar el audio del cache a un audio
     * permanente.
     * @param src Ruta del caché donde se encuentra el audio
     * @return Un nuevo archivo guardado como permanente.
     * @throws IOException
     */
    public File copy(File src) throws IOException {
        File dst = createNewRecordFile();
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
            return dst;
        }
    }

    /**
     * Guarda el archivo del caché.
     * @return Archivo del caché almacenao en un File.
     * @throws IOException
     */
    private File createRecordFile() throws IOException {
        File record = new File(fileName);
        return record;
    }

    /**
     * Crea un nuevo archivo, que será donde se almacena el archivo de audio si se guarda.
     * @return Un nuevo archivo con la nueva ruta.
     * @throws IOException
     */
    private File createNewRecordFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String recordFileName = "3GP_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File record = File.createTempFile(
                recordFileName,  /* prefix */
                ".3pg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentRecordPath = record.getAbsolutePath();
        return record;
    }

    /**
     * Inicializa los componentes, y les da comportamiento a los botones.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        recordButton = findViewById(R.id.activity_record_record);
        playButton = findViewById(R.id.activity_record_play);
        saveButton = findViewById(R.id.activity_record_save);

        playButton.setEnabled(false);
        saveButton.setEnabled(false);
        recordButton.setOnClickListener(new View.OnClickListener() {
            boolean mStartRecording = true;
            @Override
            public void onClick(View view) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    recordButton.setText(R.string.stop_recording);
                } else {
                    saveButton.setEnabled(true);
                    playButton.setEnabled(true);
                    recordButton.setText(R.string.record_audio);
                }
                mStartRecording = !mStartRecording;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            boolean mStartPlaying = true;

            @Override
            public void onClick(View view) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    playButton.setText(R.string.stop);
                } else {
                    playButton.setText(R.string.play);
                }
                mStartPlaying = !mStartPlaying;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File result = null;
                try {
                    result = copy(createRecordFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent output = new Intent();
                output.putExtra("RecordFile",result.getAbsolutePath());
                setResult(RESULT_OK, output);
                finish();
            }
        });

        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }
}