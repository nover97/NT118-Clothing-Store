package app.nover.clothingstore;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import app.nover.clothingstore.models.UserModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseDatabase database;
    DatabaseReference reference;
    CallbackLoginFragment callbackLoginFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText fullNameET, emailET, passwordET, password2ET;
    private Button signUpBtn;
    private TextView signInRedirectText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStoreDB;

    // ...
// Initialize Firebase Auth
    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();
        fireStoreDB = FirebaseFirestore.getInstance();

        fullNameET = view.findViewById(R.id.name_signup);
        emailET = view.findViewById(R.id.email_signup);
        passwordET = view.findViewById(R.id.password_signup);
        password2ET = view.findViewById(R.id.password2_signup);
        signInRedirectText = view.findViewById(R.id.signInRedirectText);
        signUpBtn = view.findViewById(R.id.button_signup);

        signUpBtn.setOnClickListener(v -> {
//                    database = FirebaseDatabase.getInstance();
//                    reference = database.getReference("users");
                    String fullName = fullNameET.getText().toString().trim();
                    String email = emailET.getText().toString().trim();
                    String password = passwordET.getText().toString().trim();
                    String password2 = password2ET.getText().toString().trim();
                    if (!isNameValid() | !isEmailValid() | !isPasswordValid()) {

                    } else {
                        registerUser(fullName, email, password);
                    }
                }
        );
        signInRedirectText.setOnClickListener(v -> {
            if (callbackLoginFragment != null) {
                callbackLoginFragment.setSignInFragment();
            }
        });
        return view;
    }

    public void registerUser(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserModel userModel = new UserModel(name, email, password);
                            String uid = task.getResult().getUser().getUid();
                            fireStoreDB.collection("Users").document(uid)
                                    .set(userModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                            Toast.makeText(getActivity(), "UserDate successfully written.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                            Toast.makeText(getActivity(), "UserData failed to write.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            Toast.makeText(getActivity(), "Account successfully created.", Toast.LENGTH_SHORT).show();
                            if (callbackLoginFragment != null)
                                callbackLoginFragment.setSignInFragment();

                        } else {
                            Log.w("Authen debug", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean isPasswordValid() {
        String password = passwordET.getText().toString();
        String password2 = password2ET.getText().toString();
        if (password.contains(" ")) {
            passwordET.setError("Password cannot contain white space.");
            passwordET.requestFocus();
            return false;
        } else if (password.length() < 6) {
            passwordET.setError("Password cannot be less than 6 characters.");
            passwordET.requestFocus();
            return false;
        } else if (!password.equals(password2)) {
            password2ET.setError("Passwords are not the same, please type again.");
            password2ET.requestFocus();
            return false;
        } else {
            passwordET.setError(null);
            return true;
        }
    }

    public boolean isNameValid() {
        String name = fullNameET.getText().toString().trim();
        if (name.isEmpty()) {
            fullNameET.setError("Name cannot be empty.");
            fullNameET.requestFocus();
            return false;
        } else {
            fullNameET.setError(null);
            return true;
        }
    }

    public boolean isEmailValid() {
        String value = emailET.getText().toString().trim();
        if (value.isEmpty()) {
            emailET.setError("Email cannot be empty.");
            emailET.requestFocus();
            return false;
        } else {
            emailET.setError(null);
            return true;
        }
    }

    public void setCallbackLoginFragment(CallbackLoginFragment clFragment) {
        this.callbackLoginFragment = clFragment;
    }
}