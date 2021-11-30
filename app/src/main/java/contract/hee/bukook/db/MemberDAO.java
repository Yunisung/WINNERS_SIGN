package contract.hee.bukook.db;

import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import contract.hee.bukook.bean.Member;
import contract.hee.bukook.bean.Admin;

public class MemberDAO {
    private OkHttpClient client;
    private static MemberDAO instance = new MemberDAO();
    public static MemberDAO getInstance() {
        return instance;
    }

    private MemberDAO(){ this.client = new OkHttpClient(); }
    /** 웹 서버로 요청을 한다. */
    public void join_idCK(HashMap<String, String> mb_id, Callback callback) {
        Gson g =new Gson();

        RequestBody body = new FormBody.Builder()
                .add("joinIdCk", g.toJson(mb_id))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=joinIdCk")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);

    }
    public void insertJoin(Member mb, Callback callbackInsertJoin) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("insertJoin", g.toJson(mb))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=insertJoin")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackInsertJoin);
    }
    public void selectPw(Member mb, Callback callbackSelectPw) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("selectPw", g.toJson(mb))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=selectPw")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackSelectPw);
    }
    public void selectMyInfo(HashMap<String, String> mb_id, Callback callbackSelectMyInfo) {
        Gson g =new Gson();

        RequestBody body = new FormBody.Builder()
                .add("selectMyInfo", g.toJson(mb_id))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=selectMyInfo")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackSelectMyInfo);
    }

    public void updateInfo(Member mb, Callback callbackUpdateInfo) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("updateInfo", g.toJson(mb))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=updateInfo")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackUpdateInfo);
    }
    //아이디찾기
    public void selectFindId(HashMap<String, String> map, Callback callbackSelectFindId) {
        Log.d("selectFindId", map.toString());
        Gson g = new Gson();
        RequestBody body = new FormBody.Builder()
                .add("selectFindId", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url + "/app/Appdbconn?page=selectFindId")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackSelectFindId);
    }
     //비밀번호찾기
    public void selectFindPw(Member mb, Callback callbackSelectFindPw) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("selectFindPw", g.toJson(mb))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=selectFindPw")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackSelectFindPw);
    }

    public void updatePw(Member mb, Callback callbackUpdatePw) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("updatePw", g.toJson(mb))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=updatePw")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackUpdatePw);

    }

    public void sendPhone(HashMap<String, String> map, Callback callbackPhone) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("sendPhone", g.toJson(map))
                .build();
        Request request = new Request.Builder()
               // .url("http://192.168.0.34/haha/newfile.php")
                .url(Admin.sic_url+"/app/Appdbconn?page=sendPhone")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackPhone);
    }
}
