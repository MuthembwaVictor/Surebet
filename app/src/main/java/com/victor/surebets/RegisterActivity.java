
package com.victor.surebets;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends Activity {
 private static final EditText Button = null;
 private static final int CAMERA_REQUEST = 1888;
	private static final int PERMISSIONS_ACCESS_GALLERY = 2000;
	ImageView imageView1;
 EditText first;
 EditText second;
 EditText identity;
	Bitmap bitmap;
 EditText birth;
 Button  register,takephoto;
	private static final int PICK_IMAGE_REQUEST = 20000;
	ProgressDialog progressDialog;
	String imgPath="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_service);
		imageView1= (ImageView) findViewById(R.id.imageView1);
		takephoto= (android.widget.Button) findViewById(R.id.takephoto);
		progressDialog=new ProgressDialog(this);
		progressDialog.setMessage("Registering. Please Wait");
		progressDialog.setCancelable(false);
		
		first = (EditText) findViewById(R.id.first);
		second = (EditText) findViewById(R.id.second);
		identity = (EditText) findViewById(R.id.identity);
		birth = (EditText) findViewById(R.id.birth);
		register = (android.widget.Button) findViewById(R.id.register);
		takephoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showFileChooser();
			}
		});
		
		
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				upload_details();
				
				
			}
		});
			
    
				
			}

	private void upload_details() {
		AsyncHttpClient c = new AsyncHttpClient();

		try {
			RequestParams params = new RequestParams();
			File myFile =new File(imgPath);
			params.put("fileToUpload", myFile);
			params.add("first",first.getText().toString());
			params.add("second",second.getText().toString());
			params.add("identity",identity.getText().toString());
			params.add("birth",birth.getText().toString());
			// the 2 parameters are posted to this PHP file which is in wamp
			progressDialog.show();
			c.post(Constants.URL_BASE+"add.php", params, new TextHttpResponseHandler(){
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                  Log.e("SERVER","FAILED" + responseString);
					throwable.printStackTrace();
					progressDialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
					Log.d("SERVER","SUCCESS");
					progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),	"Saved "+responseString,Toast.LENGTH_LONG).show();
					Log.d("SERVER","SUCCESS" + responseString);
					if(responseString.toLowerCase().contains("success")){
					    first.setText("");
						second.setText("");
						identity.setText("");
						birth.setText("");

					}
                }
            });
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register
				, menu);
		return true;
	}

	//STEP 2 Display gallery to allow the user to choose the photo
	private void showFileChooser() {
		if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_ACCESS_GALLERY);
		}

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
	}
	//STEP 3 Display the selected image on the image view and set the path
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			Uri filePath = data.getData();
			try {
				//Getting the Bitmap from Gallery
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
				//Setting the Bitmap to ImageView
				imageView1.setImageBitmap(bitmap);
				imgPath=getPath(filePath);
				Log.d("PATH",imgPath);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//method to get the file path from uri
	public String getPath(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		String document_id = cursor.getString(0);
		document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
		cursor.close();

		cursor = getContentResolver().query(
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
		cursor.moveToFirst();
		String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		cursor.close();

		return path;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		//super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode)
		{
			case PERMISSIONS_ACCESS_GALLERY: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


				} else
				{
					Log.d("PERMISSION","DENIED");
				}
				return;
			}

		}
	}
}
