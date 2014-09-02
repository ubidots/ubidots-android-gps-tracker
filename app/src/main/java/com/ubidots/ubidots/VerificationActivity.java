package com.ubidots.ubidots;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class VerificationActivity extends Activity {
    private ImageView mMapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mMapImage = (ImageView) findViewById(R.id.continue_map);

        mMapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerificationActivity.this, UbidotsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
