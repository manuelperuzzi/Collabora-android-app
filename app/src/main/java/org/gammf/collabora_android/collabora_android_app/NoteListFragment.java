package org.gammf.collabora_android.collabora_android_app;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Mattia on 04/08/2017.
 */

public class NoteListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] itemName = new String[]{
            "Item1",
            "Item2",
            "Item3",
            "Item4"
    };

    ArrayList<String> listItem = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //adapter = ArrayAdapter.createFromResource(getActivity(), R.array.planets, android.R.layout.simple_list_item_2);
        for(String s : itemName){
            listItem.add(s);
        }
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItem);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getActivity(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment profileFragment = new newFragment();//the fragment you want to show
        profileFragment.setArguments(savedInstanceState);
        fragmentTransaction.replace(R.id.id_fragment, profileFragment);//R.id.content_frame is the layout you want to replace
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
