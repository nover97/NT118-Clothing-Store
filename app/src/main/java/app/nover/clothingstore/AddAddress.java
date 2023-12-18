package app.nover.clothingstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import app.nover.clothingstore.adapter.AddressAdapter;
import app.nover.clothingstore.models.AddressModel;
import app.nover.clothingstore.models.ItemCart;
import app.nover.clothingstore.models.StatusCart;
import app.nover.clothingstore.models.StatusCartComparator;

public class AddAddress extends AppCompatActivity {
    ImageView imBack;
    EditText etName, etPhoneNumber, etAddress;
    Button btnDelete, btnSave;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    List<AddressModel> items;
    AddressAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        imBack = findViewById(R.id.iv_back);
        etName = findViewById(R.id.et_full_name);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etAddress = findViewById(R.id.et_address);
        recyclerView = findViewById(R.id.rv_address);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);

        items = new ArrayList<>();
        EventChangeListener();
        adapter = new AddressAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSaveAddressUser();
                clearEditText();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearEditText();
            }
        });
    }

    public void handleSaveAddressUser() {
        HashMap<String,Object> address = new HashMap<>();
        String id =  getAlphaNumericString(20);
        address.put("id", id);
        address.put("fullName", etName.getText().toString());
        address.put("phoneNumber", etPhoneNumber.getText().toString());
        address.put("addressUser", etAddress.getText().toString());

        firestore.collection("AddAddress").document(firebaseAuth.getCurrentUser().getUid()).collection("Users").document(id).set(address).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.e("1", "check");
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

    public String getAlphaNumericString(int n)
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public void clearEditText() {
        etName.setText("");
        etAddress.setText("");
        etPhoneNumber.setText("");
    }

}