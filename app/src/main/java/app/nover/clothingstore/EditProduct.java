package app.nover.clothingstore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import app.nover.clothingstore.models.ItemModel;
import app.nover.clothingstore.models.UserModel;

public class EditProduct extends AppCompatActivity {

    EditText etName, etPrice, etDescription, etDisPrice;
    CheckBox cbS, cbM, cbL, cbXL, cbBlack, cbWhite, cbRed, cbYellow;
    Button btnClear, btnAdd, btnDelete;
    Spinner spinnerDiscount, spinnerCategory;
    String[] discountArray, categoryArray;
    String id;
    ImageView imBack, imAvatarProduct;
    FirebaseFirestore firestore;
    String choseCategory, choseDiscount;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        imAvatarProduct = findViewById(R.id.im_avatar_product);
        imBack = findViewById(R.id.iv_back);
        etName = findViewById(R.id.et_name);
        etPrice = findViewById(R.id.et_price);
        etDisPrice = findViewById(R.id.et_dis_price);
        etDescription = findViewById(R.id.et_description);
        cbS = findViewById(R.id.cb_S);
        cbM = findViewById(R.id.cb_M);
        cbL = findViewById(R.id.cb_L);
        cbXL = findViewById(R.id.cb_XL);
        cbBlack = findViewById(R.id.cb_black);
        cbWhite = findViewById(R.id.cb_white);
        cbRed = findViewById(R.id.cb_red);
        cbYellow = findViewById(R.id.cb_yellow);
        btnAdd = findViewById(R.id.btn_save);
        btnClear = findViewById(R.id.btn_clear);
        btnDelete = findViewById(R.id.btn_delete);
        discountArray = new String[]{"0", "10", "20", "30", "40", "50"};
        categoryArray = new String[]{"T-Shirt", "Polo", "Quần", "Váy", "Jean"};

        registerResult();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("projectId");
        Log.e("id", id);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.toObject(UserModel.class).getRole().equals("admin")) {
                            startActivity(new Intent(EditProduct.this, AdminActivity.class));
                        } else {
                            startActivity(new Intent(EditProduct.this, MainActivity.class));

                        }
                    }
                });
            }
        });

        fillData();

        imAvatarProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spinnerDiscount = (Spinner) findViewById(R.id.spinner_discount);
                ArrayAdapter<String> adapterDiscount = new ArrayAdapter<String>(EditProduct.this,
                        android.R.layout.simple_spinner_item, discountArray);
                adapterDiscount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDiscount.setAdapter(adapterDiscount);
                spinnerDiscount.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                Object item = parent.getItemAtPosition(pos);
                                choseDiscount = item.toString();
                                if(etPrice.getText().toString().isEmpty()) {

                                } else {
                                    if(choseDiscount != "0") {
                                        String price = (100 - Integer.valueOf(choseDiscount)) * Integer.valueOf(etPrice.getText().toString()) / 100 + "";
                                        etDisPrice.setText(price);
                                        Log.e("price", price);
                                    }
                                }
                                Log.e("price", etPrice.getText().toString());


                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
            }
        }, 2000);

        spinnerCategory = (Spinner) findViewById(R.id.spinner_cate);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(EditProduct.this,
                android.R.layout.simple_spinner_item, categoryArray);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);
        spinnerCategory.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        choseCategory = item.toString();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(checkInput()) {
                    handleUpdateProduct();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClearInput();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDeleteProduct();
            }
        });
    }

    private void fillData() {
        firestore.collection("Products").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    ItemModel item = documentSnapshot.toObject(ItemModel.class);
                    etName.setText(item.getName());
                    etPrice.setText(item.getOriginalPrice());
                    etDisPrice.setText(item.getPrice());
                    etDescription.setText(item.getDescription());
                    Glide.with(imAvatarProduct).load(item.getImageUrl()).into(imAvatarProduct);
                    for(String color : item.getArrayColor()) {
                        if(cbBlack.getText().toString().equals(color)) {
                            cbBlack.setChecked(true);
                        }
                        if(cbRed.getText().toString().equals(color)) {
                            cbRed.setChecked(true);
                        }
                        if(cbWhite.getText().toString().equals(color)) {
                            cbWhite.setChecked(true);
                        }
                        if(cbYellow.getText().toString().equals(color)) {
                            cbYellow.setChecked(true);
                        }
                    }
                    for(String size : item.getArraySize()) {
                        if(cbM.getText().toString().equals(size)) {
                            cbM.setChecked(true);
                        }
                        if(cbS.getText().toString().equals(size)) {
                            cbS.setChecked(true);
                        }
                        if(cbL.getText().toString().equals(size)) {
                            cbL.setChecked(true);
                        }
                        if(cbXL.getText().toString().equals(size)) {
                            cbXL.setChecked(true);
                        }
                    }
                    int i = 0;
                    for(String category : categoryArray) {
                        if(category.equals(item.getCategory())) {
                            spinnerCategory.setSelection(i);
                        }
                        i++;

                    }
                }
            }
        });
    }

    public void handleUpdateProduct() {
        final HashMap<String,Object> product = new HashMap<>();

        List<String> sizeArray = getArrayCheckBox(new CheckBox[]{cbS, cbM, cbL, cbXL});
        List<String> colorArray = getArrayCheckBox(new CheckBox[]{cbWhite, cbBlack, cbYellow, cbRed});
        product.put("name", etName.getText().toString());
        product.put("description", etDescription.getText().toString());
        product.put("category",choseCategory);
        product.put("originalPrice", etPrice.getText().toString());
        product.put("arrayColor", colorArray);
        product.put("arraySize", sizeArray);
        if(choseDiscount.equals("0") && etDisPrice.getText().toString().isEmpty()) {
            product.put("price", "");
        } else {
            product.put("price", etDisPrice.getText().toString());

        }

        StorageReference ref
                = storageReference
                .child("Products/"+ UUID.randomUUID().toString());

        if(imageUri != null) {
            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String fileLink = task.getResult().toString();
                                            Log.e("link", fileLink);
                                            product.put("imageUrl", fileLink);
                                            firestore.collection("Products").document(id).update(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    handleClearInput();
                                                    startActivity(new Intent(EditProduct.this, AdminActivity.class));
                                                }
                                            });
//
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProduct.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            firestore.collection("Products").document(id).update(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    handleClearInput();
                    Log.e("check", id);
                    startActivity(new Intent(EditProduct.this, AdminActivity.class));
                }
            });
        }
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            imageUri = result.getData().getData();
                            imAvatarProduct.setImageURI(imageUri);
                            Log.e("uri", imageUri.toString());
                        } catch (Exception e) {
                            Toast.makeText(EditProduct.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    public List<String> getArrayCheckBox(CheckBox[] cb1) {
        List<String> items = new ArrayList<>();
        for(CheckBox cb:cb1) {
            if(cb.isChecked()) {
                items.add(cb.getText().toString());
            }
        }
        return items;
    }

    private void handleClearInput() {
        etName.setText("");
        etDescription.setText("");
        etPrice.setText("");
        cbBlack.setChecked(false);
        cbYellow.setChecked(false);
        cbWhite.setChecked(false);
        cbRed.setChecked(false);
        cbRed.setChecked(false);
        cbS.setChecked(false);
        cbL.setChecked(false);
        cbM.setChecked(false);
        cbXL.setChecked(false);
        imAvatarProduct.setImageURI(null);
        spinnerDiscount.setSelection(0);
        spinnerCategory.setSelection(0);
    }

    public boolean checkInput() {
        if(etName.getText().toString().isEmpty()
                || etPrice.getText().toString().isEmpty()
                ||etDescription.getText().toString().isEmpty()) {
            return false;
        }
        if(!etPrice.getText().toString().matches("^[0-9]+$")) {
            return false;
        }
        if(!cbYellow.isChecked()&& !cbRed.isChecked()&& !cbWhite.isChecked()&& !cbBlack.isChecked()) {
            return false;
        }
        if(!cbM.isChecked()&& !cbL.isChecked()&&!cbS.isChecked()&&!cbXL.isChecked()) {
            return false;
        }
        return true;
    }

    private void handleDeleteProduct() {
        firestore.collection("Products").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(EditProduct.this, AdminActivity.class));
            }
        });
    }

}