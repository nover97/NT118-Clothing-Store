package app.nover.clothingstore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddNewProduct extends AppCompatActivity {

    EditText etName, etPrice, etDescription, etDisPrice;
    CheckBox cbS, cbM, cbL, cbXL, cbBlack, cbWhite, cbRed, cbYellow;
    Button btnClear, btnAdd;
    Spinner spinnerDiscount, spinnerCategory;
    String[] discountArray, categoryArray;
    ImageView imBack, imAvatarProduct;
    FirebaseFirestore firestore;
    String choseCategory, choseDiscount;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        imAvatarProduct = findViewById(R.id.im_avatar_product);
        imBack = findViewById(R.id.iv_back);
        etName = findViewById(R.id.et_name);
        etPrice = findViewById(R.id.et_price);
        etDescription = findViewById(R.id.et_description);
        etDisPrice = findViewById(R.id.et_dis_price);
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
        discountArray = new String[]{"0", "10", "20", "30", "40", "50"};
        categoryArray = new String[]{"T-Shirt", "Polo", "Dress", "Trousers", "Jean"};

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        registerResult();

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imAvatarProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 3);
                    pickImage();

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if(checkInput()) {
                   handleAddProduct();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               handleClearInput();
            }
        });

        //DiscountSpinner
        spinnerDiscount = (Spinner) findViewById(R.id.spinner_discount);
        ArrayAdapter<String> adapterDiscount = new ArrayAdapter<String>(AddNewProduct.this,
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
                            String price = (100 - Integer.valueOf(choseDiscount))*Integer.valueOf(etPrice.getText().toString())/100+"";
                            etDisPrice.setText(price);
                            Log.e("price", price);
                        }

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        //CategorySpinner
        spinnerCategory = (Spinner) findViewById(R.id.spinner_cate);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(AddNewProduct.this,
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

    public void handleAddProduct() {
        final HashMap<String,Object> product = new HashMap<>();

        List<String> sizeArray = getArrayCheckBox(new CheckBox[]{cbS, cbM, cbL, cbXL});
        List<String> colorArray = getArrayCheckBox(new CheckBox[]{cbWhite, cbBlack, cbYellow, cbRed});
        String id = getAlphaNumericString(20);
        product.put("projectId", id);
        product.put("name", etName.getText().toString());
        product.put("description", etDescription.getText().toString());
        product.put("category",choseCategory);
        product.put("originalPrice", etPrice.getText().toString());
        product.put("arrayColor", colorArray);
        product.put("arraySize", sizeArray);
        if(choseDiscount.equals("0")) {
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
                                            firestore.collection("Products").document(id).set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    handleClearInput();
                                                    Log.e("check", id);

                                                }
                                            });
//
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewProduct.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            firestore.collection("Products").document(id).set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    handleClearInput();
                    Log.e("check", id);

                }
            });
        }


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
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
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
                            Toast.makeText(AddNewProduct.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void handleClearInput() {
        etName.setText("");
        etDescription.setText("");
        etPrice.setText("");
        cbBlack.setChecked(false);
        cbWhite.setChecked(false);
        cbRed.setChecked(false);
        cbYellow.setChecked(false);
        cbS.setChecked(false);
        cbL.setChecked(false);
        cbM.setChecked(false);
        cbXL.setChecked(false);
        imAvatarProduct.setImageURI(null);
        spinnerDiscount.setSelection(0);
        spinnerCategory.setSelection(0);
    }
}