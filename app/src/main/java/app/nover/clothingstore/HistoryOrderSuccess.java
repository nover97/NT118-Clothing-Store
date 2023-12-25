package app.nover.clothingstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.nover.clothingstore.adapter.StatusCartAdapter;
import app.nover.clothingstore.models.StatusCart;
import app.nover.clothingstore.models.StatusCartComparator;

public class HistoryOrderSuccess extends AppCompatActivity {
    ImageView tvBack;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    List<StatusCart> items;
    RecyclerView recyclerView;
    StatusCartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order_failure);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        tvBack = findViewById(R.id.iv_back);

        items = new ArrayList<>();
        EventChangeListenerAdmin();

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


    private void EventChangeListenerAdmin() {
        firestore.collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                firestore.collection("AddToCheckout")
                                        .document(document.getId()).collection("Users")
                                        .get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if(document.toObject(StatusCart.class).getStatusCode().equals("6")) {
                                                            items.add(document.toObject(StatusCart.class));
                                                        }
                                                        Log.e("TAG", items.size()+"");
                                                        Collections.sort(items, new StatusCartComparator());
                                                        adapter.notifyDataSetChanged();

                                                    }
                                                } else {
                                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                            }
                        } else {
                            Log.d("123", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}