package app.nover.clothingstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.nover.clothingstore.adapter.StatusCartAdapter;
import app.nover.clothingstore.models.StatusCart;
import app.nover.clothingstore.models.StatusCartComparator;

public class HistoryOrder extends AppCompatActivity {

    ImageView tvBack;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    List<StatusCart> items;
    RecyclerView recyclerView;
    StatusCartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        tvBack = findViewById(R.id.iv_back);

        items = new ArrayList<>();
        EventChangeListener();

        recyclerView = findViewById(R.id.rv_pending);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StatusCartAdapter(items);
        recyclerView.setAdapter(adapter);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                            if(dc.getDocument().toObject(StatusCart.class).getStatusCode().equals("4")){
                                items.add(dc.getDocument().toObject(StatusCart.class));

                            }
                            if(dc.getDocument().toObject(StatusCart.class).getStatusCode().equals("5")){
                                items.add(dc.getDocument().toObject(StatusCart.class));

                            }
                            if(dc.getDocument().toObject(StatusCart.class).getStatusCode().equals("6")){
                                items.add(dc.getDocument().toObject(StatusCart.class));

                            }
                        }
                        Collections.sort(items, new StatusCartComparator());

                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
