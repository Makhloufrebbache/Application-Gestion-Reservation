package com.example.laboratoire3;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
public class DateFormat extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private EditText textDate;

    public void setEditTextDate(EditText textDate) {
        this.textDate = textDate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendrier = Calendar.getInstance();
        int annee = calendrier.get(Calendar.YEAR);
        int mois = calendrier.get(Calendar.MONTH);
        int jours = calendrier.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, annee, mois, jours);
    }

    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
        String SelectedDate = jour + "/" + (mois + 1) + "/" + annee;
        textDate.setText(SelectedDate);
    }
}



