package org.gammf.collabora_android.app.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.gammf.collabora_android.app.R;

import java.util.ArrayList;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class CollaborationFragment extends Fragment {

    Bundle arguments;
    TextView label;
    String collabname;
    ListView notesList;
    ArrayList<DataModel> drawerItem;

    public CollaborationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_connect, container, false);
        Bundle bundle = getArguments();
        collabname =  bundle.getString("collabName");


     /*   Handler txtsettext = new Handler(Looper.getMainLooper());
        txtsettext.post(new Runnable() {
            public void run() {
                label.setText(collabname);
            }
        });
*/

        notesList = (ListView) rootView.findViewById(R.id.notesListView);

        drawerItem = new ArrayList<DataModel>();
        drawerItem.add(new DataModel(R.drawable.connect, "Connect"));
        drawerItem.add(new DataModel(R.drawable.fixtures, "Fixtures"));
        drawerItem.add(new DataModel(R.drawable.table, "Table"));
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, drawerItem);
        notesList.setAdapter(adapter);
        notesList.setOnItemClickListener(new CollaborationFragment.DrawerItemClickListener());


        return rootView;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final DataModel listName = (DataModel) parent.getItemAtPosition(position);
            selectItem(position, listName.getName());
        }

    }

    private void selectItem(int position, String itemName) {

        Fragment fragment = null;
        Bundle fragmentArgument = new Bundle();
        fragment = new NoteFragment();
        fragmentArgument.putString("collabName", "Ciao");

        fragment.setArguments(fragmentArgument);
        if (fragment != null) {

            FragmentManager fragmentManager2 = getFragmentManager();
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
            fragmentTransaction2.addToBackStack("xyz");
            fragmentTransaction2.hide(CollaborationFragment.this);
            //fragmentTransaction2.add(android.R.id.content, fragment);
            fragmentTransaction2.replace(R.id.content_frame, fragment);
            fragmentTransaction2.commit();

            /*
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(itemName);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
*/

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
