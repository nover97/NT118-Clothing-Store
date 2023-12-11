package app.nover.clothingstore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.nover.clothingstore.adapter.ProductAdapter;
import app.nover.clothingstore.models.ItemModel;


public class HomeFragment extends Fragment {

    List<ItemModel> items;
    ProductAdapter productAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore db;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.item_gridview_home);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        items = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        EventChangeListener();

        productAdapter = new ProductAdapter(items);
        recyclerView.setAdapter(productAdapter);



        return view;
    }



    private void EventChangeListener() {
        db.collection("Products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            Log.e("db", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc:value.getDocumentChanges()) {
                            if(dc.getType() == DocumentChange.Type.ADDED) {
                                items.add(dc.getDocument().toObject(ItemModel.class));
                            }
                        }
                        productAdapter.notifyDataSetChanged();
                    }

                });
    }

}