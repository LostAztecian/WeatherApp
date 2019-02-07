package ru.stoliarenkoas.weatherapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class WeatherIconView extends View {

    private Paint paintOrange;
    private Paint paintYellow;

    private void init() {
        paintOrange = new Paint();
        paintOrange.setColor(Color.parseColor("#FFCC80"));
        paintOrange.setStyle(Paint.Style.FILL);
        paintYellow = new Paint();
        paintYellow.setColor(Color.YELLOW);
        paintYellow.setStyle(Paint.Style.FILL);
    }

    public WeatherIconView(Context context) {
        super(context);
        init();
    }

    public WeatherIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeatherIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 8; i++) {
            canvas.drawArc(50, 130, 150, 230, 250, 30, true, paintOrange);
            canvas.rotate(45, 100, 100);
        }
        canvas.drawCircle(100, 100, 40, paintYellow);
    }
}
