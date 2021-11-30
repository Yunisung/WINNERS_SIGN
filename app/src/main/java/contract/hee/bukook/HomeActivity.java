package contract.hee.bukook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import contract.hee.bukook.contract.CancelPopupActivity;

public class HomeActivity extends AppCompatActivity {

    protected void didTapContractHome(View v) {
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("main","계약서 작성취소");
        intent.putExtra("text","작성중인 계약서가 취소됩니다.");
        intent.putExtra("where","login");
        startActivity(intent);
    }

    protected void didTapFindIdHome(View v) {
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("where","main");
        intent.putExtra("main","아이디 찾기");
        intent.putExtra("text","아이디 찾기가 취소됩니다.");
        startActivity(intent);
    }

    protected void didTapFindPwHome(View v) {
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("where","main");
        intent.putExtra("main","비밀번호 변경");
        intent.putExtra("text","비밀번호 변경이 취소됩니다.");
        startActivity(intent);
    }

    protected void didTapJoinHome(View v) {
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("where","main");
        intent.putExtra("main","회원가입");
        intent.putExtra("text","작성중인 회원가입이 취소됩니다.");
        startActivity(intent);
    }

    protected void didTapUpdateInfoHome(View v) {
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("main","정보수정");
        intent.putExtra("text","정보수정이 취소됩니다.");
        intent.putExtra("where","login");
        startActivity(intent);
    }
}