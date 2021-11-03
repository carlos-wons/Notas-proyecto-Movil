package com.example.notes.ui.notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.ui.NotesActivity;
import com.example.notes.R;
import com.example.notes.data.DaoNotes;
import com.example.notes.models.Note;
import com.example.notes.ui.adapters.NotesAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectorNotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectorNotesFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView rvDdata;
    private GridLayoutManager layoutManager;
    public static NotesAdapter notesAdapter;

    public SelectorNotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectorNotesFragment.
     */
    public static SelectorNotesFragment newInstance(String param1, String param2) {
        SelectorNotesFragment fragment = new SelectorNotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Obtiene los parámetros transferidos.
     * @param savedInstanceState Bundle que permite el intercambio de datos.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Crea la interfaz del fragmento, además define el método onClick, permitiendo abrir la
     * actividad NotesActivity al cliquear cualquier nota, mándando un ID para poder mostrar la información en
     * aquella actividad.
     * @param inflater (Proporcionado por el sistema).
     * @param container (Proporcionado por el sistema).
     * @param savedInstanceState (Proporcionado por el sistema).
     * @return View del fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_selector_notes, container, false);
        rvDdata = (RecyclerView) v.findViewById(R.id.fragment_note_selector_RecyclerView);
        ArrayList<Note> notes = new ArrayList<>();
        notes = getNotes();
        notesAdapter = new NotesAdapter(getActivity(), notes);
        notesAdapter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), NotesActivity.class);
                        intent.putExtra("id", getNotesForSearch().get(rvDdata.getChildAdapterPosition(view)).getId());
                        startActivity(intent);
                    }
                }
        );
        rvDdata.setAdapter(notesAdapter);
        return v;
    }

    /**
     * Obtiene la lista de notas.
     * @return Lista de notas.
     */
    private ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        DaoNotes daoReminders = new DaoNotes(getActivity().getApplicationContext());
        notes = daoReminders.getAllNotes();
        return notes;
    }

    private ArrayList<Note> getNotesForSearch() {
        ArrayList<Note> notes = new ArrayList<>();
        notes = NotesAdapter.getListNotes();
        return notes;
    }
}