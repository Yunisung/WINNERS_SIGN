package contract.hee.bukook.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import contract.hee.bukook.MainActivity;
import contract.hee.bukook.R;

public class FindIdFinishActivity extends AppCompatActivity {
    private TextView id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_finish);
        id=findViewById(R.id.id);
        id.setText(getIntent().getStringExtra("mb_id"));
    }

//    public void goLoginFrm(View v){
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//    public  void findPwFrm(View v){
//        Intent intent = new Intent(getApplication(),FindPwActivity.class);
//        startActivity(intent);
//        finish();
//    }

    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent =new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        finish();
    }



}