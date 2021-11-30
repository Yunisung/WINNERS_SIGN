package contract.hee.bukook;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import contract.hee.bukook.bean.Admin;

public class DaumWebViewActivity extends Activity {
    private WebView webView;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_daum_web_view);

        // WebView 초기화
        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }
    public void init_webView() {
        // WebView 설정
        webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        // JavaScript 허용
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUserAgentString("User-Agent:Android");
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // webview url load

        webView.loadUrl(Admin.sic_url + "/_files/address_aos.html");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                    Log.d("TAG", "address1 : " + arg1);//우편번호
                    Log.d("TAG", "address2 : " + arg2);//주소
                    Log.d("TAG", "address_building : " + arg3);//건물(있 or 없)
                    Intent intent = new Intent();
                    Bundle bundle= new Bundle();
                    bundle.putString("zipcode",arg1);
                    bundle.putString("addr1",arg2+" "+arg3);
                    intent.putExtra("addr",bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }


}