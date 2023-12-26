package app.nover.clothingstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.nover.clothingstore.login.LoginActivity;
import app.nover.clothingstore.models.UserModel;

public class SplashActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null) {
//            firestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
//                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if(task.isSuccessful()) {
//                                if(task.getResult().toObject(UserModel.class).getRole().equals("admin")) {
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            startActivity(new Intent(SplashActivity.this, AdminActivity.class));
//                                            finish();
//                                        }
//                                    }, 2000);
//                                } else {
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                                            finish();
//                                        }
//                                    }, 2000);
//                                }
//                            }
//                        }
//                    });
//            finish();
//        }
//        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 2000);
//        }

    }
}