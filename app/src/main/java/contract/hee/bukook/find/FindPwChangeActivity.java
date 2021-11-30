package contract.hee.bukook.find;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import contract.hee.bukook.HomeActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Member;
import contract.hee.bukook.bean.PatternAdd;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.contract.CancelPopupActivity;
import contract.hee.bukook.db.MemberDAO;
import contract.hee.bukook.mbinfo.MyInfoPwCkActivity;

public class FindPwChangeActivity extends HomeActivity {
    private EditText updatePw;
    private EditText repw;
    private Member mb = new Member();
    private MemberDAO md = MemberDAO.getInstance();
    InputMethodManager manager ;

    @Override
    public void didTapFindPwHome(View v) {
        super.didTapFindPwHome(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_change);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        updatePw = findViewById(R.id.updatePw);
        repw = findViewById(R.id.repw);
    }
    public void back(View v){
        onBackPressed();
    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/76c85f78d7ad4782911e03fd02b6a272";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(SessionMb.mb_idnum!=null){
            Toast.makeText(this, "비밀번호 변경이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplication(), MyInfoPwCkActivity.class);
            startActivity(intent);
            finish();
        }else {
            Log.d("TAG", "FindPwChangeActivity--CancelPopupActivity: ");
            Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
            intent.putExtra("where","main");
            intent.putExtra("main","비밀번호 변경");
            intent.putExtra("text","비밀번호 변경이 취소됩니다.");
            startActivity(intent);
        }
    }
    public void goFinishPwFrm(View v){
        if(updatePw.getText().toString().trim().length()==0 || repw.getText().toString().trim().length()==0){
            Toast.makeText(this, "변경하실 비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
            if(updatePw.getText().toString().trim().length()==0){
                updatePw.requestFocus();
                manager.showSoftInput(updatePw, InputMethodManager.SHOW_IMPLICIT);
            }else if(repw.getText().toString().trim().length()==0){
                repw.requestFocus();
                manager.showSoftInput(repw, InputMethodManager.SHOW_IMPLICIT);
            }
            return;
        }else {
            if(!PatternAdd.validatePw(updatePw.getText().toString().trim())){
                Toast.makeText(this, "비밀번호는 4~12자 영문과 숫자를 사용하세요.", Toast.LENGTH_SHORT).show();
                updatePw.requestFocus();
                manager.showSoftInput(updatePw, InputMethodManager.SHOW_IMPLICIT);
                return;
            }else if (!updatePw.getText().toString().equals(repw.getText().toString())) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                repw.requestFocus();
                manager.showSoftInput(repw, InputMethodManager.SHOW_IMPLICIT);
                return;
            } else {
                mb.setMb_id(getIntent().getStringExtra("mb_id"));
                mb.setMb_phone(getIntent().getStringExtra("mb_phone"));
                mb.setMb_pw(updatePw.getText().toString());
                md.updatePw(mb, callbackUpdatePw);
            }
        }
    }
    Callback callbackUpdatePw = new Callback() {
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
                    if("Y".equals(body.trim())){
                        Toast.makeText(FindPwChangeActivity.this, "이전 비밀번호와 같습니다. 새로운 비밀번호로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        updatePw.setText("");
                        repw.setText("");
                        updatePw.requestFocus();
                        manager.showSoftInput(updatePw, InputMethodManager.SHOW_IMPLICIT);
                    }else if ("true".equals(body.trim())){
                        if(SessionMb.mb_idnum!=null){
                            Toast.makeText(FindPwChangeActivity.this, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), MyInfoPwCkActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), FindPwFinishPopup.class);
                            startActivity(intent);

                        }
                    }else{
                        Toast.makeText(FindPwChangeActivity.this, "비밀번호 변경에 실패 하셨습니다.", Toast.LENGTH_SHORT).show();
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
    public void clickView(View v) {
        switch (v.getId()) {
            case R.id.updatePwLy:
                updatePw.requestFocus();
                manager.showSoftInput(updatePw, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.repwLy:
                repw.requestFocus();
                manager.showSoftInput(repw, InputMethodManager.SHOW_IMPLICIT);
                break;
        }
    }

}