package com.example.notes.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.data.DaoImages;
import com.example.notes.data.DaoNotes;
import com.example.notes.data.DaoRecorders;
import com.example.notes.data.DaoReminders;
import com.example.notes.data.DaoVideos;
import com.example.notes.models.Image;
import com.example.notes.models.Note;
import com.example.notes.models.Reminders;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class NotesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READEXTERNAL = 1001;
    private static final int RECORD_ACTIVITY_RESULT = 1824;
    ArrayList<Image> images;
    ArrayList<Image> newImages;
    ArrayList<String> videos;
    ArrayList<String> newVideos;
    boolean hasPermissions;
    static final int REQUEST_TAKE_PHOTO = 1;
    private int idImageOld;
    Chip chipDate;
    BottomAppBar bottomAppBar;
    private TextInputEditText tieTitle;
    private EditText etContent;
    long id;
    Reminders reminder;
    Note note;
    private DaoReminders daoReminders;
    private DaoNotes daoNotes;
    boolean isReminder;
    Button btnAccept;
    Button btnCancel;
    Button btnDelete;
    Button btnDate;
    Button btnTime;
    ImageView imageViewCharged;
    ImageView videoViewCharged;
    ImageView recordViewCharged;
    private DaoImages daoImages;
    private DaoRecorders daoRecorders;
    private DaoVideos daoVideos;
    private ImageView imageViewNew;
    private ImageView videoViewNew;
    private ImageView recordViewNew;
    private int idImageNew;
    private int rowCounterImages;
    private String currentVideoPath;
    private TableLayout tableLayoutImages;
    private TableLayout tableLayoutVideos;
    private TableLayout tableLayoutRecords;
    private TableRow tableRowImages;
    private TableRow tableRowVideos;
    private TableRow tableRowRecords;
    private int rowCounterVideos;
    private int idVideoNew;
    private int idVideoOld;
    private ArrayList<String> records;
    private ArrayList<String> newRecords;
    private int rowCounterRecords;
    private int idRecordNew;
    private int idrecordOld;


    /**
     * Carga todos los componentes necesarios para el Activity Actual
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        // Inicia
        tableLayoutImages = findViewById(R.id.activity_notes_table_images);
        tableLayoutVideos = findViewById(R.id.activity_notes_table_videos);
        tableLayoutRecords = findViewById(R.id.activity_notes_table_records);
        tableRowImages = new TableRow(this);
        tableRowVideos = new TableRow(this);
        tableRowRecords = new TableRow(this);
        tableLayoutImages.addView(tableRowImages);
        tableLayoutVideos.addView(tableRowVideos);
        tableLayoutRecords.addView(tableRowRecords);
        hasPermissions = false;
        images = new ArrayList<>();
        newImages = new ArrayList<>();
        videos = new ArrayList<>();
        newVideos = new ArrayList<>();
        records = new ArrayList<>();
        newRecords = new ArrayList<>();
        isReminder = false;
        daoNotes = new DaoNotes(getApplicationContext());
        daoReminders = new DaoReminders(getApplicationContext());
        daoImages = new DaoImages(getApplicationContext());
        daoVideos = new DaoVideos(getApplicationContext());
        daoRecorders = new DaoRecorders(getApplicationContext());
        note = new Note();
        reminder = new Reminders();
        tieTitle = findViewById(R.id.activity_notes_textinputedittext);
        etContent = findViewById(R.id.activity_notes_content);
        chipDate = findViewById(R.id.activity_notes_date_chip);
        id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
            getReminder();
        }
        bottomAppBar = findViewById(R.id.bottomAppBarNotes);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_notes);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_notes);
        // Asignar el action bar personalizado para este activity y configurarlo para que muestre
        // la flecha de regreso.
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
                finish();
            }
        });
        // Termina asignación de action bar
        bottomAppBarDefinition();
        chipDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
    }

    /**
     * Ocurre cuando se presiona la tecla de retroceso, agrega llama al método de agregar nota y
     * finaliza la actividad.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addNote();
        finish();
    }

    /**
     * Scede cuando se crean las opciones del menú. Verifica si es una nueva nota, si lo es, quita la
     * opción de eliminar, en caso contrario no hace nada.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bottom_notes, menu);
        if (id == -1) {
            menu.findItem(R.id.menu_bottom_notes_delete).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Se ejecuta cada que una actividad que esta llamó, se cierra. Si la requisición fue de tomar
     * una foto, ejecuta el método makeImageView, si la requisición fue de un video, makeVideoView y
     * si es de audios, makeRecordView
     * @param requestCode El código que solicitó la apertura.
     * @param resultCode Resultado de la apertura.
     * @param data Los datos que son recibidos del Activity que se cerró.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (id == -1) {
                makeImageView(currentPhotoPath, false);
            } else {
                makeImageView(currentPhotoPath, true);
            }
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            currentVideoPath = videoUri.toString();
            if (id == -1) {
                videos.add(currentVideoPath);
                makeVideoView(currentVideoPath, false);
            } else {
                newVideos.add(currentVideoPath);
                makeVideoView(currentVideoPath, true);
            }
        }
        if (requestCode == RECORD_ACTIVITY_RESULT && resultCode == RESULT_OK) {
            String currentRecordPath;
            currentRecordPath = data.getStringExtra("RecordFile");
            if (id == -1) {
                records.add(currentRecordPath);
                makeRecordView(currentRecordPath, false);
            } else {
                newRecords.add(currentRecordPath);
                makeRecordView(currentRecordPath, true);
            }
        }
    }

    /**
     * Muestra un TimePickerDialog y obtiene la fecha elegida, colocándola en el botón correspondiente a
     * la hora.
     * @param v
     */
    public void showTimePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm", Locale.US);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NotesActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                btnTime.setText(dateFormatter.format(calendar.getTime()));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    /**
     * Muestra un calendarPicker, la fecha optenida la coloca en el botón la fecha obtenida.
     * @param v
     */
    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy", Locale.US);

        DatePickerDialog datePickerDialog = new DatePickerDialog(NotesActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                btnDate.setText(dateFormatter.format(calendar.getTime()));

            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * Inicia un Intent encargado de abrir la cámara. Si se pudo crear bien se crea un nuevo archivo
     * encargado de almacenar la foto y posterior a esto, obtiene mediante un provider un URI, el cual permite
     * almacenar la foto.
     */
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.notes.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Crea un imageView que almacenará una miniatura de la foto tomada, la va a agregar al tableLayout
     * especial para imágenes. Además, le da formato y tamaño a la imagen para que se muestre de la manera correcta.
     * Finalmente, agrega el método onClick a cada imageView para que cuando se de clic sobre este, abra un nuevo
     * Activity para mostrar la foto con detalle.
     * @param srcImage Dirección donde se encuentra almacenada la imagen.
     * @param isNew Es imagen nueva, verdadero si se acaba de tomar, falso si se sacó de la base de datos.
     */
    private void makeImageView(String srcImage, boolean isNew) {
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / dimensionInDp, photoH / dimensionInDp);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        if (isNew) {
            imageViewNew = new ImageView(this);
            Bitmap imageBitmap = BitmapFactory.decodeFile(srcImage, bmOptions);
            float proporcion = 200 / (float) imageBitmap.getWidth();
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 200, (int) (imageBitmap.getHeight() * proporcion), false);
            imageViewNew.setImageBitmap(imageBitmap);
            imageViewNew.setId(idImageNew++);
            rowCounterImages++;
            imageViewNew.setPadding(5, 5, 5, 5);
            tableRowImages.addView(imageViewNew);
            imageViewNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NotesActivity.this, ImageViewActivity.class);
                    intent.putExtra("imagePath", newImages.get(view.getId()).getSrcImage());
                    startActivity(intent);
                }
            });
        } else {
            imageViewCharged = new ImageView(this);
            Bitmap imageBitmap = BitmapFactory.decodeFile(srcImage, bmOptions);
            float proporcion = 200 / (float) imageBitmap.getWidth();
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 200, (int) (imageBitmap.getHeight() * proporcion), false);
            imageViewCharged.setImageBitmap(imageBitmap);
            imageViewCharged.setId(idImageOld++);
            rowCounterImages++;
            imageViewCharged.setPadding(5, 5, 5, 5);
            tableRowImages.addView(imageViewCharged);
            imageViewCharged.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NotesActivity.this, ImageViewActivity.class);
                    intent.putExtra("imagePath", images.get(view.getId()).getSrcImage());
                    startActivity(intent);
                }
            });
        }
        if ((rowCounterImages % 5) == 0) {
            tableRowImages = new TableRow(this);
            tableLayoutImages.addView(tableRowImages);
        }
    }

    /**
     * Crea un imageView que almacenará una representación del video tomado, la va a agregar al tableLayout
     * especial para videos. Además, le da tamaño al imageView para que se muestre de la manera correcta.
     * Finalmente, agrega el método onClick a cada imageView para que cuando se de clic sobre este, abra un nuevo
     * Activity para mostrar el video con detalle.
     * @param srcVideo URI del video.
     * @param isNew Es video nuevo, verdadero si se acaba de tomar, falso si se sacó de la base de datos.
     */
    private void makeVideoView(String srcVideo, boolean isNew) {
        if (isNew) {
            videoViewNew = new ImageView(this);
            videoViewNew.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
            videoViewNew.setId(idVideoNew++);
            rowCounterVideos++;
            videoViewNew.setPadding(5, 5, 5, 5);
            tableRowVideos.addView(videoViewNew);
            videoViewNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NotesActivity.this, VideoViewActivity.class);
                    intent.putExtra("videoPath", newVideos.get(view.getId()));
                    startActivity(intent);
                }
            });
        } else {
            videoViewCharged = new ImageView(this);
            videoViewCharged.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
            videoViewCharged.setId(idVideoOld++);
            rowCounterVideos++;
            videoViewCharged.setPadding(5, 5, 5, 5);
            tableRowVideos.addView(videoViewCharged);
            videoViewCharged.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NotesActivity.this, VideoViewActivity.class);
                    intent.putExtra("videoPath", videos.get(view.getId()));
                    startActivity(intent);
                }
            });
        }
        if ((rowCounterVideos % 5) == 0) {
            tableRowVideos = new TableRow(this);
            tableLayoutVideos.addView(tableRowVideos);
        }
    }

    /**
     * Crea un imageView que almacenará una representación del audio grabado, la va a agregar al tableLayout
     * especial para grabaciones. Además, le da tamaño al imageView para que se muestre de la manera correcta.
     * Finalmente, agrega el método onClick a cada imageView para que cuando se de clic sobre este, reproduzca
     * el audio almacenado.
     * @param srcRecord Dirección de donde está almacenado el audio.
     * @param isNew Es audio nuevo, verdadero si se acaba de tomar, falso si se sacó de la base de datos.
     */
    private void makeRecordView(String srcRecord, boolean isNew) {
        if (isNew) {
            recordViewNew = new ImageView(this);
            recordViewNew.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
            recordViewNew.setId(idRecordNew++);
            rowCounterRecords++;
            recordViewNew.setPadding(5, 5, 5, 5);
            tableRowRecords.addView(recordViewNew);
            recordViewNew.setOnClickListener(new View.OnClickListener() {
                View thisView;
                boolean mStartPlaying = true;

                private MediaPlayer   player = null;
                private void onPlay(boolean start) {
                    if (start) {
                        startPlaying();
                    } else {
                        stopPlaying();
                    }
                }

                private void startPlaying() {
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(newRecords.get(thisView.getId()));
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        Log.e("taBuga", "prepare() ta Buga");
                    }
                }

                private void stopPlaying() {
                    player.release();
                    player = null;
                }
                @Override
                public void onClick(View view) {
                    thisView = view;
                    onPlay(mStartPlaying);
                    mStartPlaying = !mStartPlaying;
                }
            });
        } else {
            recordViewCharged = new ImageView(this);
            recordViewCharged.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
            recordViewCharged.setId(idrecordOld++);
            rowCounterRecords++;
            recordViewCharged.setPadding(5, 5, 5, 5);
            tableRowRecords.addView(recordViewCharged);
            recordViewCharged.setOnClickListener(new View.OnClickListener() {
                View thisView;
                boolean mStartPlaying = true;

                private MediaPlayer   player = null;
                private void onPlay(boolean start) {
                    if (start) {
                        startPlaying();
                    } else {
                        stopPlaying();
                    }
                }

                private void startPlaying() {
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(records.get(thisView.getId()));
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        Log.e("taBuga", "prepare() ta Buga");
                    }
                }

                private void stopPlaying() {
                    player.release();
                    player = null;
                }
                @Override
                public void onClick(View view) {
                    thisView = view;
                    onPlay(mStartPlaying);
                    mStartPlaying = !mStartPlaying;
                }
            });
        }
        if ((rowCounterRecords % 5) == 0) {
            tableRowRecords = new TableRow(this);
            tableLayoutRecords.addView(tableRowRecords);
        }
    }

    /**
     * Define la navegación ubicada en la parte inferior. Crea los menús, el segundo menú es creado
     * con el fragmento BottomSheetNavigationFragment. Agrega comportamiento a los items del menú.
     */
    private void bottomAppBarDefinition() {
        //find id
        bottomAppBar = findViewById(R.id.bottomAppBarNotes);

        //set bottom bar to Action bar as it is similar like Toolbar
        setSupportActionBar(bottomAppBar);

        //click event over Bottom bar menu item
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_bottom_notes_reminders:
                        showCustomDialog();
                        break;
                    case R.id.menu_bottom_notes_delete:
                        new AlertDialog.Builder(NotesActivity.this)
                                .setTitle("Delete")
                                .setMessage("Are you sure to delete this note?")
                                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        daoReminders.delete(id);
                                        Toast.makeText(NotesActivity.this, "Eliminado correctamente.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).setNegativeButton("Cancel", null).show();
                        break;
                }
                return false;
            }
        });


        //click event over navigation menu like back arrow or hamburger icon
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    validatePermission();
                }
                //open bottom sheet
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance(NotesActivity.this, hasPermissions);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });


    }

    /**
     * Crea un un Dialog Personalizado. Lo obtiene de dialog_custom_layout.xml que le da definición a la interfaz.
     * Además, le da comportamiento a los botones.
     */
    private void showCustomDialog() {
        AlertDialog.Builder alertDialogDateChooser = new AlertDialog.Builder(NotesActivity.this);
        View customView = LayoutInflater.from(NotesActivity.this).inflate(R.layout.dialog_custom_layout, null);
        //Set current date to button
        btnDate = customView.findViewById(R.id.dialog_button_get_finishdate);
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
        btnDate.setText(dateFormatter.format(calendar.getTime()));
        // Finish current date
        // Set current time to button
        final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
        btnTime = customView.findViewById(R.id.dialog_button_get_finishtime);
        btnTime.setText(timeFormatter.format(calendar.getTime()));
        // Finish current time on button
        btnAccept = customView.findViewById(R.id.btnAcceptDialog);
        btnCancel = customView.findViewById(R.id.btnCancelDialog);
        btnDelete = customView.findViewById(R.id.btnDeleteDialog);
        if (isReminder) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.INVISIBLE);
        }
        alertDialogDateChooser.setView(customView);
        final AlertDialog customDialog = alertDialogDateChooser.create();
        customDialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.cancel();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipDate.setVisibility(View.VISIBLE);
                String text = btnDate.getText() + ". " + btnTime.getText();
                chipDate.setText(text);
                isReminder = true;
                customDialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != -1) {
                    deleteNotify(reminder.getId() + "");
                }
                chipDate.setText("");
                chipDate.setVisibility(View.INVISIBLE);
                isReminder = false;
                customDialog.dismiss();
            }
        });
    }

    /**
     * Agrega una nota a la base de datos. Si el título o el contenido están vacíos, no almacena nada.
     * Además, manda almacenar las fotos, los videos y los audios grabados.
     */
    private void addNote() {
        long idInserted;
        String title = tieTitle.getText().toString();
        String content = etContent.getText().toString();
        Intent intent = new Intent();
        //Sucede si No tiene texto el título o el contenido, cancela el guardado.
        if (title.isEmpty() || title == null || title.matches("^[ \n\r]+$") || content.isEmpty() || content == null || title.matches("^[ \n\r]+$")) {
            Toast.makeText(NotesActivity.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
            deleteImages();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            if (id == -1) {
                // Sucede si es una nota nueva
                if (isReminder) {
                    // Si es un recordatorio
                    Reminders reminder;
                    reminder = new Reminders(title, content, 1, btnDate.getText() + ". " + btnTime.getText());
                    idInserted = daoReminders.insertReminder(reminder);
                    if (idInserted != -1) {
                        Toast.makeText(NotesActivity.this, "Recordatorio agregado correctamente", Toast.LENGTH_SHORT).show();
                        saveImages(idInserted);
                        saveVideos(idInserted);
                        saveRecords(idInserted);
                        saveNotify();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(NotesActivity.this, "No se pudo agregar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Note note;
                    note = new Note(title, content, 0);
                    idInserted = daoNotes.insertNote(note);
                    if (idInserted != -1) {
                        saveImages(idInserted);
                        saveVideos(idInserted);
                        saveRecords(idInserted);
                        Toast.makeText(NotesActivity.this, "Nota agregada correctamente", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(NotesActivity.this, "No se pudo agregar", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (isReminder) {
                    this.reminder.setTitle(tieTitle.getText().toString());
                    this.reminder.setContent(etContent.getText().toString());
                    this.reminder.setFinishDate(chipDate.getText().toString());
                    this.reminder.setReminder(1);
                    if (daoReminders.update(this.reminder)) {
                        daoImages.insertImage(id, newImages);
                        daoVideos.insertVideo(id, newVideos);
                        daoRecorders.insertRecorder(id,newRecords);
                        Toast.makeText(NotesActivity.this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotesActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    this.note.setTitle(tieTitle.getText().toString());
                    this.note.setContent(etContent.getText().toString());
                    this.note.setReminder(0);
                    if (daoNotes.update(this.note)) {
                        daoImages.insertImage(id, newImages);
                        daoVideos.insertVideo(id, newVideos);
                        daoRecorders.insertRecorder(id,newRecords);
                        Toast.makeText(NotesActivity.this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotesActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * Create an object of reminder and note, and set the values for the elements in the view.
     */
    private void getReminder() {
        reminder = daoReminders.getOneById(id);
        if (reminder.isReminder() == 0) {
            note = new Note();
            note.setId(reminder.getId());
            note.setTitle(reminder.getTitle());
            note.setContent(reminder.getContent());
            note.setReminder(reminder.isReminder());
        }
        images = daoImages.getAll(id);
        videos = daoVideos.getAll(id);
        records = daoRecorders.getAll(id);
        showImages();
        showVideos();
        showRecords();
        tieTitle.setText(reminder.getTitle());
        etContent.setText(reminder.getContent());
        if (reminder.isReminder() == 1) {
            isReminder = true;
            chipDate.setVisibility(View.VISIBLE);
            chipDate.setText(reminder.getFinishDate());
        }
    }

    /**
     * Delete the notificaiton.
     *
     * @param tag id of the notification to delete
     */
    private void deleteNotify(String tag) {
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(this, "Para ver si se elimina", Toast.LENGTH_SHORT).show();
    }

    /**
     * Generate a random key.
     *
     * @return Random key
     */
    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * Make a Data type to send it to the workmanager and save the notification.
     *
     * @param title          Title that the notification will have
     * @param content        Content of the notification
     * @param idNotification id to identify the notification
     * @return Data that contain the title, content and id of the notification
     */
    private Data saveData(String title, String content, long idNotification) {
        return new Data.Builder()
                .putString("Title", title)
                .putString("Content", content)
                .putLong("id", idNotification).build();
    }

    /**
     * Transform the date contained in the chip button to a Date format and create a new notify with the
     * saveNotification method.
     */
    private void saveNotify() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy. HH:mm");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(chipDate.getText().toString());
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        String tag = generateKey();
        Date justNow = new Date();
        long alertTime = date.getTime() - justNow.getTime();
        Data data = saveData(tieTitle.getText().toString(), etContent.getText().toString(), id);
        WorkManagerNotify.saveNotification(alertTime, data, id + "");

    }

    String currentPhotoPath;

    /**
     * Create a file that contains the image to save
     *
     * @return The file with the image.
     * @throws IOException If the file can't be created
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        if (id == -1) {
            images.add(new Image(currentPhotoPath));
        } else {
            newImages.add(new Image(id, currentPhotoPath));
        }
        return image;
    }

    private void deleteImages() {
        for (int i = 0; i < images.size(); i++) {
            File fDelete = new File(images.get(i).getSrcImage());
            if (fDelete.exists()) {
                fDelete.delete();
            }
        }
    }

    private void saveImages(long id) {
        daoImages.insertImage(id, images);
    }

    private void saveRecords(long id) {
        daoRecorders.insertRecorder(id,records);
    }

    private void saveVideos(long id) {
        daoVideos.insertVideo(id, videos);
    }

    private void showImages() {
        for (int i = 0; i < images.size(); i++) {
            makeImageView(images.get(i).getSrcImage(), false);
        }
    }

    private void showVideos() {
        for (int i = 0; i < videos.size(); i++) {
            makeVideoView(videos.get(i), false);
        }
    }

    private void showRecords() {
        for (int i = 0; i < records.size(); i++) {
            makeRecordView(records.get(i), false);
        }
    }

    static final int REQUEST_VIDEO_CAPTURE = 2;

    /**
     * Solicitud para grabar
     */
    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    /**
     * Solicita permiso para almacenar y leer archivos.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void validatePermission() {

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            hasPermissions = true;
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.

            new AlertDialog.Builder(NotesActivity.this)
                    .setMessage("Es necesario que concedas permisos para leer archivos. De esta manera, podremos mostrarte el video grabado")
                    .setPositiveButton(R.string.accept, null)
                    .show();

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READEXTERNAL);

        } else {
            // You can directly ask for the permission.
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READEXTERNAL);
        }

    }

    /**
     * Sucede cuando se acciona alguna opción de algún permiso. Si se le da permiso, se establece la
     * variable hasResult como true, en caso contrario, muestra un mensaje de que se desabilitará la función
     * de grabar un video.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_CODE_READEXTERNAL) {

            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermissions = true;
                } else {
                    Toast.makeText(NotesActivity.this, "Opción de videos deshabilitada por falta de permisos.", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    /**
     * Abre el RecordActivity.
     */
    public void openRecordActivity() {
        Intent intent = new Intent(NotesActivity.this, RecordActivity.class);
        startActivityForResult(intent, RECORD_ACTIVITY_RESULT);
    }
}