package app.nover.clothingstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Arrays;
import java.util.List;

import app.nover.clothingstore.R;

public class BlankFragment extends DialogFragment {

    private ListView listView;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.f_blank_list);

        List<String> strings = Arrays.asList("Hello", "World", "Bye");
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strings);

        listView.setAdapter(arrayAdapter);

    }
}