package ru.stoliarenkoas.weatherapp.browser;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestMaker {
    private OnResponseCompleted listener;

    public RequestMaker(OnResponseCompleted listener) {
        this.listener = listener;
    }

    public void run(String url) {
        Log.d("OkHttp", "Request maker, string: " + url);

        final OkHttpClient client = new OkHttpClient(); //crashes here
        Log.d("OkHttp", "Passed client creation.");

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            Handler handler = new Handler();

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OkHttp", "OnFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String answer = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onCompleted(answer);
                    }
                });
            }
        });
    }

    public interface OnResponseCompleted {
        void onCompleted(String content);
    }

}
