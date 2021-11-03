package com.example.notes.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /**
     * Generado por el sistema.
     */
    public NotesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    /**
     * Generado por el sistema.
     * @return Generado por el sistema.
     */
    public LiveData<String> getText() {
        return mText;
    }
}