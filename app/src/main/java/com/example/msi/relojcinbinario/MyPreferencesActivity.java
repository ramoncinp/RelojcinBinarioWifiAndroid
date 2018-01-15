package com.example.msi.relojcinbinario;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by MSI on 28/06/2017.
 */

public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
