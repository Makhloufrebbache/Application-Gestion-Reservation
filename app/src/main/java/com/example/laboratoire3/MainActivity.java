package com.example.laboratoire3;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private double totalPrice = 0.0;
    private DbSqlLite dbsqllite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleMenuClick(item);
                return true;
            }
        });
    }

    public void showDatePickerDialog(View view) {
        EditText editTextDate = (EditText) view;
        DateFormat newFragment = new DateFormat();
        newFragment.setEditTextDate(editTextDate);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reservation_menu, menu);
        return true;
    }

    private void handleMenuClick(MenuItem item) {
        // Show different messages based on the selected option
        if (item.getItemId() == R.id.chambre_id) {
            showBedDialog();
        } else if (item.getItemId() == R.id.massage_id) {
            showOptionsDialog("Select the type of Massage", new String[]{"Accept Additional ($)", "No"}, 40, 52);
        } else if (item.getItemId() == R.id.sauna_id) {
            showOptionsDialog("Select the type of Sauna", new String[]{"Accept Additional ($)", "No"}, 25, 35);
        } else if (item.getItemId() == R.id.buffet_id) {
            showBuffetOptionsDialog();
        } else if (item.getItemId() == R.id.soins_de_beaute) {
            showBeautyServicesDialog();
        } else if (item.getItemId() == R.id.facture_id) {
            showInvoice();
        } else if (item.getItemId() == R.id.quitter_id) {
            showMessage("Quitter selected");
        }
    }

    private void showBedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the type of bed");

        final String[] bedOptions = {"Single Bed", "Queen Bed", "King Bed"};
        final int[] selectedOption = {0}; // Default: Single Bed

        builder.setSingleChoiceItems(bedOptions, 0, (dialog, which) -> selectedOption[0] = which);

        builder.setPositiveButton("Accept", (dialog, which) -> {
            String selectedBedType = bedOptions[selectedOption[0]];
            updateTotalPrice(78, 80);
            showMessage("You selected: " + selectedBedType);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showOptionsDialog(String title, String[] options, double weekdayPrice, double weekendPrice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        final int[] selectedOption = {0}; // Default: Accept

        builder.setSingleChoiceItems(options, 0, (dialog, which) -> selectedOption[0] = which);

        builder.setPositiveButton("Accept", (dialog, which) -> {
            String selectedOptionText = options[selectedOption[0]];
            if (selectedOptionText.equals("Additional ($)")) {
                updateTotalPrice(0, 12); // Additional cost
            } else if (selectedOptionText.equals("No")) {
                updateTotalPrice(0, 0); // No additional cost
            }
            showMessage("You selected: " + selectedOptionText);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showBuffetOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select access to the buffet");

        final String[] buffetOptions = {"Weekday", "Weekend"};
        final int[] selectedOption = {0}; // Default: Weekday

        builder.setSingleChoiceItems(buffetOptions, 0, (dialog, which) -> selectedOption[0] = which);

        builder.setPositiveButton("Accept", (dialog, which) -> {
            String selectedOptionText = buffetOptions[selectedOption[0]];
            if (selectedOptionText.equals("Weekday")) {
                updateTotalPrice(32, 0); // Buffet price on weekdays
            } else if (selectedOptionText.equals("Weekend")) {
                updateTotalPrice(39, 0); // Buffet price on weekends
            }
            showMessage("You selected: Access to the Kings' buffet - " + selectedOptionText);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showBeautyServicesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sélécctionner le service beauté");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final CheckBox checkBoxManicure = new CheckBox(this);
        checkBoxManicure.setText("Manicure");
        layout.addView(checkBoxManicure);

        final CheckBox checkBoxFacial = new CheckBox(this);
        checkBoxFacial.setText("Facial");
        layout.addView(checkBoxFacial);

        final CheckBox checkBoxHaircut = new CheckBox(this);
        checkBoxHaircut.setText("Haircut");
        layout.addView(checkBoxHaircut);

        builder.setView(layout);

        builder.setPositiveButton("Accépter", (dialog, which) -> {
            if (checkBoxManicure.isChecked()) {
                updateTotalPrice(22, 30); // Manicure price
            }
            if (checkBoxFacial.isChecked()) {
                updateTotalPrice(25, 35); // Facial price
            }
            if (checkBoxHaircut.isChecked()) {
                updateTotalPrice(32, 40); // Haircut price
            }

            showMessage("Vous avez séléctionner le service beauté");
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showInvoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invoice");

        builder.setMessage("Le cout total est: $ " + totalPrice);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateTotalPrice(double weekdayPrice, double weekendPrice) {
        // Update the total price based on the selected options
        totalPrice += (weekdayPrice + weekendPrice);
    }

    // Method to show a Toast indicating that the reservation was successful
    private void showSuccessfulReservation() {
        Toast.makeText(this, "Reservation successful!", Toast.LENGTH_SHORT).show();
    }

    // Method to handle the click on the reserve button
    public void onClickReserve(View view) {
        showSuccessfulReservation();
        saveReservationToDatabase();

        // Reset UI components
        resetFields();
    }

    // Method to reset UI components
    private void resetFields() {
        // Clear the date field
        EditText editTextDate = findViewById(R.id.editTextFechaSalida);
        EditText editTextDate2 = findViewById(R.id.editTextFechaLlegada);
        editTextDate.setText("");
        editTextDate2.setText("");

        // Reset the total price
        totalPrice = 0.0;
    }

    // Method to save the reservation to the database
    private void saveReservationToDatabase() {
        SQLiteDatabase db = dbsqllite.getWritableDatabase();

        // Create a new reservation record
        ContentValues values = new ContentValues();
        // You can add more columns as needed
        values.put(DbSqlLite.COLUMN_ROOM_TYPE, "Séléction le type de chambre");
        values.put(DbSqlLite.COLUMN_PRICE, totalPrice);

        // Insert the new record into the database
        long newRowId = db.insert(DbSqlLite.TABLE_RESERVATIONS, null, values);

        // Check if the insertion was successful
        if (newRowId != -1) {
            showMessage("Réservation enregistrée avec succés");
        } else {
            showMessage("Erreur de sauvegarde");
        }

        // Close the database connection
        db.close();
    }

    @Override
    protected void onDestroy() {
        // Close the dbHelper when the activity is destroyed
        dbsqllite.close();
        super.onDestroy();
    }
}
