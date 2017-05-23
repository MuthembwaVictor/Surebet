
package com.victor.surebets;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static com.victor.surebets.R.id.imgPassport;

public class DepositActivity extends Activity {
    private static final EditText Button = null;
    private static final String TAG ="SUREBET" ;
    EditText kitambulisho;
    Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        kitambulisho = (EditText) findViewById(R.id.kitambulisho);
        verify = (android.widget.Button) findViewById(R.id.verify);
        verify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(!kitambulisho.getText().toString().trim().isEmpty()) {
                    Intent x = new Intent(getApplicationContext(), PassportActivity.class);
                    x.putExtra("ID", kitambulisho.getText().toString().trim());
                    startActivity(x);
                }else{
                    kitambulisho.setError("Invalid ID Number");
                    Toast.makeText(DepositActivity.this, "You must enter a valid ID number", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deposit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == R.id.verify) {
            Intent x = new Intent(getApplicationContext(), VerifiedActivity.class);
            startActivity(x);
        }


        return super.onOptionsItemSelected(item);
    }

    public void verify(View view) {

    }
        // TODO Auto-generated method stub
        private void search (String text){
            AsyncHttpClient client = new AsyncHttpClient();
            String url = Constants.URL_BASE + "search.php";
            RequestParams params = new RequestParams();
            params.put("search", text);
            client.post(url, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(TAG, "failed To upload");
                    throwable.printStackTrace();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                    Log.d(TAG, "Success");
                    try {
                        JSONArray array = new JSONArray(responseString);
                        JSONObject obj = array.getJSONObject(0);
                        String photo = obj.getString("picture");
                      //  Picasso.with(getApplicationContext()).load(Constants.URL_BASE + photo).into(imgPassport);
/*get the user details and set text*/

                        String name = obj.getString("name");

                        HashMap<String, String> deposit = new HashMap<String, String>();

                        deposit.put(name, "name");
                        list.add(deposit);

                        ListAdapter adapter = new SimpleAdapter(
                                DepositActivity.this, list, R.layout.activity_passport,
                                new String[]{name},
                              // display into list
                                 new int[]{R.id.tvNames});

//
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }














