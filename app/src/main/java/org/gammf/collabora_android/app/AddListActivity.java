package org.gammf.collabora_android.app;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddListActivity extends AppCompatActivity {

    String listType;
    private static final String LIST_TYPE = "listtype";
    private static final String LIST_NAME = "listname";
    private static final String LIST_DESCRIPTION = "listdescription";

    private static final String ADD_NOTENAME = "notenameadded";
    private static final String ADD_NOTEDESC = "notedescadded";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        final Intent intent = getIntent();
        this.listType = intent.getStringExtra("type");

        final TextView lblTitleAddListActivity = (TextView) findViewById(R.id.lblTitleAddListActivity);
        if (listType.equals("newnote")) {
            lblTitleAddListActivity.setText("Add New Note");
        }else{
            lblTitleAddListActivity.setText("Add New List");
        }
        final EditText txtListName = (EditText) findViewById(R.id.txtListName);
        final EditText txtListDescription = (EditText) findViewById(R.id.txtListDescription);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listType.equals("personallist")){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(LIST_TYPE, "personallist");
                    resultIntent.putExtra(LIST_NAME, txtListName.getText().toString());
                    resultIntent.putExtra(LIST_DESCRIPTION, txtListDescription.getText().toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                }else if(listType.equals("projectlist")){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(LIST_TYPE, "projectlist");
                    resultIntent.putExtra(LIST_NAME, txtListName.getText().toString());
                    resultIntent.putExtra(LIST_DESCRIPTION, txtListDescription.getText().toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }else if(listType.equals("newnote")){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(ADD_NOTENAME, txtListName.getText().toString());
                    resultIntent.putExtra(ADD_NOTEDESC, txtListDescription.getText().toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}
