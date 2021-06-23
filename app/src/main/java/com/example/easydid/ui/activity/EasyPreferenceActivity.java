package com.example.easydid.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.easydid.R;
import com.example.easydid.ui.activity.AppCompatPreferenceActivity;
import com.example.easydid.ui.fragment.EasyPreferenceFragment;

public class EasyPreferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easypreference);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_case, new EasyPreferenceFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(1);
        finish();
    }
}


