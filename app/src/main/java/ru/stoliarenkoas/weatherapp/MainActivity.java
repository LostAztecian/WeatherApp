package ru.stoliarenkoas.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {
    private String cityName = "Manhattan";
    private String currentWeather = "raining 17C";

    private boolean showHumidity;
    private boolean showPressure;
    private boolean showTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) showWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void confirmSelection(View view) {
        getParameters();
        updateWeather();

        if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, CityWeatherActivity.class);
            intent.putExtra("cityName", cityName);
            intent.putExtra("currentWeather", currentWeather);
            startActivity(intent);
        } else showWeather();

    }

    private void showWeather() {
        ((TextView)findViewById(R.id.textView_cityName)).setText(cityName);
        ((TextView)findViewById(R.id.textView_cityWeather)).setText(currentWeather);
    }

    private void updateWeather() {
        StringBuilder sb = new StringBuilder("Storming clouds");
        if (showTemperature) sb.append(", 271K");
        if (showHumidity) sb.append(", 31%");
        if (showPressure) sb.append(", 760mm");
        sb.append(".");
        currentWeather = sb.toString();
    }

    private void getParameters() {
        cityName = ((TextView)findViewById(R.id.editText_selectCity)).getText().toString();

        showHumidity = ((Switch)findViewById(R.id.switch_showHumidity)).isChecked();
        showPressure = ((Switch)findViewById(R.id.switch_showPressure)).isChecked();
        showTemperature = ((Switch)findViewById(R.id.switch_showTemperature)).isChecked();
    }
}
