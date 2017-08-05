package org.gammf.collabora_android.collabora_android_app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Bundle s = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Intent intent = getIntent();
        String titolo = intent.getStringExtra("title");
        String position = intent.getStringExtra("position");

        TextView moduleTitle = (TextView) findViewById(R.id.txtModule);
        moduleTitle.setText(titolo);

        Button btnChange = (Button) findViewById(R.id.btnchangefragment);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment profileFragment = new newFragment();//the fragment you want to show
                profileFragment.setArguments(s);
                fragmentTransaction.remove(getFragmentManager().findFragmentById(R.id.id_fragment));
                fragmentTransaction.replace(R.id.id_fragment, profileFragment);//R.id.content_frame is the layout you want to replace
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                */
            }
        });
    }
}
