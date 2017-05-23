package com.victor.surebets;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	Button withdraw;
	Button deposit;
	Button register;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        withdraw = (Button) findViewById(R.id.withdraw);
        deposit = (Button) findViewById(R.id.deposit);
        register=(Button) findViewById(R.id.register);
        
        
        withdraw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent a = new Intent(MainActivity.this,WithdrawActivity.class);
				startActivity(a);
			}
		});
deposit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent b = new Intent(MainActivity.this,DepositActivity.class);
				startActivity(b);
			}
		});
        

        
register.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent c = new Intent(MainActivity.this,RegisterActivity.class);
		startActivity(c);
	}
});
        
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
