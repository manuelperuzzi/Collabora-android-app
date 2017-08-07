package org.gammf.collabora_android.collabora_android_app;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EditNoteActivity extends AppCompatActivity {

    private static final String NOTE_NAME = "notename";
    private static final String NEW_NAME = "newname";
    private static final String NEW_DESC = "newdesc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        final Intent intent = getIntent();
        String noteName = intent.getStringExtra(NOTE_NAME);

        final TextView txtNewNoteName = (TextView) findViewById(R.id.txtNewNoteName);
        final TextView txtNewNoteDesc = (TextView) findViewById(R.id.txtNewNoteDesc);

        FloatingActionButton btnSaveChanges = (FloatingActionButton) findViewById(R.id.btnSaveChange);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(NEW_NAME, txtNewNoteName.getText().toString());
                resultIntent.putExtra(NEW_DESC, txtNewNoteDesc.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
