package uk.co.appoly.sceneform_example;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    ImageButton idkwheretogo;
    ImageButton iknowwheretogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idkwheretogo = (ImageButton) findViewById(R.id.idkwheretogo);
        idkwheretogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openmultiplelocationmarker(); }
        });
        iknowwheretogo = (ImageButton) findViewById(R.id.iknowwheretogo);
        iknowwheretogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openlandingpage();
            }
        });

    }
    public void openlandingpage() {
        Intent intent = new Intent(this, Landing_page.class);
        startActivity(intent);
    }
    public void openmultiplelocationmarker() {
        Intent intent = new Intent(this, Multiplelocationmarker.class);
        startActivity(intent);
    }
}