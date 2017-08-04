package org.gammf.collabora_android.collabora_android_app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Intent intent = getIntent();
        String titolo = intent.getStringExtra("title");
        String position = intent.getStringExtra("position");

        TextView moduleTitle = (TextView) findViewById(R.id.txtModule);
        moduleTitle.setText(titolo);

    }
}
