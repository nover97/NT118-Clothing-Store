package app.nover.clothingstore.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.nover.clothingstore.CallbackLoginFragment;
import app.nover.clothingstore.MainActivity;
import app.nover.clothingstore.R;
import app.nover.clothingstore.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements CallbackLoginFragment {

    ActivityLoginBinding binding;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        makeSignInFragment();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
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