package contract.hee.bukook.contract;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;

import contract.hee.bukook.CommonFile;
import contract.hee.bukook.R;

public class SignPopupActivity extends Activity {
    private LinearLayout drowsign;
    private SignDrow drowView;
    private CommonFile commonFile= new CommonFile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_popup);
        drowsign = findViewById(R.id.drowsign);
        //서명그리는 클래스->레이아웃에 추가
        drowView = new SignDrow(SignPopupActivity.this);
        drowsign.addView(drowView);
    }
    public void Clear(View v) {
        drowView.GetPath().reset();
        drowView.invalidate();
    }

    //확인btn->서명사진저장
    public void ok(View v){
        drowsign.buildDrawingCache();
        drowsign.setDrawingCacheEnabled(true);

        Bitmap sign = drowsign.getDrawingCache();

        File storage = getCacheDir();   //내부저장소 캐시 경로를 받아옵니다.
        Log.d("TAG", "storage: "+storage);

        String fileName = commonFile.makeFilename();

        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);
               // 1. 내부저장소(캐시)에 먼저 저장한다.
            sign = commonFile.makeTempFile(tempFile,sign);
               // 2. Activity 간 bitmap 전달하기
              //   Bitmap -> ByteArray 로 전환 후 Intent의 Extra로 넣어서 전달.
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            sign.compress(Bitmap.CompressFormat.PNG, 100, stream); // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            byte[] bytes = stream.toByteArray();

            Intent intent = new Intent();
            intent.putExtra("sign", bytes);
            intent.putExtra("fileName", fileName);
            setResult(RESULT_OK, intent);
            finish();
        }
    @Override
    public void onBackPressed() {
        return;
    }
}


