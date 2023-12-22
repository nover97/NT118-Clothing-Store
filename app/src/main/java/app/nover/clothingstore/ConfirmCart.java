package app.nover.clothingstore;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import app.nover.clothingstore.adapter.CheckoutAdapter;
import app.nover.clothingstore.adapter.StatusCartAdapter;
import app.nover.clothingstore.models.ItemCart;
import app.nover.clothingstore.models.StatusCart;
import app.nover.clothingstore.models.StatusCartComparator;
import app.nover.clothingstore.models.UserModel;

public class ConfirmCart extends AppCompatActivity {

    ImageView tvBack;
    List<StatusCart> items;
    RecyclerView recyclerView;
    StatusCartAdapter adapter;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_cart);


        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        tvBack = findViewById(R.id.iv_back);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final String[] role = {""};
        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    role[0] = task.getResult().toObject(UserModel.class).getRole();
                    items = new ArrayList<>();
                    if(role[0].equals("admin")) {
                        EventChangeListenerAdmin();
                    } else {
                        EventChangeListener();
                    }

                    recyclerView = findViewById(R.id.rv_pending);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ConfirmCart.this));

                    adapter = new StatusCartAdapter(items);
                    recyclerView.setAdapter(adapter);
                }
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
                            if(dc.getDocument().toObject(StatusCart.class).getStatusCode().equals("3")){
                                items.add(dc.getDocument().toObject(StatusCart.class));

                            }
                        }

                        adapter.notifyDataSetChanged();
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
                                                        if(document.toObject(StatusCart.class).getStatusCode().equals("3")) {
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