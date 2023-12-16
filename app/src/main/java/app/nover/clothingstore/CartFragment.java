package app.nover.clothingstore;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.nover.clothingstore.adapter.CartAdapter;
import app.nover.clothingstore.models.ItemCart;

/**
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    List<ItemCart> items;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    TextView tvTotal;
    Button tvHome;
    Button btnCheckout, btnCheck, btnUncheck;
    LinearLayout emptyLayout, checkout, layoutTop;
    int totalCart = 0;
    int totalAmount;
    private FirebaseFirestore firestore;

    public CartFragment() {

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
        tvTotal = view.findViewById(R.id.tv_total_cart);
        btnCheckout = view.findViewById(R.id.btn_checkout);
        emptyLayout = view.findViewById(R.id.tv_empty);
        tvHome = view.findViewById(R.id.go_to_home);
        checkout = view.findViewById(R.id.go_to_checkout);
        layoutTop = view.findViewById(R.id.layout_top);
        btnCheck = view.findViewById(R.id.btn_check);
        btnUncheck = view.findViewById(R.id.btn_uncheck);

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });


        items = new ArrayList<>();

        EventChangeListener();
        cartAdapter = new CartAdapter(items);


        recyclerView.setAdapter(cartAdapter);

        btnCheckout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (totalCart == 0) {
                    Toast.makeText(getContext(), "No have product in cart", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), Checkout.class);
                startActivity(intent);
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (items.size() > 0) {
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).setIsCheck(true);
                    }
                    cartAdapter.notifyDataSetChanged();
                }
            }
        });

        btnUncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (items.size() > 0) {
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).setIsCheck(false);
                    }
                    cartAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }


    private void EventChangeListener() {

        firestore.collection("AddToCart").document(firebaseAuth.getCurrentUser().getUid()).collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("db", error.getMessage());
                            return;
                        }

                        int i = 0;
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                items.add(dc.getDocument().toObject(ItemCart.class));
                            }
                            if (dc.getDocument().toObject(ItemCart.class).getIsCheck()) {
                                i++;
                            }
                        }

                        if (items.size() > 0) {
                            layoutTop.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);

                        } else {
                            layoutTop.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);

                        }
                        cartAdapter.notifyDataSetChanged();
                        totalCart = grandTotal((items));

                        tvTotal.setText(convertDot(grandTotal(items) + ""));

                    }

                });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private int grandTotal(List<ItemCart> items) {
        if (items.size() == 0) {
            return 0;
        }
        int totalPrice = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getIsCheck()) {
                totalPrice += Integer.parseInt(items.get(i).getPrice()) * Integer.parseInt(items.get(i).getCount());
            }
        }
        return totalPrice;
    }

    public String convertDot(String no) {
        if (no.length() == 0) {
            return "";
        }
        Integer no1 = Integer.parseInt(no);
        return String.format(Locale.US, "%,d", no1).replace(',', '.') + "đ";
    }

    public List reverseList(List items) {
        for (int k = 0, j = items.size() - 1; k < j; k++) {
            items.add(k, items.remove(j));
        }
        return items;
    }
}