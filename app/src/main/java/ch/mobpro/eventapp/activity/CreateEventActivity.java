package ch.mobpro.eventapp.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.*;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.base.BaseActivity;
import ch.mobpro.eventapp.databinding.ActivityCreateEventBinding;
import ch.mobpro.eventapp.viewmodel.CreateEventViewModel;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class CreateEventActivity extends BaseActivity<ActivityCreateEventBinding> {

    private String TAG = this.getClass().getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private CreateEventViewModel viewModel;
    private EditText pickStartTime;
    private EditText pickStartDate;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

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
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        pickStartTime = findViewById(R.id.editStartTime);
        pickStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        LocalTime time = LocalTime.of(hour, minute);
                        viewModel.event.setStartTime(time);
                        pickStartTime.setHint(hour + ":" + minute);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        pickStartDate = findViewById(R.id.editStartDate);
        pickStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(CreateEventActivity.this, (DatePickerDialog.OnDateSetListener) (view, y, m, d) -> {
                    LocalDate date = LocalDate.of(y, m, d);
                    viewModel.event.setStartDate(date);
                    pickStartDate.setHint(d + "." + m + "." + y);
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        setSupportActionBar(toolbar);
    }

    private void onCreateSuccess(boolean isSuccess) {
        if (!isSuccess) {
            Toast.makeText(this, "Could not create  event", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);
        }
    }
}
