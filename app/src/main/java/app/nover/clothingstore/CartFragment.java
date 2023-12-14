package app.nover.clothingstore;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.nover.clothingstore.adapter.CartAdapter;
import app.nover.clothingstore.models.ItemCart;

/**
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    List<ItemCart> items;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;


    int totalAmount;
    public CartFragment () {

    }
     @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.lv_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        items = new ArrayList<>();

        EventChangeListener();
        cartAdapter = new CartAdapter(items);


        recyclerView.setAdapter(cartAdapter);

        return view;
    }



    private void EventChangeListener() {
        firestore.collection("AddToCart").document(firebaseAuth.getCurrentUser().getUid()).collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            Log.e("db", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc:value.getDocumentChanges()) {
                            if(dc.getType() == DocumentChange.Type.ADDED) {
                                items.add(dc.getDocument().toObject(ItemCart.class));
                                Log.e("TA", items.toString());
                            }
                        }
                        cartAdapter.notifyDataSetChanged();
                    }

                });
    }
}