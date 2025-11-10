package com.duongnguyen.m_hike;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duongnguyen.m_hike.adapters.ObservationAdapter;
import com.duongnguyen.m_hike.helpers.DatabaseHelper;
import com.duongnguyen.m_hike.models.Hike;
import com.duongnguyen.m_hike.models.Observation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HikeDetailActivity extends AppCompatActivity implements ObservationAdapter.OnObservationActionListener {
    private long hikeId;
    private DatabaseHelper dbHelper;
    private TextView tvName, tvLocation, tvDate, tvParking, tvLength, tvDifficulty, tvDescription;
    private RecyclerView rvObservations;
    private ObservationAdapter observationAdapter;
    private ArrayList<Observation> observationList;
    private FloatingActionButton fabAddObservation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        hikeId = getIntent().getLongExtra("HIKE_ID", -1);
        if (hikeId == -1) {
            Toast.makeText(this, "Error: Hike ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        findViews();

        loadHikeDetails();
        setupObservationList();
        loadObservations();

        fabAddObservation.setOnClickListener(v -> showAddObservationDialog());
    }

    private void findViews() {
        tvName = findViewById(R.id.tvDetailHikeName);
        tvLocation = findViewById(R.id.tvDetailLocation);
        tvDate = findViewById(R.id.tvDetailDate);
        tvParking = findViewById(R.id.tvDetailParking);
        tvLength = findViewById(R.id.tvDetailLength);
        tvDifficulty = findViewById(R.id.tvDetailDifficulty);
        tvDescription = findViewById(R.id.tvDetailDescription);
        rvObservations = findViewById(R.id.rvObservations);
        fabAddObservation = findViewById(R.id.fabAddObservation);
    }

    private void loadHikeDetails() {
        Hike currentHike = dbHelper.getHikeById(hikeId);
        if (currentHike != null) {
            setTitle(currentHike.getName());
            tvName.setText(currentHike.getName());
            tvLocation.setText(currentHike.getLocation());
            tvDate.setText("Date: " + currentHike.getHikeDate());
            tvParking.setText("Parking: " + currentHike.getParkingAvailable());
            tvLength.setText("Length: " + currentHike.getLength() + " km");
            tvDifficulty.setText("Difficulty: " + currentHike.getDifficulty());

            if (currentHike.getDescription() != null && !currentHike.getDescription().isEmpty()) {
                tvDescription.setText(currentHike.getDescription());
                tvDescription.setVisibility(View.VISIBLE);
            } else {
                tvDescription.setVisibility(View.GONE);
            }
        }
    }

    private void setupObservationList() {
        observationList = new ArrayList<>();
        observationAdapter = new ObservationAdapter(this, observationList, this);
        rvObservations.setLayoutManager(new LinearLayoutManager(this));
        rvObservations.setAdapter(observationAdapter);
    }

    private void loadObservations() {
        observationList.clear();
        observationList.addAll(dbHelper.getAllObservationsForHike(hikeId));
        observationAdapter.notifyDataSetChanged();
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showAddObservationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Observation");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText inputObs = new EditText(this);
        inputObs.setHint("Observation * (e.g., Saw a bird)");
        layout.addView(inputObs);

        final EditText inputComment = new EditText(this);
        inputComment.setHint("Additional Comments (Optional)");
        layout.addView(inputComment);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String obsText = inputObs.getText().toString().trim();
            String commentText = inputComment.getText().toString().trim();

            if (obsText.isEmpty()) {
                Toast.makeText(this, "Observation cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                Observation newObs = new Observation(obsText, getCurrentTime(), commentText, hikeId);
                dbHelper.addObservation(newObs);
                Toast.makeText(this, "Observation added", Toast.LENGTH_SHORT).show();
                loadObservations(); // Refresh the list
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDeleteClick(Observation observation) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Observation")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteObservation(observation.getId());
                    loadObservations();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}