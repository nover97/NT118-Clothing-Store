package app.nover.clothingstore;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.OnProgressListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import app.nover.clothingstore.login.ForgotPassword;
import app.nover.clothingstore.login.LoginActivity;
import app.nover.clothingstore.models.UserModel;


public class ProfileFragment extends Fragment {

    Button logoutBtn;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    TextView tvName, tvPending, tvDelivery, tvConfirm,tvEmail, tvHistoryOrder, tvUpdate, tvAddAddress, tvReset;
    String id;
    ImageView imAvatar;
    Uri selectImage;
    int[] isTrue;
    ImageView imPending, imDelivery, imConfirm;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        imAvatar = view.findViewById(R.id.im_avatar);
        logoutBtn = view.findViewById(R.id.logout_btn);
        imPending = view.findViewById(R.id.pending_icon_button);
        imDelivery = view.findViewById(R.id.delivery_icon_bage);
        imConfirm = view.findViewById(R.id.confirm_icon_button);
        tvUpdate = view.findViewById(R.id.tv_update_info);
        tvAddAddress = view.findViewById(R.id.add_address);
        tvReset = view.findViewById(R.id.tv_reset_password);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        tvPending = view.findViewById(R.id.tv_noti_pending);
        tvDelivery = view.findViewById(R.id.tv_noti_delivery);
        tvConfirm = view.findViewById(R.id.tv_noti_confirm);
        tvHistoryOrder = view.findViewById(R.id.tv_history_order);


        try {
            firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String fullName = task.getResult().getString("fullName");
                            String email = task.getResult().getString("email");
                            String url = task.getResult().getString("urlImage");

//                        String phone = task.getResult().getString("Phone");
                            //other stuff

                            tvName.setText(fullName);
                            tvEmail.setText(email);
                            Glide.with(imAvatar).load(url).into(imAvatar);


                        } else {

                        }
                    });
        } catch (Exception e) {
            Log.d("debug", e.getMessage());
        }

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Reset password");
                builder.setMessage("Are you sure reset your password?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String email = documentSnapshot.toObject(UserModel.class).getEmail();
                                resetPassword(email);
                            }
                        });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                builder.show();
            }
        });




        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm Logout");
                builder.setMessage("Are you sure logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                builder.show();

            }

        });

        imAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        imPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PendingCart.class);
                startActivity(intent);
            }
        });

        imDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DeliveryCart.class);
                startActivity(intent);
            }
        });

        imConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ConfirmCart.class);
                startActivity(intent);
            }
        });

        tvHistoryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HistoryOrder.class);
                startActivity(intent);
            }
        });

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateInfo.class);
                startActivity(intent);
            }
        });

        tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddAddress.class);
                startActivity(intent);
            }
        });

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateInfo.class);
                startActivity(intent);
            }
        });

        tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddAddress.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectImage = data.getData();

            StorageReference ref
                    = storageReference
                    .child("images/" + UUID.randomUUID().toString());

            ref.putFile(selectImage)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String fileLink = task.getResult().toString();
                                            Log.e("link", fileLink);
                                            HashMap<String, Object> upload = new HashMap<>();
                                            upload.put("urlImage", fileLink);
                                            firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Glide.with(imAvatar).load(fileLink).into(imAvatar);

                                                }
                                            });
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    public void handleFillInfo() {
        FirebaseFirestore.getInstance()
                .collection("AddToCheckout").document(firebaseAuth.getCurrentUser().getUid()).collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            for (int i = 0; i < myListOfDocuments.size(); i++) {
                                countStatusPending(myListOfDocuments.get(i).getId());
                            }
                        }
                    }
                });
    }


    public void countStatusPending(String id) {
        isTrue = new int[]{0, 0, 0};
        FirebaseFirestore.getInstance()
                .collection("AddToCheckout").document(firebaseAuth.getCurrentUser().getUid()).collection("Users").document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String statusCodes = documentSnapshot.getString("statusCode");
                            if (statusCodes.equals("1")) {
                                isTrue[0] += 1;
                            } else if (statusCodes.equals("2")) {
                                isTrue[1] += 1;
                            } else if (statusCodes.equals("3")) {
                                isTrue[2] += 1;
                            }
                        }

                        if (isTrue[0] > 0) {
                            tvPending.setText(isTrue[0] + "");
                            tvPending.setVisibility(View.VISIBLE);
                        } else {
                            tvPending.setVisibility(View.GONE);
                        }
                        if (isTrue[1] > 0) {
                            tvDelivery.setText(isTrue[1] + "");
                            tvDelivery.setVisibility(View.VISIBLE);
                        } else {
                            tvDelivery.setVisibility(View.GONE);
                        }
                        if (isTrue[2] > 0) {
                            tvConfirm.setText(isTrue[2] + "");
                            tvConfirm.setVisibility(View.VISIBLE);
                        } else {
                            tvConfirm.setVisibility(View.GONE);
                        }

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        handleFillInfo();
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();

    }

    public void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Reset password link has been sent to your registered email", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
