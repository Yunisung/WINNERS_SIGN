package contract.hee.bukook.mbinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import contract.hee.bukook.HomeActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Member;
import contract.hee.bukook.bean.PatternAdd;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.MemberDAO;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class UpdateInfoActivity extends HomeActivity {
    //private TextView cc1;
    //private TextView cc2;
    private TextView name;
    private TextView id;
    private EditText repw;
    private EditText pw;
    private EditText phone;
    private EditText email;
    private Member member = new Member();
    private MemberDAO md = MemberDAO.getInstance();
    private InputMethodManager manager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //cc1 = findViewById(R.id.cc1);
        //cc2 = findViewById(R.id.cc2);
        name = findViewById(R.id.mb_name);
        id = findViewById(R.id.mb_id);
        pw = findViewById(R.id.mb_pw);
        repw = findViewById(R.id.repw);
        phone = findViewById(R.id.mb_phone);
        email = findViewById(R.id.mb_email);
        name.setText(SessionMb.mb_name);
        id.setText(SessionMb.mb_id);

        //부분색 변경
        //colorChange();
        selectMyInfo();
    }

    @Override
    public void didTapUpdateInfoHome(View v) {
        super.didTapUpdateInfoHome(v);
    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/69cb27d106164025bbbf73e8551b836e";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void selectMyInfo() {
        String mb_id = SessionMb.mb_id;
        Log.d("TAG", ".getStringExtra: "+mb_id);
        if (!id.equals(null)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("mb_id", mb_id);
            new Thread() {
                public void run() {
                    // 파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                    md.selectMyInfo(map, callbackSelectMyInfo);
                }
            }.start();
        }
    }

    Callback callbackSelectMyInfo = new Callback() {
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
                    if (!body.trim().equals(null)) {
                        Gson gson = new Gson();
                        member = new Member();
                        member = gson.fromJson(body.trim(), member.getClass());
                        phone.setText(member.getMb_phone());
                        email.setText(member.getMb_email());
                    }
                }
            });
        }
    };

    public void goUpdateFinishFrm(View v) {
        if(phone.getText().toString().trim().length()==0 ){
            Toast.makeText(this, "휴대폰 번호를 비워둘 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!pwCk()){
            return;
        }
        if(!PatternAdd.validatePhone(phone.getText().toString().trim())){
            Toast.makeText(this, "올바른 핸드폰 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            return;
        }
//        if(!PatternAdd.validateEmail(email.getText().toString().trim())){
//            Toast.makeText(this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
        updateInfo();
    }
    //부분 색 변경
    public void colorChange() {
//        List<String> arr = new ArrayList<>();
//        arr.add(cc1.getText().toString());
//        arr.add(cc2.getText().toString());
//
//        for (int i = 0; i < arr.size(); i++) {
//            SpannableStringBuilder st = new SpannableStringBuilder(arr.get(i));
//            int s = arr.get(i).indexOf("(");
//            int e = arr.get(i).length();
//            st.setSpan(new ForegroundColorSpan(Color.parseColor("#EB5B3D")), s, e, SPAN_EXCLUSIVE_EXCLUSIVE);
//            switch (i) {
//                case 0:
//                    cc1.setText(st);
//                    break;
//                case 1:
//                    cc2.setText(st);
//                    break;
//            }
//        }
    }
    //back
    public void back(View v){
        onBackPressed();
    }
    public void home(View v) {
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean pwCk() {
        String mb_pw = pw.getText().toString().trim();
        String mb_repw = repw.getText().toString().trim();

        if (mb_pw.length()==0) {
            return true;
        } else {
            if(!PatternAdd.validatePw(mb_pw)){
                Toast.makeText(this, "비밀번호는 4~12자 영문과 숫자를 사용하세요.", Toast.LENGTH_SHORT).show();
                pw.requestFocus();
                manager.showSoftInput(pw, InputMethodManager.SHOW_IMPLICIT);
                return false;
            }else if(mb_repw.length()==0){
                Toast.makeText(this, "비밀번호 확인란을 입력해 주세요", Toast.LENGTH_SHORT).show();
                repw.requestFocus();
                manager.showSoftInput(repw, InputMethodManager.SHOW_IMPLICIT);
                return false;
            }else if (mb_pw.equals(mb_repw)){
                return true;
            }
        }
        Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
        repw.requestFocus();
        manager.showSoftInput(repw, InputMethodManager.SHOW_IMPLICIT);
        return false;
    }

    private void updateInfo() {
        boolean send = false;
        String sendPw=null;

        if(pw.getText().toString().trim().length()!= 0){
            sendPw=pw.getText().toString();
        }
        Log.d("TAG", "sendPw: "+sendPw);
        if (pw.getText().toString().length() != 0 || !member.getMb_phone().equals(phone.getText().toString())|| !member.getMb_email().equals(email.getText().toString())) {
            send = true;

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat HHmmss = new SimpleDateFormat("HHmmss");
            String mdate = yyyymmdd.format(date);
            String mtime = HHmmss.format(date);

            member.setMb_phone(phone.getText().toString());
            member.setMb_email(email.getText().toString());
            member.setMb_mdate(mdate);
            member.setMb_mtime(mtime);
            member.setMb_pw(sendPw);
        }
        if (send) {
            md.updateInfo(member, callbackUpdateInfo);
        } else {
            Intent intent = new Intent(getApplicationContext(), UpdateInfoFinishPopup.class);
            startActivity(intent);
        }
    }
    Callback callbackUpdateInfo = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    if("Y".equals(body.trim())){
                        Toast.makeText(UpdateInfoActivity.this, "이전 비밀번호와 같습니다. 새로운 비밀번호로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        pw.setText("");
                        repw.setText("");
                        pw.requestFocus();
                        manager.showSoftInput(pw, InputMethodManager.SHOW_IMPLICIT);
                    }else if ("true".equals(body.trim())){
                        Intent intent = new Intent(getApplicationContext(), UpdateInfoFinishPopup.class);
                        startActivity(intent);

                    }else{
                        Toast.makeText(UpdateInfoActivity.this, "정보수정에 실패하였습니다. 다시시도 해주세요", Toast.LENGTH_SHORT).show();
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
            case R.id.pw:
                pw.requestFocus();
                manager.showSoftInput(pw, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.repwLy:
                repw.requestFocus();
                manager.showSoftInput(repw, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.phone:
                phone.requestFocus();
                manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.email:
                email.requestFocus();
                manager.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
                break;
        }
    }
}