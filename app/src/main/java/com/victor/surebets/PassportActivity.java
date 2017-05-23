package com.victor.surebets;

import android.app.ProgressDialog;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PassportActivity extends Activity {
    private static final String TAG = "SUREBET";
    ImageView imgPoster;
    TextView tvNames, tvIdNum;
    ProgressBar bar;
    String id="";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Searching. Please Wait");
        progressDialog.setCancelable(false);

        imgPoster = (ImageView) findViewById(R.id.imgPassport);
        tvNames = (TextView) findViewById(R.id.tvNames);
        tvIdNum = (TextView) findViewById(R.id.tvIdNum);
        bar = (ProgressBar) findViewById(R.id.progressBarPassport);
        id= getIntent().getStringExtra("ID");
        search_details();
    }

    private void search_details() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("id",id);
        String url =Constants.URL_BASE+"search.php";
        progressDialog.show();
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("SUREBET","Failed to load correctly");
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("SUREBET","Success "+responseString);
                progressDialog.dismiss();
                //Toast.makeText(PassportActivity.this, "Success", Toast.LENGTH_SHORT).show();
                // {"id":"39","first":"Claire","second":"Mwangi ","identity":"1234567","birth":"08.09.1996","picture":"uploads\/3407901_3831733.jpg"}
                try {
                    JSONObject obj =new JSONObject(responseString);
                    String names = obj.getString("first") + " "+obj.getString("second");
                    String photo_url = Constants.URL_BASE+obj.getString("picture");
                    Picasso.with(getApplicationContext()).load(photo_url).into(imgPoster);
                    tvNames.setText(names);
                    tvIdNum.setText(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

		// the 2 parameters are posted to this PHP file which is in wamp