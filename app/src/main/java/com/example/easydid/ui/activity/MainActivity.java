package com.example.easydid.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.easydid.R;
import com.example.easydid.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.btnStart.setOnClickListener(view -> {
            Intent intent = new Intent(this, EasyDidActivity.class);
            startActivity(intent);
        });

        binding.btnSetting.setOnClickListener(view -> {
            Intent intent = new Intent(this, EasyPreferenceActivity.class);
            startActivity(intent);
        });

    }
}