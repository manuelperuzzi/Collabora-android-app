package org.gammf.collabora_android.collabora_android_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Bundle s = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Intent intent = getIntent();
        String titolo = intent.getStringExtra("title");
        String position = intent.getStringExtra("position");

        TextView moduleTitle = (TextView) findViewById(R.id.txtModule);
        moduleTitle.setText(titolo);

    }
}
