package com.example.notes.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.notes.R;

import java.util.concurrent.TimeUnit;

public class WorkManagerNotify extends Worker {

    /**
     * Constructor.
     * @param context (Proporcionado por el sistema).
     * @param workerParams (Proporcionado por el sistema).
     */
    public WorkManagerNotify(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * Procesa la información para la notificación, para posteriormente programar una tarea que muestre
     * una notificación.
     * @param duration Tiempo que aparecerá la notificación en aparecer.
     * @param data Contenido de la notificación (Título y contenido).
     * @param tag Identificador de la notificación para la tarea.
     */
    public static void saveNotification(long duration, Data data, String tag) {
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(WorkManagerNotify.class)
                .setInitialDelay(duration, TimeUnit.MILLISECONDS).addTag(tag).setInputData(data).build();
        WorkManager instance = WorkManager.getInstance();
        instance.enqueue(oneTimeWorkRequest);
    }

    /**
     * Se ejecuta cuando el tiempo asignado se haya cumplido. Manda llamar al método notify pra mostrar
     * la notificación, dándole los valores obtenidos en el método saveNotification.
     * @return
     */
    @NonNull
    @Override
    public Result doWork() {
        String title = getInputData().getString("Title");
        String content = getInputData().getString("Content");
        int id = (int) getInputData().getLong("id", 0);
        notify(title, content, id);
        return Result.success();
    }


    /**
     * Crea y muestra la notificación. Para Android Oreo en adelante, es necesario generar un Id para
     * poder ser registrado (Ya lo hace el código).
     * @param title Título que la notificación mostrará.
     * @param content Contenido de la notificación.
     * @param id ID de la notificación
     */
    private void notify(String title, String content, int id) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id + "", "Finish notification", importance);
            channel.setDescription("This is a notification for finish date of a reminder.");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), id + "")
                .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());

    }

}
