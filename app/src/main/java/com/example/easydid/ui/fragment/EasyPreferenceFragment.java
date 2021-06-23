package com.example.easydid.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.kicc.easypos.tablet.filedownload.DownloadActivity;

import com.example.easydid.R;

public class EasyPreferenceFragment extends PreferenceFragmentCompat {

    private EditTextPreference mServerPort;
    private Context mContext;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        mContext = this.getContext();
        addPreferencesFromResource(R.xml.preference_easydid);
        initVideoDownloadPref();

    }

    public void initVideoDownloadPref()
    {
        Preference videoDownloadPref = findPreference("pref_key_easydid_ad_video");
        videoDownloadPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(mContext, DownloadActivity.class);
                intent.putExtra("intent_extra_file_path", "/didFilePath");
                ((Activity)mContext).startActivityForResult(intent, 1);
                return true;
            }
        });
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//
//        mServerPort = getPreferenceManager().findPreference("pref_key_easydid_network_server_port");
//        mServerPort.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                return false;
//            }
//        });
//
//        return inflater.inflate(R.layout.fragment_easypreference, container, false);
//    }
}
