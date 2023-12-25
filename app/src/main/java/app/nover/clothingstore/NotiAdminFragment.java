package app.nover.clothingstore;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.nover.clothingstore.adapter.NotiAdapter;
import app.nover.clothingstore.models.Noti;


public class NotiAdminFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Noti> notiArrayList;
    private NotiAdapter notiAdapter;
    private FirebaseFirestore db;

    public NotiAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize FirebaseFirestore instance
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_notification, container, false);

        // Get the button view from the layout
        Button btnAddNotification = view.findViewById(R.id.btnthemtb);

        // Set click listener for the button
        btnAddNotification.setOnClickListener(v -> addNotificationToFirestore());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db = FirebaseFirestore.getInstance();
        notiArrayList = new ArrayList<>();
        notiAdapter = new NotiAdapter(requireContext(), notiArrayList);

        recyclerView.setAdapter(notiAdapter);

        // Add snapshot listener to the "Notification" collection
        db.collection("Notification")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore error", error.getMessage());
                        return;
                    }

                    notiArrayList.clear(); // Clear the existing list

                    for (DocumentSnapshot document : value.getDocuments()) {
                        String documentId = document.getId();
                        Noti noti = document.toObject(Noti.class);
                        noti.setDocumentId(documentId);
                        notiArrayList.add(noti);
                    }

                    notiAdapter.sortDataByTime();
                    notiAdapter.notifyDataSetChanged();
                });

        notiAdapter.setOnItemClickListener(new NotiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Noti noti) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Xác nhận xóa")
                        .setMessage("Bạn muốn xóa tài liệu?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            String documentId = noti.getDocumentId();
                            if (documentId != null) {
                                DocumentReference docRef = db.collection("Notification").document(documentId);
                                docRef.delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Firestore", "Đã xóa tài liệu thành công: " + documentId);
                                            // Xóa thông báo khỏi danh sách hiển thị
                                            notiArrayList.remove(noti);
                                            notiAdapter.notifyDataSetChanged();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Firestore", "Lỗi khi xóa tài liệu: " + e.getMessage());
                                        });
                            }
                        })
                        .setNegativeButton("Không", (dialog, which) -> {
                            // Do nothing, close the dialog
                        })
                        .show();
            }
        });
    }

    private void addNotificationToFirestore() {
        // Get the message content from EditText
        EditText etContent = requireView().findViewById(R.id.etcontent);
        String content = etContent.getText().toString().trim();

        // Get the title from EditText
        EditText etTitle = requireView().findViewById(R.id.ettitle);
        String title = etTitle.getText().toString().trim();

        // Check if the message content and title are empty
        if (content.isEmpty() || title.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập nội dung và tiêu đề thông báo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new notification object
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("content", content);
        notificationData.put("title", title);
        notificationData.put("time", FieldValue.serverTimestamp());
        notificationData.put("onclicked", false);

        // Add the notification object to Firestore collection "Notification"
        db.collection("Notification")
                .add(notificationData)
                .addOnSuccessListener(documentReference -> {
                    // Reset the EditText content to empty
                    etContent.setText("");
                    etTitle.setText("");
                })
                .addOnFailureListener(e -> {
                    // Handle the failure if adding notification fails
                    Log.e("Firestore error", "Lỗi: " + e.getMessage());
                });
    }
}
