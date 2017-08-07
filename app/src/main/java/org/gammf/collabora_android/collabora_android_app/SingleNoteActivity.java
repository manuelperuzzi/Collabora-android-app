package org.gammf.collabora_android.collabora_android_app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SingleNoteActivity extends AppCompatActivity {

    String noteName, noteDescription;
    TextView title,description;
    private static final String NOTE_NAME = "notename";
    private static final String NOTE_DESC = "notedesc";
    private static final String NEW_NAME = "newname";
    private static final String NEW_DESC = "newdesc";
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Bundle s = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);
        Intent intent = getIntent();
        final String intentTitle = intent.getStringExtra(NOTE_NAME);
        final String intentDesc = intent.getStringExtra(NOTE_DESC);

        title = (TextView) findViewById(R.id.txtNoteName);
        title.setText(intentTitle);

        description = (TextView) findViewById(R.id.txtNoteDescription);
        description.setText(intentDesc);

        ImageButton btnEditNote = (ImageButton) findViewById(R.id.btnEditNote);
        btnEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SingleNoteActivity.this, EditNoteActivity.class);
                myIntent.putExtra(NOTE_NAME, intentTitle);
                myIntent.putExtra(NOTE_DESC, intentDesc);
                startActivityForResult(myIntent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    noteName = data.getStringExtra(NEW_NAME);
                    noteDescription = data.getStringExtra(NEW_DESC);
                    SingleNoteActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            title.setText(noteName);
                            description.setText(noteDescription);
                        }
                    });
                }
                break;
            }
        }
    }
}
