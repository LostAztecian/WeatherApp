package stoliarenkoas.ru.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String cityName = "Manhattan";
    private String currentWeather = "Cloudy, 17C";

    private boolean showTemperature;
    private boolean showPressure;
    private boolean showHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
    }

    private void loadData() {
        final Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("cityName")) {
            cityName = extras.getString("cityName");
            showTemperature = extras.getBoolean("showTemperature");
            showPressure = extras.getBoolean("showPressure");
            showHumidity = extras.getBoolean("showHumidity");
        }
        if (cityName.isEmpty()) cityName = "Manhattan";

        getWeather();

        final TextView cityNameView = findViewById(R.id.textView_cityName);
        final TextView cityWeatherView = findViewById(R.id.textView_cityWeather);
        cityNameView.setText(cityName);
        cityWeatherView.setText(currentWeather);
    }

    private void getWeather() {
        if (cityName == "Manhattan") return;
        StringBuffer sb = new StringBuffer("Storming clouds");
        if (showTemperature) sb.append(", 276K");
        if (showPressure) sb.append(", 749mm");
        if (showHumidity) sb.append(", 61%");
        sb.append(".");
        currentWeather = sb.toString();
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

}
