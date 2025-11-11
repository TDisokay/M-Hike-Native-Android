package com.duongnguyen.m_hike;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.duongnguyen.m_hike.helpers.DatabaseHelper;
import com.duongnguyen.m_hike.helpers.ValidationHelper;
import com.duongnguyen.m_hike.models.Hike;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddEditHikeActivity extends AppCompatActivity {
    private TextView tvHeader;
    private TextInputEditText etHikeName, etLocation, etDate, etLength, etDescription;
    private Spinner spinnerParking, spinnerDifficulty;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private Hike existingHike = null;
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_hike);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        findViews();
        setupSpinners();
        setupDatePicker();

        if (getIntent().hasExtra("HIKE_TO_EDIT")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                existingHike = getIntent().getSerializableExtra("HIKE_TO_EDIT", Hike.class);
            } else {
                existingHike = (Hike) getIntent().getSerializableExtra("HIKE_TO_EDIT");
            }

            tvHeader.setText("Edit Hike");
            btnSave.setText("Update Hike");
            if (existingHike != null) {
                populateFields(existingHike);
            }
        } else {
            tvHeader.setText("Add New Hike");
            btnSave.setText("Save Hike");
        }

        btnSave.setOnClickListener(v -> saveHike());
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void findViews() {
        tvHeader = findViewById(R.id.tvHeader);
        etHikeName = findViewById(R.id.etHikeName);
        etLocation = findViewById(R.id.etLocation);
        etDate = findViewById(R.id.etDate);
        etLength = findViewById(R.id.etLength);
        etDescription = findViewById(R.id.etDescription);
        spinnerParking = findViewById(R.id.spinnerParking);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> parkingAdapter = ArrayAdapter.createFromResource(this,
                R.array.parking_options, android.R.layout.simple_spinner_item);
        parkingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerParking.setAdapter(parkingAdapter);

        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_options, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateDateLabel();
        };

        etDate.setOnClickListener(v -> new DatePickerDialog(AddEditHikeActivity.this, dateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void populateFields(Hike hike) {
        etHikeName.setText(hike.getName());
        etLocation.setText(hike.getLocation());
        etDate.setText(hike.getHikeDate());
        etLength.setText(String.valueOf(hike.getLength()));
        etDescription.setText(hike.getDescription());

        spinnerParking.setSelection(((ArrayAdapter<String>) spinnerParking.getAdapter()).getPosition(hike.getParkingAvailable()));
        spinnerDifficulty.setSelection(((ArrayAdapter<String>) spinnerDifficulty.getAdapter()).getPosition(hike.getDifficulty()));
    }

    private void saveHike() {
        String name = Objects.requireNonNull(etHikeName.getText()).toString().trim();
        String location = Objects.requireNonNull(etLocation.getText()).toString().trim();
        String date = Objects.requireNonNull(etDate.getText()).toString().trim();
        String lengthStr = Objects.requireNonNull(etLength.getText()).toString().trim();
        String parking = spinnerParking.getSelectedItem().toString();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        String description = Objects.requireNonNull(etDescription.getText()).toString().trim();

        if (!ValidationHelper.validateHikeInput(this, name, location, date, lengthStr)) {
            return;
        }

        double length = Double.parseDouble(lengthStr);
        Hike hike = new Hike(name, location, date, parking, length, difficulty, description);

        if (existingHike != null) {
            hike.setId(existingHike.getId());
            dbHelper.updateHike(hike);
            Toast.makeText(this, "Hike updated successfully", Toast.LENGTH_SHORT).show();

        } else {
            dbHelper.addHike(hike);
            Toast.makeText(this, "Hike added successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}