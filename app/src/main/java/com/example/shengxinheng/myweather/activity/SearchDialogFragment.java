package com.example.shengxinheng.myweather.activity;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shengxinheng.myweather.R;
import com.example.shengxinheng.myweather.activity.adapter.SearchResultAdapter;
import com.example.shengxinheng.myweather.datamodel.Location;
import com.example.shengxinheng.myweather.modelview.LocationsModel;
import com.example.shengxinheng.myweather.weather.WeatherService;

import java.util.List;

public class SearchDialogFragment extends DialogFragment {

    public interface SearchDialogListener {
        public void onDialogSearchClick(DialogFragment dialog, String text);
        public void onDialogSelectClick(DialogFragment dialog, Location location);
    }

    private SearchDialogListener listener;
    private LocationsModel locationsModel;
    private ListView listView;
    private SearchResultAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SearchDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        locationsModel = ViewModelProviders.of(getActivity()).get(LocationsModel.class);
        locationsModel.getLocations().observe(getActivity(), new Observer<List>() {
            @Override
            public void onChanged(@Nullable List list) {
                if(adapter == null) return;
                adapter.reload(list);
                adapter.notifyDataSetChanged();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.search, null);
        adapter = new SearchResultAdapter(getContext());
        listView = view.findViewById(R.id.city_result);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Location location = (Location) adapterView.getItemAtPosition(i);
                listener.onDialogSelectClick(SearchDialogFragment.this, location);
                SearchDialogFragment.this.getDialog().cancel();
            }
        });
        AppCompatButton button = view.findViewById(R.id.search_button);
        final AppCompatEditText editText = view.findViewById(R.id.city_input);
                button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().equals("")){
                    listener.onDialogSearchClick(SearchDialogFragment.this, editText.getText().toString());
                }
            }
        });
        builder.setView(view).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SearchDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }


}
