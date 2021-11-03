package com.example.notes.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.notes.R;

public class NotesFragment extends Fragment {

    private NotesViewModel notesViewModel;
    private SelectorNotesFragment selectorFragment;

    /**
     * Crea e infla el fragmento de notas.
     * @param savedInstanceState Bundle que permite la comunicación con otros fragmentos.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectorFragment = new SelectorNotesFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contenedor_pequenio_notes, selectorFragment).commit();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        return root;
    }

    /**
     * Actualiza el fragmento con los nuevos elementos agregados.
     */
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportFragmentManager().beginTransaction().detach(selectorFragment).attach(selectorFragment).commit();
    }
}