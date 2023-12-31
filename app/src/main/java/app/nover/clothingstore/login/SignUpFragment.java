package app.nover.clothingstore.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.nover.clothingstore.CallbackLoginFragment;
import app.nover.clothingstore.R;
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
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final String TAG = "Sign Up Fragment";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private CallbackLoginFragment callbackLoginFragment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText fullNameET, emailET, passwordET, password2ET;
    private ImageView showHidePwIV, showHidePw2IV;
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

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
        showHidePwIV = view.findViewById(R.id.showHidePw_signup);
        showHidePw2IV = view.findViewById(R.id.showHidePw2_signup);
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

        showHidePwIV.setOnClickListener(v -> {
            if (passwordET.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                // If visible -> Hide it
                passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                // Change icon
                showHidePwIV.setImageResource(R.drawable.ic_visible_24);
            } else {
                passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showHidePwIV.setImageResource(R.drawable.ic_not_visibile_24);
            }
        });

        showHidePw2IV.setOnClickListener(v -> {
            if (password2ET.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                // If visible -> Hide it
                password2ET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                // Change icon
                showHidePw2IV.setImageResource(R.drawable.ic_visible_24);
            } else {
                password2ET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showHidePw2IV.setImageResource(R.drawable.ic_not_visibile_24);
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
                            UserModel userModel = new UserModel(name, email, password, "https://firebasestorage.googleapis.com/v0/b/clothing-store-c9cdd.appspot.com/o/images%2Fdefault-avatar-profile-icon-vector-social-media-user-image-700-205124837.jpg?alt=media&token=7d22f986-ca6a-45ee-94f3-526c0653ae72", "user","","");
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
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                emailET.setError("Email is already in use.");
                                emailET.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                emailET.setError("Email is invalid.");
                                emailET.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
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
        String email = emailET.getText().toString().trim();
        if (email.isEmpty()) {
            emailET.setError("Email cannot be empty.");
            emailET.requestFocus();
            return false;
        } else if (!isValidEmail(email)) {
            emailET.setError("Not a valid email.");
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
