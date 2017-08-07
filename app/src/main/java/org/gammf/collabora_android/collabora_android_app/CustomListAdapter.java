package org.gammf.collabora_android.collabora_android_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mattia on 05/08/2017.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemname;
    private final ArrayList<String> itemdescription;

    public CustomListAdapter(Activity context, ArrayList<String> itemname) {
        super(context, R.layout.list_item, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.itemdescription = new ArrayList<String>();
        for(String s : itemname){
            this.itemdescription.add("Description "+s);
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname.get(position));
        extratxt.setText(itemdescription.get(position));
        return rowView;

    };

    public void addList(String listName, String listDescription){
        itemname.add(listName);
        itemdescription.add(listDescription);
    }
}
