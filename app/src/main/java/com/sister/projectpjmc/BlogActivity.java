package com.sister.projectpjmc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BlogActivity extends AppCompatActivity {
    public static BufferedReader br;
    private int responseCode;
    TextView textView;
    StringBuffer response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        final String clientId = "kmSbxLR9HbX5gZ0g58Ou";//애플리케이션 클라이언트 아이디값";
        final String clientSecret = "4tBeJxcOcS";//애플리케이션 클라이언트 시크릿값";
        textView = findViewById(R.id.post);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int display = 3;
                try {
                    String text = URLEncoder.encode("화곡 타코야끼", "UTF-8");
                    String apiURL = "https://openapi.naver.com/v1/search/blog.json?query="+ text + "&display=" + display + "&start=1"; // json 결과
                    //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text + "&display=" + display + "&start=1"; // xml 결과
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                    responseCode = con.getResponseCode(); //에러 발생

                    Log.e("responseCode", "responseCode ::: " + responseCode);
                    if(responseCode == 200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();

                    //runOnUIThread 기억할 것
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(response.toString());
                        }
                    });

                } catch (Exception e) {
                    textView.setText("error response: "+ responseCode);
                }

            }
        }).start();
    }
}
