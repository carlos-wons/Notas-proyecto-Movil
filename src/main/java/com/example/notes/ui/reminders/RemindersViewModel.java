package com.example.notes.ui.reminders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notes.models.Reminders;

import java.util.ArrayList;

public class RemindersViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Reminders>> mAlReminders;

    /**
     * Proporcionado por el sistema.
     */
    public RemindersViewModel() {
        ArrayList<String> fechas = new ArrayList<>();
        ArrayList<Reminders> reminders = new ArrayList<>();
        mAlReminders = new MutableLiveData<>();
        mAlReminders.setValue(reminders);
    }

    /**
     * Proporcionado por el sistema.
     * @return Proporcionado por el sistema.
     */
    public LiveData<ArrayList<Reminders>> getList() {
        return mAlReminders;
    }

}