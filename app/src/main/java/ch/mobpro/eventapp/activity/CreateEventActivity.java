package ch.mobpro.eventapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.base.BaseActivity;
import ch.mobpro.eventapp.databinding.ActivityCreateEventBinding;
import ch.mobpro.eventapp.service.EventCategoryService;
import ch.mobpro.eventapp.viewmodel.CreateEventViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class CreateEventActivity extends BaseActivity<ActivityCreateEventBinding> implements OnMapReadyCallback {

    private String TAG = this.getClass().getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private CreateEventViewModel viewModel;
    private EditText pickStartTime;
    private EditText pickStartDate;
    private EditText pickEndTime;
    private EditText pickEndDate;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private GoogleMap mMap;
    private EventLocationMapsFragmentActivity eventLocationMapsFragmentActivity;

    @Override
    protected int layoutRes() {
        return R.layout.activity_create_event;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateEventViewModel.class);
        dataBinding.setViewModel(viewModel);

        viewModel.getCreationSuccess().observe(this, (onSuccess) -> onCreateSuccess(onSuccess));
        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        pickStartTime = findViewById(R.id.editStartTime);
        pickStartTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(CreateEventActivity.this, (view, hour1, minute1) -> {
                LocalTime time = LocalTime.of(hour1, minute1);
                viewModel.event.setStartTime(time);
                pickStartTime.setText(hour1 + ":" + minute1);
            }, hour, minute, true);
            timePickerDialog.show();
        });

        pickStartDate = findViewById(R.id.editStartDate);
        pickStartDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(CreateEventActivity.this, (view, y, m, d) -> {
                LocalDate date = LocalDate.of(y, m, d);
                viewModel.event.setStartDate(date);
                pickStartDate.setText(d + "." + m + "." + y);
            }, year, month, day);
            datePickerDialog.show();
        });
        pickEndTime = findViewById(R.id.editEndTime);
        pickEndTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(CreateEventActivity.this, (view, hour12, minute12) -> {
                LocalTime time = LocalTime.of(hour12, minute12);
                viewModel.event.setEndTime(time);
                pickEndTime.setText(hour12 + ":" + minute12);
            }, hour, minute, true);
            timePickerDialog.show();
        });
        pickEndDate = findViewById(R.id.editEndDate);
        pickEndDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(CreateEventActivity.this, (DatePickerDialog.OnDateSetListener) (view, y, m, d) -> {
                LocalDate date = LocalDate.of(y, m, d);
                viewModel.event.setEndDate(date);
                pickEndDate.setText(d + "." + m + "." + y);
            }, year, month, day);
            datePickerDialog.show();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void onCreateSuccess(boolean isSuccess) {
        if (!isSuccess) {
            Toast.makeText(this, "Could not create  event", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng rotkreuz = new LatLng(47.1358, 8.4667);
        mMap.addMarker(new MarkerOptions().position(rotkreuz).title("Marker in Rotkreuz"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rotkreuz));
    }
}
