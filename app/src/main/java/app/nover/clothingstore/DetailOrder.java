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
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.nover.clothingstore.adapter.CheckoutAdapter;
import app.nover.clothingstore.adapter.StatusCartAdapter;
import app.nover.clothingstore.models.ItemCart;
import app.nover.clothingstore.models.StatusCart;
import app.nover.clothingstore.models.UserModel;

public class DetailOrder extends AppCompatActivity {

    ArrayList nameIntent;
    TextView tvHeaderTitle;
    ImageView tvBack;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    List<String> items;
    List<ItemCart> itemsCart;
    RecyclerView recyclerView;
    String id, idUser;
    CheckoutAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        idUser = bundle.getString("idUser");
        String idItems = bundle.getString("obj");
        items = convertStringToArray(idItems);


//        itemsCart = new ArrayList<>();
//        EventChangeListener();
//
//        recyclerView = findViewById(R.id.lv_checkout);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        adapter = new CheckoutAdapter(itemsCart);
//        recyclerView.setAdapter(adapter);


        final String[] role = {""};
        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    role[0] = task.getResult().toObject(UserModel.class).getRole();
                    itemsCart = new ArrayList<>();
                    if(role[0].equals("admin")) {
                        EventChangeListenerAdmin();
                    } else {
                        EventChangeListener();
                    }

                    recyclerView = findViewById(R.id.lv_checkout);
                    recyclerView.setLayoutManager(new LinearLayoutManager(DetailOrder.this));

                    adapter = new CheckoutAdapter(itemsCart);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        tvHeaderTitle = findViewById(R.id.header_title);

        tvBack = findViewById(R.id.iv_back);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void EventChangeListener() {

        firestore.collection("AddCheckoutItem").document(firebaseAuth.getCurrentUser().getUid()).collection(id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("db", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            Log.e("1", dc.getDocument().toObject(ItemCart.class).getName());
                            itemsCart.add(dc.getDocument().toObject(ItemCart.class));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void EventChangeListenerAdmin() {

        firestore.collection("AddCheckoutItem").document(idUser).collection(id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("db", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            Log.e("1", dc.getDocument().toObject(ItemCart.class).getName());
                            itemsCart.add(dc.getDocument().toObject(ItemCart.class));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }



    public List<String> convertStringToArray(String s1) {
        String replace = s1.replace("[","");
        String replace1 = replace.replace("]","");
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
       return myList;
    }
}