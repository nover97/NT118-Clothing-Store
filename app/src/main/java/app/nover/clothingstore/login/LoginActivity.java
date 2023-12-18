package app.nover.clothingstore.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.nover.clothingstore.AdminActivity;
import app.nover.clothingstore.CallbackLoginFragment;
import app.nover.clothingstore.MainActivity;
import app.nover.clothingstore.R;
import app.nover.clothingstore.databinding.ActivityLoginBinding;
import app.nover.clothingstore.models.UserModel;

public class LoginActivity extends AppCompatActivity implements CallbackLoginFragment {

    ActivityLoginBinding binding;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
     FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    String role ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        makeSignInFragment();

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Log.e("e", role);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {


        firestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            role = documentSnapshot.toObject(UserModel.class).getRole();
                            Log.e("e", role);
                           if(role.equals("admin")) {
                               startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                           } else {
                               startActivity(new Intent(LoginActivity.this, MainActivity.class));
                           }
                        }
                    }
                });
        finish();
        }

    }

    public void makeSignInFragment() {
        SignInFragment fragment = new SignInFragment();
        fragment.setCallbackLoginFragment(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_login_fragment, fragment);
        fragmentTransaction.commit();
    }


    public void makeSignUpFragment() {
        SignUpFragment fragment = new SignUpFragment();
        fragment.setCallbackLoginFragment(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fl_login_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void setSignUpFragment() {
        makeSignUpFragment();
    }

    public void setSignInFragment() {
        makeSignInFragment();
    }
}
