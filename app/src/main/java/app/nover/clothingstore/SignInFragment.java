package app.nover.clothingstore;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText emailET, passwordET;
    Button signInBtn;
    TextView signUpRedirectText;
    CallbackLoginFragment callbackLoginFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mAuth = FirebaseAuth.getInstance();

        emailET = view.findViewById(R.id.email_signin);
        passwordET = view.findViewById(R.id.password_signin);
        signInBtn = view.findViewById(R.id.button_signin);
        signUpRedirectText = view.findViewById(R.id.signUpRedirectText);

        signInBtn.setOnClickListener(v -> {

            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();

            if (!isUserValid() | !isPasswordValid()) {

            } else {
//                authorizeUser();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Authen debug", "signInWithEmail:success");
                                    Toast.makeText(getActivity(), "Logged in successfully",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Authen debug", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signUpRedirectText.setOnClickListener(v -> {
            if (callbackLoginFragment != null) {
                callbackLoginFragment.setSignUpFragment();
            }
        });

        return view;
    }

    public void authorizeUser() {
        String Email = emailET.getText().toString().trim();
        String Password = passwordET.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(Email);
        checkUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    emailET.setError(null);
                    String passwordFromDB = snapshot.child(Email).child("password").getValue(String.class);

                    if (passwordFromDB.equals(Password)) {
                        emailET.setError(null);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        passwordET.setError("Invalid password, please try again.");
                        passwordET.requestFocus();
                    }
                } else {
                    emailET.setError("Email does not exist, please sign up.");
                    emailET.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //
    public boolean isUserValid() {
        String value = emailET.getText().toString();
        if (value.isEmpty()) {
            emailET.setError("Email cannot be empty.");
            emailET.requestFocus();
            return false;
        } else {
            emailET.setError(null);
            return true;
        }
    }

    public boolean isPasswordValid() {
        String password = passwordET.getText().toString();
        if (password.contains(" ")) {
            passwordET.setError("Password cannot contain white space.");
            passwordET.requestFocus();
            return false;
        } else if (password.length() < 6) {
            passwordET.setError("Password cannot be less than 6 characters.");
            passwordET.requestFocus();
            return false;
        } else {
            passwordET.setError(null);
            return true;
        }
    }

    public void setCallbackLoginFragment(CallbackLoginFragment clFragment) {
        this.callbackLoginFragment = clFragment;
    }
}