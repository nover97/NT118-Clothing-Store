package app.nover.clothingstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.nover.clothingstore.adapter.CheckoutAdapter;
import app.nover.clothingstore.adapter.StatusCartAdapter;
import app.nover.clothingstore.models.ItemCart;
import app.nover.clothingstore.models.StatusCart;

public class DeliveryCart extends AppCompatActivity {

    ImageView tvBack;
    List<StatusCart> items;
    RecyclerView recyclerView;
    StatusCartAdapter adapter;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_cart);


        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        tvBack = findViewById(R.id.iv_back);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        items = new ArrayList<>();
        EventChangeListener();

        recyclerView = findViewById(R.id.rv_pending);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StatusCartAdapter(items);
        recyclerView.setAdapter(adapter);

    }

    private void EventChangeListener() {
        firestore.collection("AddToCheckout").document(firebaseAuth.getCurrentUser().getUid()).collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("db", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getDocument().toObject(StatusCart.class).getStatusCode().equals("2")) {
                                items.add(dc.getDocument().toObject(StatusCart.class));

                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
    }
}