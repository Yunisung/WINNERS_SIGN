package contract.hee.bukook;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import contract.hee.bukook.bean.SessionMb;

public class ImgUploadTask extends AsyncTask<String, Integer, Boolean> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            JSONObject jsonObject = JSONParser.uploadImage(params[0],params[1],params[2],params[3]);
            Log.d("TAG", "doInBackground() jsonObject: " + jsonObject);
            if (jsonObject != null)
                return jsonObject.getString("result").equals("success");

        } catch (JSONException e) {
            Log.i("TAG", "Error : " + e.getLocalizedMessage());
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean){
            Log.i("TAG", "onPostExecute Individual file upload ok");
        }  else{
            return;
        }

    }
}

 class JSONParser {

    public static JSONObject uploadImage(String imageUploadUrl, String sourceImageFile, String fi_group_val, String seq_val) {
        Log.d("TAG", "uploadImage() called with: imageUploadUrl = [" + imageUploadUrl + "], sourceImageFile = [" + sourceImageFile + "], fi_group = [" + fi_group_val + "]");
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

        try {
            File sourceFile = new File(sourceImageFile);
            Log.d("TAG", "sourceFile exists: " + sourceFile.exists());

            String filename = sourceImageFile.substring(sourceImageFile.lastIndexOf("/")+1);
            Log.d("TAG", "sourceImageFile.substring filename: "+filename);

            // OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart("idnum", SessionMb.mb_idnum)
                    .addFormDataPart("seq", seq_val)
                    .addFormDataPart("fi_group", fi_group_val)
                    .build();

            Request request = new Request.Builder()
                    .url(imageUploadUrl)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if (response != null) {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Log.e("TAG", "Success : " + res);
                    return new JSONObject(res);
                }
            }
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("TAG", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }
}
