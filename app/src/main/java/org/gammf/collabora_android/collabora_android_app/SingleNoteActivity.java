package org.gammf.collabora_android.collabora_android_app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SingleNoteActivity extends AppCompatActivity {

    private static final String NOTE_NAME = "notename";
    private static final String NOTE_DESC = "notedesc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Bundle s = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);
        Intent intent = getIntent();
        final String intentTitle = intent.getStringExtra(NOTE_NAME);
        final String intentDesc = intent.getStringExtra(NOTE_DESC);

        TextView title = (TextView) findViewById(R.id.txtNoteName);
        title.setText(intentTitle);

        TextView description = (TextView) findViewById(R.id.txtNoteDescription);
        description.setText(intentDesc);

        ImageButton btnEditNote = (ImageButton) findViewById(R.id.btnEditNote);
        btnEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SingleNoteActivity.this, EditNoteActivity.class);
                myIntent.putExtra(NOTE_NAME, intentTitle);
                myIntent.putExtra(NOTE_DESC, intentDesc);
                SingleNoteActivity.this.startActivity(myIntent);
            }
        });
    }
}
