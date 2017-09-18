package org.gammf.collabora_android.app.gui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;

/**
 * A simple {@link ArrayAdapter} subclass.
 *
 * Use this class as a custom adapter for list view
 *
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<CollaborationComponentInfo> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<CollaborationComponentInfo> data = null;


    /**
     * Build a new adapter for expandable list in navigation menu
     * @param mContext the application context
     * @param layoutResourceId represent the component resource layout id
     * @param data the collaboration component list, a list can be filled with note(s) or member(s) or module(s)
     */
    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ArrayList<CollaborationComponentInfo> data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    @NonNull
    public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {

        final LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        final View listItem = inflater.inflate(layoutResourceId, parent, false);

        final ImageView imageViewIcon = listItem.findViewById(R.id.imageViewIcon);
        final TextView textViewName = listItem.findViewById(R.id.textViewName);
        final TextView textViewAdditionalInfo = listItem.findViewById(R.id.textViewAdditionalInfo);

        CollaborationComponentInfo folder = data.get(position);

        imageViewIcon.setImageResource(folder.getIcon());
        textViewName.setText(folder.getContent());
        textViewAdditionalInfo.setText(folder.getAdditionalInfo());

        return listItem;
    }
}

