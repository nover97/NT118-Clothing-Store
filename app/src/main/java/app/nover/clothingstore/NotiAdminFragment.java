package app.nover.clothingstore;

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
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import app.nover.clothingstore.models.Noti;

public class NotiAdminFragment extends Fragment {

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

        // Get the button views from the layout
        Button btnAddNotification = view.findViewById(R.id.btnthemtb);
        Button btnDeleteNotification = view.findViewById(R.id.btnxoatb);

        // Set click listener for the "Thêm thông báo" button
        btnAddNotification.setOnClickListener(v -> addNotificationToFirestore());

        // Set click listener for the "Xóa thông báo" button
        btnDeleteNotification.setOnClickListener(v -> {
            // Replace the current fragment with the "DeleteNoti" fragment
            FragmentTransaction transaction = requireFragmentManager().beginTransaction();
            transaction.replace(R.id.flFragment, new DeleteNotiFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
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
                    Toast.makeText(requireContext(), "Thông báo đã được thêm thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle the failure if adding notification fails
                    Log.e("Firestore error", "Lỗi: " + e.getMessage());
                    Toast.makeText(requireContext(), "Lỗi khi thêm thông báo", Toast.LENGTH_SHORT).show();
                });
    }
}