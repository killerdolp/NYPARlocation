package uk.co.appoly.sceneform_example;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;

public class Landing_page extends AppCompatActivity {
    ImageButton buttonkoufu;
    ImageButton buttonfoodgle;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);


        //location for koufu
        //button is found here
        buttonkoufu = (ImageButton) findViewById(R.id.OpenARcoreKouFu);
        buttonkoufu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input lan and long here
                openNewActivity("1.379692","103.849135","koufu");
            }
        });
        //foodgle
        buttonfoodgle = (ImageButton) findViewById(R.id.OpenARcoreFoodgle);
        buttonfoodgle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input lan and long here
                openNewActivity("1.376935","103.849511","foodgle");
            }
        });
    }

    public void openNewActivity(String lan , String longitude,String nameinfo) {
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("lan",lan);
        intent.putExtra("long",longitude);
        intent.putExtra("nameinfo",nameinfo);
        startActivity(intent);
    }

}