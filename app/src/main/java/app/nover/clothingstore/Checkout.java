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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import app.nover.clothingstore.adapter.CheckoutAdapter;
import app.nover.clothingstore.modal.DialogModal;
import app.nover.clothingstore.models.ItemCart;

public class Checkout extends AppCompatActivity implements DialogModal.ExampleDialogListener{

    RecyclerView recyclerView;
    CheckoutAdapter adapter;
    List<ItemCart> items;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    TextView tvTotal, tvEdit,tvName, tvPhone, tvAddress,tvHome, tvChosen;
    LinearLayout emptyLayout;
    ImageView imageBack;
    Spinner spinnerPayment;
    Button btnCheckout;
    String paymentOption;
    String totalCart;
    String paymentChoose;

    private   String[] payment= {"Payment on delivery", "Payment via Momo wallet"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tvTotal = findViewById(R.id.tv_total_cart);
        tvAddress = findViewById(R.id.tv_address);
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvEdit = findViewById(R.id.tv_edit);
        emptyLayout = findViewById(R.id.tv_empty);
        tvHome = findViewById(R.id.go_to_home);
        imageBack = findViewById(R.id.iv_back);
        btnCheckout = findViewById(R.id.btn_confirm);
        tvChosen = findViewById(R.id.tv_choose);


        imageBack.setOnClickListener(view -> {
            this.finish();
        });


        tvChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Checkout.this, ChoosenAddress.class));
            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Checkout.this, MainActivity.class));
            }
        });

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        items = new ArrayList<>();
        EventChangeListener();


        recyclerView = findViewById(R.id.lv_checkout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CheckoutAdapter(items);
        recyclerView.setAdapter(adapter);

        setInfoUser();

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        spinnerPayment = (Spinner) findViewById(R.id.spinner_payment);
        ArrayAdapter<String> adapterColor = new ArrayAdapter<String>(Checkout.this,
                android.R.layout.simple_spinner_item, payment);

        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayment.setAdapter(adapterColor);
        spinnerPayment.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        paymentOption= item.toString();

                        //prints the text in spinner item.
                    }
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOnClickCheckout();
            }
        });
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
                            Log.e("obj", dc.getDocument().toObject(ItemCart.class).getName());
                            if(dc.getDocument().toObject(ItemCart.class).getIsCheck()==true){
                                items.add(dc.getDocument().toObject(ItemCart.class));
                            }
                        }

                       if(items.size() > 0) {
                           recyclerView.setVisibility(View.VISIBLE);
                           emptyLayout.setVisibility(View.GONE);
                       } else {
                           recyclerView.setVisibility(View.GONE);
                           emptyLayout.setVisibility(View.VISIBLE);
                       }

                        adapter.notifyDataSetChanged();
                        totalCart = grandTotal(items)+"";
                        tvTotal.setText(convertDot(grandTotal(items)+""));

                    }
                });
    }

    private void setInfoUser() {
        Intent intent = getIntent();
        String idAddress = intent.getStringExtra("id");
        if(idAddress.isEmpty()) {
            firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        String name = documentSnapshot.getString("fullName");
                        String phoneNumber = documentSnapshot.getString("phoneNumber");
                        String address = documentSnapshot.getString("address");

                        applyTexts(name,phoneNumber,address);
                    }
                    else{
                        Toast.makeText(Checkout.this, "Record not found.", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Checkout.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            firestore.collection("AddAddress").document(firebaseAuth.getCurrentUser().getUid()).collection("Users").document(idAddress).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        String name = documentSnapshot.getString("fullName");
                        String phoneNumber = documentSnapshot.getString("phoneNumber");
                        String address = documentSnapshot.getString("addressUser");

                        applyTexts(name,phoneNumber,address);
                    }
                    else{
                        Toast.makeText(Checkout.this, "Record not found.", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Checkout.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    public void handleOnClickCheckout() {
        final HashMap<String,Object> checkoutMap = new HashMap<>();
        final HashMap<String,Object> checkoutItemMap = new HashMap<>();

        String idCheckoutMap = getAlphaNumericString(20);
        String idCheckoutItemMap = getAlphaNumericString(20);

        if(tvName.getText().toString().isEmpty() || tvPhone.getText().toString().isEmpty()||tvAddress.getText().toString().isEmpty()) {
            Toast.makeText(Checkout.this,"Missing parameter!!!", Toast.LENGTH_SHORT).show();
            return;

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Date date = new Date();
            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");

            checkoutMap.put("dateCreateAt", formatterDate.format(date));
            checkoutMap.put("timeCreateAt", formatterTime.format(date));

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            checkoutMap.put("timestampCreateAt", tsLong);

        }

        checkoutMap.put("name", tvName.getText().toString());
        checkoutMap.put("phoneNumber", tvPhone.getText().toString());
        checkoutMap.put("address", tvAddress.getText().toString());
        checkoutMap.put("payment", paymentOption);
        checkoutMap.put("total", totalCart);

        checkoutMap.put("id", idCheckoutMap);
        checkoutMap.put("idCheckoutItem", idCheckoutItemMap);
        checkoutMap.put("statusCode","1");

        checkoutItemMap.put("id", checkoutItemMap);
        checkoutItemMap.put("idCheckout", idCheckoutMap);

        List<String> array = new ArrayList<String>();


        for(int i = 0; i<items.size();i++) {
            String idItem = getAlphaNumericString(20);
            array.add(idItem);
            checkoutItemMap.put("id", idItem);
            checkoutItemMap.put("name", items.get(i).getName());
            checkoutItemMap.put("price", items.get(i).getPrice());
            checkoutItemMap.put("count", items.get(i).getCount());
            checkoutItemMap.put("imageUrl",items.get(i).getImageUrl());
            checkoutItemMap.put("size", items.get(i).getSize());
            checkoutItemMap.put("color", items.get(i).getColor());


            firestore.collection("AddCheckoutItem").document(firebaseAuth.getCurrentUser().getUid())
                    .collection(idCheckoutMap).document(idItem).set(checkoutItemMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });

        }
        checkoutMap.put("arrayIdItem", array);


        //upload data to database
        firestore.collection("AddToCheckout").document(firebaseAuth.getCurrentUser().getUid())
                .collection("Users").document(idCheckoutMap).set(checkoutMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Checkout.this,"Checkout is successfully", Toast.LENGTH_SHORT).show();
                    }
                });


        firestore.collection("AddToCart").document(firebaseAuth.getCurrentUser().getUid()).collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            for (int i = 0; i < myListOfDocuments.size(); i++) {
                                deleteItemAfterCheckout(myListOfDocuments.get(i).getId());
                            }
                        }
                    }
                });
        startActivity(new Intent(Checkout.this, MainActivity.class));

    }

    public void deleteItemAfterCheckout(String id) {
        firestore.collection("AddToCart").document(firebaseAuth.getCurrentUser().getUid())
                .collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            ItemCart itemCart =  documentSnapshot.toObject(ItemCart.class);
                            if(itemCart.getIsCheck()) {
                                firestore.collection("AddToCart").document(firebaseAuth.getCurrentUser().getUid()).collection("Users").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.e("delete", "Success");
                                    }
                                });
                            }
                        }
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

    private int grandTotal(List<ItemCart> items){
        if(items.size()==0) {
            return 0;
        }
        int totalPrice = 0;
        for(int i = 0 ; i < items.size(); i++) {
            if(items.get(i).getIsCheck()) {
                totalPrice += Integer.parseInt(items.get(i).getPrice()) *  Integer.parseInt(items.get(i).getCount());
            }
        }
        return totalPrice;
    }

    public String convertDot(String no)
    {
        if(no.length()==0) {
            return "";
        }
        Integer no1 = Integer.parseInt(no);
        return  String.format(Locale.US,"%,d", no1).replace(',','.')+ "đ";
    }

    public void openDialog() {
        DialogModal exampleDialog = new DialogModal();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void applyTexts(String username, String phoneNumber, String address) {
        tvName.setText(username);
        tvPhone.setText(phoneNumber);
        tvAddress.setText(address);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}