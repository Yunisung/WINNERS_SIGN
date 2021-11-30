package contract.hee.bukook.db;

import com.google.gson.Gson;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import contract.hee.bukook.bean.Member;
import contract.hee.bukook.bean.Admin;

public class LoginDB {
    private OkHttpClient client;
    private static LoginDB instance = new LoginDB();
    public static LoginDB getInstance() {
        return instance;
    }
    private LoginDB(){
        this.client = new OkHttpClient();
    }
    /** 웹 서버로 요청을 한다. */
    public void goLogin(Member mb, Callback callbackGoLogin) {
        Gson g =new Gson();

        RequestBody body = new FormBody.Builder()
                .add("goLogin", g.toJson(mb))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=goLogin")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackGoLogin);

    }
}
