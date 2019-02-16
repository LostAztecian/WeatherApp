package ru.stoliarenkoas.weatherapp.browser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import ru.stoliarenkoas.weatherapp.R;

public class BrowserFragment extends Fragment {
    private RequestMaker requestMaker;

    private EditText input;
    private Button confirmButton;
    private WebView mainWindow;


    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);
        input = view.findViewById(R.id.browser_input_text);
        confirmButton = view.findViewById(R.id.browser_button);
        mainWindow = view.findViewById(R.id.browser_web_view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestMaker = new RequestMaker(new RequestMaker.OnResponseCompleted() {
            @Override
            public void onCompleted(String content) {
                Log.d("OkHttp", "On response completed");
                mainWindow.loadData(content, "text/html", "utf-8");
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OkHttp", input.getText().toString());
                requestMaker.run(input.getText().toString());
            }
        });
    }
}
