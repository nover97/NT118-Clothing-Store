package app.nover.clothingstore.login;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.nover.clothingstore.CallbackLoginFragment;
import app.nover.clothingstore.MainActivity;
import app.nover.clothingstore.R;

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
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final String TAG = "Sign In Fragment";
    private EditText emailET, passwordET;
    private ImageView showHidePwIV;
    private Button signInBtn;
    private TextView signUpRedirectText;
    private CallbackLoginFragment callbackLoginFragment;
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
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mAuth = FirebaseAuth.getInstance();

        emailET = view.findViewById(R.id.email_signin);
        passwordET = view.findViewById(R.id.password_signin);
        showHidePwIV = view.findViewById(R.id.showHidePw_signin);
        signInBtn = view.findViewById(R.id.button_signin);
        signUpRedirectText = view.findViewById(R.id.signUpRedirectText);

        signInBtn.setOnClickListener(v -> {

            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();

            if (!isEmailValid() | !isPasswordValid()) {

            } else {
                authorizeUser(email, password);

            }
        });

        signUpRedirectText.setOnClickListener(v -> {
            if (callbackLoginFragment != null) {
                callbackLoginFragment.setSignUpFragment();

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

        return view;
    }

    public void authorizeUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Logged in successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                emailET.setError("Email not exists, please register.");
                                emailET.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                emailET.setError("Invalid credentials, please check.");
                                passwordET.setError("Invalid credentials, please check.");
                                passwordET.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    //
    public boolean isEmailValid() {
        String email = emailET.getText().toString();
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