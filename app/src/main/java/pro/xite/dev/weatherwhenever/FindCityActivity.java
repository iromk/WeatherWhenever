package pro.xite.dev.weatherwhenever;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;

import pro.xite.dev.weatherwhenever.data.ods.OdsCityProvider;
import pro.xite.dev.weatherwhenever.manage.DataProviderListener;

public class FindCityActivity extends AppCompatActivity
        implements DataProviderListener {

    private static final String TAG = "FindCityActivity/TRACER";
    static final int REQUEST_CODE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_city);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText editTextCityname = findViewById(R.id.edittext_cityname);
        editTextCityname.setVisibility(View.VISIBLE);

        editTextCityname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                suggestCities(editTextCityname.getText());
                Log.d(TAG, String.format("onTextChanged: %s", editTextCityname.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void suggestCities(Editable text) {
        OdsCityProvider.request(text.toString(), this);

    }

    @Override
    public void onSerializedDataReceived(Serializable object) {
        Log.d(TAG, "onSerializedDataReceived: .");
    }
}
