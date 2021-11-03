package com.example.notes.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.notes.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomSheetNavigationFragment extends BottomSheetDialogFragment {

    private static NotesActivity notesActivity;
    private static boolean hasPermission;
    private static MenuItem menuVideo;

    /**
     * Retorna una nueva instancia de esta clase. Además, guarda una instancia de NotesActivity y recibe
     * si la aplicación tiene permiso para manejar archivos.
     * @param notesActivity1 Instancia para ejecutar funciones de NotesActivity desde este fragmento.
     * @param permission Verdadero si el usuario tiene permiso para editar archivos (Necesario para activar o desactivar función).
     * @return Retorna una instancia de este Fragmento.
     */
    public static BottomSheetNavigationFragment newInstance(NotesActivity notesActivity1, boolean permission) {
        Bundle args = new Bundle();
        notesActivity = notesActivity1;
        hasPermission = permission;
        BottomSheetNavigationFragment fragment = new BottomSheetNavigationFragment();
        fragment.setArguments(args);

        menuVideo = notesActivity.findViewById(R.id.notes_navigation_Tvideo);
        return fragment;
    }

    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.bottom_navigation_drawer, null);

        dialog.setContentView(contentView);

        // Revisa si hay permisos, si no hay, oculta la grabación de videos.
        NavigationView navigationView = contentView.findViewById(R.id.navigation_view_notes);
        if(!hasPermission) {
            navigationView.getMenu().findItem(R.id.notes_navigation_Tvideo).setVisible(false);
        }else {
            navigationView.getMenu().findItem(R.id.notes_navigation_Tvideo).setVisible(true);
        }
        //implementa el clic de los items.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notes_navigation_Tphoto:
                        notesActivity.dispatchTakePictureIntent();
                        break;
                    case R.id.notes_navigation_Tvideo:
                        notesActivity.dispatchTakeVideoIntent();
                        break;
                    case R.id.notes_navigation_record:
                        notesActivity.openRecordActivity();
                        break;
                }
                return false;
            }
        });

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}
