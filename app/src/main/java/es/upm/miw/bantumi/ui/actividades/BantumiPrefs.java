package es.upm.miw.bantumi.ui.actividades;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.ui.fragmentos.SettingsFragment;


public class BantumiPrefs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, new SettingsFragment())
                    .commit();
        }
    }
}