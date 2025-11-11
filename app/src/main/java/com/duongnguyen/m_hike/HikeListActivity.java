package com.duongnguyen.m_hike;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duongnguyen.m_hike.adapters.HikeAdapter;
import com.duongnguyen.m_hike.helpers.DatabaseHelper;
import com.duongnguyen.m_hike.models.Hike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HikeListActivity extends AppCompatActivity implements HikeAdapter.OnHikeActionListener {
    private RecyclerView recyclerView;
    private HikeAdapter hikeAdapter;
    private ArrayList<Hike> hikesList;
    private FloatingActionButton fabAdd;
    private DatabaseHelper dbHelper;
    private TextView tvEmptyState;
    private View emptyStateContainer;
    private boolean isSearching = false;
    private MenuItem deleteAllItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_list);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupUI();
        setupClickListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hike_list_menu, menu);
        deleteAllItem = menu.findItem(R.id.action_delete_all);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isSearching = true;
                searchHikes(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                isSearching = !newText.isEmpty();
                searchHikes(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(() -> {
            isSearching = false;
            loadHikes();
            return false;
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (deleteAllItem != null) {
            deleteAllItem.setEnabled(hikeAdapter.getItemCount() > 0);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHikes();
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.rvHikes);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        emptyStateContainer = findViewById(R.id.emptyStateContainer);
        hikesList = new ArrayList<>();
        hikeAdapter = new HikeAdapter(this, hikesList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(hikeAdapter);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void checkIfListIsEmpty() {
        if (hikesList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateContainer.setVisibility(View.VISIBLE);

            if (isSearching) {
                tvEmptyState.setText("No results found");
            } else {
                tvEmptyState.setText("No Hikes Yet\nAdd your first hike by tapping the + button");
            }
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateContainer.setVisibility(View.GONE);
        }
    }

    private void setupClickListeners() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(HikeListActivity.this, AddEditHikeActivity.class);
            startActivity(intent);
        });
    }

    private void loadHikes() {
        hikesList.clear();
        hikesList.addAll(dbHelper.getAllHikes());
        hikeAdapter.notifyDataSetChanged();
        checkIfListIsEmpty();
        invalidateOptionsMenu();
    }

    private void searchHikes(String query) {
        hikesList.clear();
        hikesList.addAll(dbHelper.searchHikesByName(query));
        hikeAdapter.notifyDataSetChanged();
        checkIfListIsEmpty();
    }

    @Override
    public void onHikeClick(Hike hike) {
        Intent intent = new Intent(this, HikeDetailActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(Hike hike) {
        Intent intent = new Intent(this, AddEditHikeActivity.class);
        intent.putExtra("HIKE_ID_TO_EDIT", hike.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Hike hike) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Hike")
                .setMessage("Are you sure you want to delete '" + hike.getName() + "'? This will also delete all its observations.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteHike(hike.getId());
                    loadHikes(); // Reloads the list
                    Toast.makeText(this, "Hike deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            showDeleteAllConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllConfirmationDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete All Data")
                .setMessage("Are you sure you want to delete ALL hikes and observations? This cannot be undone.")
                .setPositiveButton("Delete All", (dialog, which) -> {
                    dbHelper.deleteAllData();
                    loadHikes();
                    Toast.makeText(this, "All data deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}