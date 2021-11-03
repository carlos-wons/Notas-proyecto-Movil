package com.example.notes.ui.reminders;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.models.Note;
import com.example.notes.ui.NotesActivity;
import com.example.notes.R;
import com.example.notes.data.DaoReminders;
import com.example.notes.models.Reminders;
import com.example.notes.ui.adapters.NotesAdapter;
import com.example.notes.ui.adapters.RemindersAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectorRemindersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectorRemindersFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RemindersViewModel remindersViewModel;
    private RecyclerView rvDdata;
    private GridLayoutManager layoutManager;
    public static RemindersAdapter remindersAdapter;

    private String mParam1;
    private String mParam2;

    /**
     * Constructor vacío, necesario.
     */
    public SelectorRemindersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectorRemindersFragment.
     */
    public static SelectorRemindersFragment newInstance(String param1, String param2) {
        SelectorRemindersFragment fragment = new SelectorRemindersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * (Proporcionado por el sistema)
     * @param savedInstanceState (Por el sistema)
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
     * Crea el view e infla el selector reminder, además le agrega la función on clic, la cual abre
     * el Notesactivity, mándando el id para poder obtener su información en ese activity.
     * @param inflater (Proporcionado por el sistema).
     * @param container (Proporcionado por el sistema).
     * @param savedInstanceState (Proporcionado por el sistema).
     * @return El view del fragment inflado.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selector_reminders, container, false);
        rvDdata = (RecyclerView) v.findViewById(R.id.fragment_reminder_selector_RecyclerView);
        ArrayList<Reminders> reminders = new ArrayList<>();
        reminders = getReminders();
        remindersAdapter = new RemindersAdapter(getActivity(), reminders);
        remindersAdapter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), NotesActivity.class);
                        intent.putExtra("id", getRemindersForSearch().get(rvDdata.getChildAdapterPosition(view)).getId());
                        startActivity(intent);
                    }
                }
        );
        rvDdata.setAdapter(remindersAdapter);
        return v;
    }

    /**
     * Obtiene la lista de recordatorios de la base de datos.
     * @return Lista de recordatorios.
     */
    private ArrayList<Reminders> getReminders() {
        ArrayList<Reminders> reminders = new ArrayList<>();
        DaoReminders daoReminders = new DaoReminders(getActivity().getApplicationContext());
        reminders = daoReminders.getAllReminders();
        return reminders;
    }

    private ArrayList<Reminders> getRemindersForSearch() {
        ArrayList<Reminders> reminders = new ArrayList<>();
        reminders = RemindersAdapter.getListReminders();
        return reminders;
    }
}