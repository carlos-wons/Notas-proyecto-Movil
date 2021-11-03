package com.example.notes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.models.Note;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> implements Filterable {

    private static ArrayList<Note> listNotes;
    ArrayList<Note> listNotesAll;
    private Context context;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    private static int id;

    /**
     * Se integra la función de clic.
     * @param onClickListener Función clic personalizada.
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Constructor vacío, necesario en un adaptador.
     */
    public NotesAdapter() {

    }

    /**
     * Función que se ejecuta para cada elemento de la lista. Establece los datos que una nota lleva.
     * @param holder Holder al que se le agregan los datos (Proporcionado por el sistema).
     * @param position Posición de la lista (Proporcionado por el sistema).
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = listNotes.get(position);
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
    }

    /**
     * Obtiene la cantidad de objetos en la lista.
     * @return Cantidad de objetos en la ista.
     */
    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    /**
     * Infla la interfaz note_selector y le asigna sus propiedades.
     * @param parent (Proporcionado por el sistema).
     * @param viewType (Proporcionado por el sistema).
     * @return El viewHolder con las opciones asignadas.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.note_selector, null);
        v.setOnClickListener(this.onClickListener);
        return new ViewHolder(v);
    }

    /**
     * Construye la clase obteniendo el contexto de la aplicación, y la lista de notas que va a manejar.
     * @param context Contexto del Activiy que lo invoca.
     * @param listNotes Lista de notas que serán agregadas.
     */
    public NotesAdapter(Context context, ArrayList<Note> listNotes) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listNotes = listNotes;
        this.listNotesAll = new ArrayList<>(listNotes);
        this.context = context;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        // Run background_thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Note> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()){
                filteredList.addAll(listNotesAll);
            }else {
                for (Note note: listNotesAll){
                    if(note.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase()) || note.getContent().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(note);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values =filteredList;
            return filterResults;
        }

        // runs on a thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listNotes.clear();
            listNotes.addAll((Collection<? extends Note>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static ArrayList<Note> getListNotes(){
        return listNotes;
    }

    /**
     * Crea un nuevo viewHolder adaptado a las necesidades a mostrar.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final LinearLayout linearLayout;
        public TextView title;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.note_selector_cardview);
            cardView.setId(id++);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.note_selector_linearlayout);
            title = (TextView) itemView.findViewById(R.id.note_selector_title);
            content = (TextView) itemView.findViewById(R.id.note_selector_content);
        }
    }
}
