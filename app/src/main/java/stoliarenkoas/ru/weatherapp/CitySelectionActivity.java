package stoliarenkoas.ru.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class CitySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);
    }

    public void confirmSelection(View view) {
        final int viewId = view.getId();
        switch (viewId) {
            case R.id.button_confirmSelection: {
                final String cityName = ((EditText)findViewById(R.id.editText_selectCity)).getText().toString();
                final boolean showTemperature = ((Switch)findViewById(R.id.switch_showTemperature)).isChecked();
                final boolean showPressure = ((Switch)findViewById(R.id.switch_showPressure)).isChecked();
                final boolean showHumidity = ((Switch)findViewById(R.id.switch_showHumidity)).isChecked();
                final Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("cityName", cityName);
                intent.putExtra("showTemperature", showTemperature);
                intent.putExtra("showPressure", showPressure);
                intent.putExtra("showHumidity", showHumidity);
                startActivity(intent);
                break;
            }
        }
    }
}
