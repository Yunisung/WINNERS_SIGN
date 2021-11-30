package contract.hee.bukook.db;

import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import contract.hee.bukook.bean.Admin;

public class ContractDB {
    private OkHttpClient client;
    private static ContractDB instance = new ContractDB();
    public static ContractDB getInstance() {
        return instance;
    }

    private ContractDB(){ this.client = new OkHttpClient(); }
    /** 웹 서버로 요청을 한다. */
    public void selectName(HashMap<String, String> mb_idnum, Callback callbacselectName) {
        Gson g = new Gson();
        RequestBody body = new FormBody.Builder()
                .add("selectName", g.toJson(mb_idnum))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=selectName")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbacselectName);
    }
    public void selectMyContract(HashMap<String, String> map, Callback callbackSelectMyContract) {
        Gson g = new Gson();
        RequestBody body = new FormBody.Builder()
                .add("selectMyContract", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=selectMyContract")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackSelectMyContract);
    }
    public void selectMyLicensee(HashMap<String, String> map, Callback callbackSelectMyLicensee) {
        Gson g = new Gson();
        RequestBody body = new FormBody.Builder()
                .add("selectMyLicensee", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=selectMyLicensee")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackSelectMyLicensee);
    }
    public void selectMyIndividual(HashMap<String, String> map, Callback callbackSelectMyIndividual) {
        Gson g = new Gson();
        RequestBody body = new FormBody.Builder()
                .add("selectMyIndividual", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=selectMyIndividual")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackSelectMyIndividual);
    }
    public void updateLicensee(HashMap<String, String> map, Callback callbackUpdateLicensee) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("updateLicensee", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=updateLicensee")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackUpdateLicensee);
    }
    public void insertLicensee(HashMap<String, String> map, Callback callbackInsertLicensee) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("insertLicensee", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=insertLicensee")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackInsertLicensee);

    }

    public void insertIndividual(HashMap<String, String> map, Callback callbackInsertIndividual) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("insertIndividual", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=insertIndividual")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackInsertIndividual);

    }

    public void updateIndividual(HashMap<String, String> map, Callback callbackUpdateIndividual) {
        Gson g =new Gson();
        RequestBody body = new FormBody.Builder()
                .add("updateIndividual", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=updateIndividual")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackUpdateIndividual);

    }

    public void countYLicensee(HashMap<String, String> map, Callback callbackcountYLicensee) {
        Gson g = new Gson();
        RequestBody body = new FormBody.Builder()
                .add("countYLicensee", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=countYLicensee")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackcountYLicensee);
    }
    public void countYIndividual(HashMap<String, String> map, Callback callbackcountYIndividual) {
        Gson g = new Gson();
        RequestBody body = new FormBody.Builder()
                .add("countYIndividual", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=countYIndividual")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackcountYIndividual);
    }
    public void selectContract_li(HashMap<String, String> map, Callback callbackselectContract_li) {
        Gson g = new Gson();
        RequestBody body = new FormBody.Builder()
                .add("selectContract_li", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=selectContract_li")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackselectContract_li);
    }
    public void selectContract_in(HashMap<String, String> map, Callback callbackselectContract_in) {
        Gson g = new Gson();
        RequestBody body = new FormBody.Builder()
                .add("selectContract_in", g.toJson(map))
                .build();
        Request request = new Request.Builder()
                .url(Admin.sic_url+"/app/Appdbconn?page=selectContract_in")
                .post(body)
                .build();

        client.newCall(request).enqueue(callbackselectContract_in);
    }
}
