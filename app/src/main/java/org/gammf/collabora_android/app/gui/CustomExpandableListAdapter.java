package org.gammf.collabora_android.app.gui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import org.gammf.collabora_android.app.R;
import org.gammf.collabora_android.short_collaborations.ShortCollaboration;

/**
 * Created by Mattia on 21/08/2017.
 *
 * Expandable list adapter customized for collaboration list drawer in main menu
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<Pair<String, List<ShortCollaboration>>> collaborationList;

    public CustomExpandableListAdapter(final Context context,
                                       final List<Pair<String, List<ShortCollaboration>>> collaborationList) {
        this.context = context;
        this.collaborationList = collaborationList;
    }

    @Override
    public Object getChild(final int listPosition, final int expandedListPosition) {
        return this.collaborationList.get(listPosition).second.get(expandedListPosition);
    }

    @Override
    public long getChildId(final int listPosition, final int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             final boolean isLastChild, View convertView, final ViewGroup parent) {
        final ShortCollaboration item = (ShortCollaboration) getChild(listPosition, expandedListPosition);
        final String expandedListText = item.getName();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandablelist_item, null);
        }
        final TextView expandedListTextView = convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(final int listPosition) {
        return this.collaborationList.get(listPosition).second.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.collaborationList.get(listPosition).first;
    }

    @Override
    public int getGroupCount() {
        return this.collaborationList.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(final int listPosition, final boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        final String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandablelist_titlegroup, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.expandableListGroupTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
