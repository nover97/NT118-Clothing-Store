package app.nover.clothingstore;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.nover.clothingstore.models.ItemDetails;
import app.nover.clothingstore.models.ItemModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private static final String TAG = "HOME Fragment";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ItemModel> itemModelArrayList;
    HomeItemAdapter homeItemAdapter;
    ProgressDialog progressDialog;
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        GridView gridView = view.findViewById(R.id.item_gridview_home);

        // Sample data
        List<ItemModel> itemList = new ArrayList<>();

        itemList.add(new ItemModel("gs://clothing-store-c9cdd.appspot.com/Products/1-BBP287yuIxOl5rTYKebA_900x (1).webp", "Item 1", 20000, 30000));
        itemList.add(new ItemModel("gs://clothing-store-c9cdd.appspot.com/Products/1-BBP287yuIxOl5rTYKebA_900x (1).webp", "Item 2", 25000, 35000));
        // Add more items as needed

        HomeItemAdapter itemAdapter = new HomeItemAdapter(requireContext(), R.layout.item_cardview, itemList);
        gridView.setAdapter(itemAdapter);
        
        if (progressDialog.isShowing())
            progressDialog.dismiss();

//        EventChangeListener();


        return view;
    }

    private void EventChangeListener() {
//        db.collection("Products").orderBy("price", Query.Direction.ASCENDING)
//                .addSnapshotListener((value, error) -> {
//                    if (error != null) {
//                        if (progressDialog.isShowing())
//                            progressDialog.dismiss();
//                        Log.e(TAG, "EventChangeListener: " + error.getMessage());
//                        return;
//                    } else {
//                        assert value != null;
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() != DocumentChange.Type.ADDED) {
//                                ItemDetails itemDetails = dc.getDocument().toObject(ItemDetails.class);
//                                ItemModel itemModel = new ItemModel(itemDetails.getImageUrl(), itemDetails.getName(), itemDetails.getPrice(), 0);
//                                Log.d(TAG, itemDetails.toString());
//                                itemModelArrayList.add(itemModel);
//                            }
//                            homeItemAdapter.notifyDataSetChanged();
//                        }
//
//                    }
//                });

    }


}