package app.nover.clothingstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.nover.clothingstore.adapter.AddressAdapter;
import app.nover.clothingstore.adapter.ChosenAdapter;
import app.nover.clothingstore.models.AddressModel;

public class ChoosenAddress extends AppCompatActivity {
    RecyclerView recyclerView;
    ChosenAdapter adapter;
    ImageView imBack;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    List<AddressModel> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosen_address);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        imBack = findViewById(R.id.iv_back);

        recyclerView = findViewById(R.id.lv_address);
        items = new ArrayList<>();
        EventChangeListener();
        adapter = new ChosenAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void EventChangeListener() {
        firestore.collection("AddAddress").document(firebaseAuth.getCurrentUser().getUid()).collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("db", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                items.add(dc.getDocument().toObject(AddressModel.class));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        this.remove();
//    }
}