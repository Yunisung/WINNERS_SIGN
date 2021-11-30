package contract.hee.bukook.mbinfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Member;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.MemberDAO;
import contract.hee.bukook.find.FindPwActivity;

public class MyInfoPwCkActivity extends AppCompatActivity {
    private TextView id;
    private EditText pw;
    private MemberDAO md = MemberDAO.getInstance();
    private InputMethodManager manager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_pwck);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        id =findViewById(R.id.mb_id);
        if(!SessionMb.mb_id.equals(null))
            id.setText(SessionMb.mb_id);
        pw = findViewById(R.id.pw);
    }
    //back
    public void  back(View v){
        onBackPressed();
    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/ed173f9a2d6a4ca8b22ebadc13a023aa";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void goFindPwFrm(View v){
        Intent intent = new Intent(getApplicationContext(), FindPwActivity.class);
        startActivity(intent);
        finish();
    }
    //확인 btn
    public void goUpdateInfo(View v){
        String mb_id = id.getText().toString().trim();
        String mb_pw = pw.getText().toString().trim();
        if(mb_pw.length()==0){
            Toast.makeText(this, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
            pw.requestFocus();
            manager.showSoftInput(pw, InputMethodManager.SHOW_IMPLICIT);
        }else{
            Member mb =new Member();
            mb.setMb_id(mb_id);
            mb.setMb_pw(mb_pw);
            md.selectPw(mb,callbackSelectPw);
        }
    }
    Callback callbackSelectPw = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("TAG", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                  if(body.trim().equals("true")){
                      Intent intent =new Intent(getApplication(),UpdateInfoActivity.class);
                      startActivity(intent);
                      finish();
                  }else{
                      Toast.makeText(MyInfoPwCkActivity.this, "패스워드가 일치하지 않습니다. 다시시도해 주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


}