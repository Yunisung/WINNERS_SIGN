package contract.hee.bukook;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class FilesTask extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            Log.d("TAG", "strings.length: "+strings.length);
            try {
                for(int i=0; i<strings.length; i++ ){
                    String img_url = strings[i]; //url of the image
                    Log.d("TAG", "doInBackground() img_url[0]: " + img_url);
                    URL url = new URL(img_url);
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onPostExecute(Bitmap result) {
          //result 리턴
        }

    }