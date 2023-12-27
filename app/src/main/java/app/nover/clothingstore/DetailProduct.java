package app.nover.clothingstore;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import app.nover.clothingstore.models.UserModel;


public class DetailProduct extends AppCompatActivity {

    TextView name, price, oriPrice, description;
    ImageView imageView, imageBack, imageCart;
    String nameIntent, priceIntent, originalPriceIntent, descriptionIntent, imageURLIntent, projectIdIntent;
    Button btnAddCart, btnEdit;
    FirebaseAuth firebaseAuth;
    private Spinner spinnerColor, spinnerSize;
    private String[] color;
    private String[] size;
    private String sizes, colors, saveSize, saveColor;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        getDataFromIntent();
        name = findViewById(R.id.tv_name);
        price = findViewById(R.id.tv_price);
        oriPrice = findViewById(R.id.tv_original_price);
        description = findViewById(R.id.tv_des);
        imageView = findViewById(R.id.iv_detail_product);
        imageBack = findViewById(R.id.iv_back);
        btnAddCart = findViewById(R.id.btn_add_cart);
        btnEdit = findViewById(R.id.btn_edit_product);

        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.toObject(UserModel.class).getRole().equals("admin")) {
                    btnEdit.setVisibility(View.VISIBLE);
                    btnAddCart.setVisibility(View.GONE);
                    Log.e("admin", "admin");
                } else {
                    btnAddCart.setVisibility(View.VISIBLE);
                    btnEdit.setVisibility(View.GONE);
                }
            }
        });

        name.setText(nameIntent);

        if (priceIntent.length() > 0) {
            oriPrice.setText("đ " + convertDot(originalPriceIntent));
            price.setText("đ " + convertDot(priceIntent));
        } else {
            price.setText("đ " + convertDot(originalPriceIntent));
        }

        description.setText("\n\u25CF" + descriptionIntent.replace("\n", "\n\u25CF  "));
        Glide.with(imageView).load(imageURLIntent).into(imageView);

        imageBack.setOnClickListener(view -> {
            firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.toObject(UserModel.class).getRole().equals("admin")) {
                        startActivity(new Intent(DetailProduct.this, AdminActivity.class));
                    } else {
                        startActivity(new Intent(DetailProduct.this, MainActivity.class));

                    }
                }
            });
        });

        //Color spinner
        spinnerColor = (Spinner) findViewById(R.id.spinner_color);
        ArrayAdapter<String> adapterColor = new ArrayAdapter<String>(DetailProduct.this,
                android.R.layout.simple_spinner_item, color);

        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapterColor);
        spinnerColor.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        String colorHex = checkColor(item.toString());
                        spinnerColor.setBackgroundColor(Color.parseColor(colorHex));
                        saveColor = item.toString();

                        //prints the text in spinner item.
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        //Size spinner
        spinnerSize = (Spinner) findViewById(R.id.spinner_size);
        ArrayAdapter<String> adapterSize = new ArrayAdapter<String>(DetailProduct.this,
                android.R.layout.simple_spinner_item, size);

        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapterSize);
        spinnerSize.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        saveSize = item.toString();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        //Handle add item to cart
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProduct.this, EditProduct.class);
                intent.putExtra("projectId", projectIdIntent);
                startActivity(intent);
            }
        });
    }


    public void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nameIntent = bundle.getString("name");
            priceIntent = bundle.getString("price");
            originalPriceIntent = bundle.getString("oriPrice");
            descriptionIntent = bundle.getString("description");
            imageURLIntent = bundle.getString("url");
            sizes = bundle.getString("arraySize");
            size = convertStringArray(sizes);
            colors = bundle.getString("arrayColor");
            color = convertStringArray(colors);
            projectIdIntent = bundle.getString("projectId");

        }
    }

    public String checkColor(String color) {
        String[] stringColor = {"Yellow", "Red", "White", "Black"};
        String[] hexColor = {"#ffe040", "#f44336", "#ffffff", "#000000"};
        for (int i = 0; i < stringColor.length; i++) {
            if (color.equals(stringColor[i])) {
                return hexColor[i];
            }
        }
        return "#ffffff";
    }


    public String convertDot(String no) {
        Integer no1 = Integer.parseInt(no);
        return String.format(Locale.US, "%,d", no1).replace(',', '.') + "đ";
    }

    public String[] convertStringArray(String sizes) {
        sizes = sizes.replace("[", "");
        sizes = sizes.replace("]", "");
        sizes = sizes.replaceAll(" ", "");
        return sizes.split(",");
    }

    public void addToCart() {
        final HashMap<String, Object> cartMap = new HashMap<>();
        String price = "0";
        if (priceIntent.isEmpty()) {
            price = originalPriceIntent;
        } else {
            price = priceIntent;
        }

        cartMap.put("price", price);
        cartMap.put("name", nameIntent);
        cartMap.put("size", saveSize);
        cartMap.put("color", saveColor);
        cartMap.put("count", "1");
        cartMap.put("imageUrl", imageURLIntent);
        cartMap.put("arraySize", sizes);
        cartMap.put("arrayColor", colors);
        String id = getAlphaNumericString(20);
        cartMap.put("id", id);
        cartMap.put("isCheck", false);
        cartMap.put("totalItem", String.valueOf(Integer.valueOf(price) * 1));

        firestore.collection("AddToCart").document(firebaseAuth.getCurrentUser().getUid())
                .collection("Users").document(id).set(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailProduct.this, "Add to cart successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getAlphaNumericString(int n) {

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
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


}