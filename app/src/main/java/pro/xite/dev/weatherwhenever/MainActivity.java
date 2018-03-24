package pro.xite.dev.weatherwhenever;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadSelectionFromPreferences();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.button_get_forecast) {
            Log.d("TRACER", "The button has been clicked");
            saveSelectionInPreferences();
        }
    }

    private void saveSelectionInPreferences() {
        final Context context = this;
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        final Spinner spinner = findViewById(R.id.spinner_city_list);
        final int last_city_index = spinner.getSelectedItemPosition();
        editor.putInt(getString(R.string.selected_city_index_prefs_key), last_city_index);
        editor.apply(); // tip! writes in a background
        //editor.commit(); // tip! writes data immediately
    }

    private void loadSelectionFromPreferences() {
        final Context context = this;
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final int default_city_index = 0; //getResources().getStringArray(R.array.city_list)[0];
        final int last_city_index = sharedPref.getInt(getString(R.string.selected_city_index_prefs_key), default_city_index);
        final Spinner spinner = findViewById(R.id.spinner_city_list);
        spinner.setSelection(last_city_index,true);
    }
}
