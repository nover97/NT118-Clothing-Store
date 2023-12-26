package app.nover.clothingstore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import app.nover.clothingstore.models.UserModel;

public class UpdateInfo extends AppCompatActivity {
    ImageView imBack;
    EditText etName,etPhone, etEmail, etAddress;
    Button btnDelete, btnSave;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        imBack = findViewById(R.id.iv_back);
        etName = findViewById(R.id.et_full_name);
        etPhone = findViewById(R.id.et_phone_number);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        btnDelete = findViewById(R.id.btn_delete);
        btnSave = findViewById(R.id.btn_save);

        eventListenFromDatabase();

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText("");
                etPhone.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleUpdateInfo();
            }
        });




    }

    public void eventListenFromDatabase() {
        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            UserModel user = documentSnapshot.toObject(UserModel.class);
                            etName.setText(user.getFullName());
                            etPhone.setText(user.getPhoneNumber());
                            etEmail.setText(user.getEmail());
                            etAddress.setText(user.getAddress());
                        }
                    }
                });
    }

    public void handleUpdateInfo() {

        if(!isValidMobile(etPhone.getText().toString())) {
            etPhone.setText("");
            Toast.makeText(UpdateInfo.this, "Your phone number is not valid. Please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String,Object> infoUser = new HashMap<>();
        infoUser.put("fullName", etName.getText().toString());
        infoUser.put("phoneNumber", etPhone.getText().toString());
        infoUser.put("address", etAddress.getText().toString());

        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .update(infoUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateInfo.this, "Update information successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidMobile(String phone) {
        if(phone.length() != 10) {
            return false;
        }
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}