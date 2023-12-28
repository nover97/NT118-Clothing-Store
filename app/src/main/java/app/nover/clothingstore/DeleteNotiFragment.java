package app.nover.clothingstore;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.nover.clothingstore.adapter.NotiAdapter;
import app.nover.clothingstore.models.Noti;

public class DeleteNotiFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Noti> notiArrayList;
    private NotiAdapter notiAdapter;
    private FirebaseFirestore db;

    public DeleteNotiFragment() {
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
        View view = inflater.inflate(R.layout.fragment_delete_noti, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        notiArrayList = new ArrayList<>();
        notiAdapter = new NotiAdapter(requireContext(), notiArrayList);

        recyclerView.setAdapter(notiAdapter);

        // Set click listener for the adapter items
        notiAdapter.setOnItemClickListener(new NotiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Noti noti) {
                deleteNotification(noti);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve notifications from Firestore and update the RecyclerView
        retrieveNotifications();
    }

    private void retrieveNotifications() {
        // Thêm snapshot listener vào bộ sưu tập "Notification"
        db.collection("Notification")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Lỗi Firestore", error.getMessage());
                        return;
                    }

                    notiArrayList.clear(); // Xóa danh sách hiện tại

                    for (DocumentSnapshot document : value.getDocuments()) {
                        String documentId = document.getId();
                        Noti noti = document.toObject(Noti.class);
                        noti.setDocumentId(documentId);
                        notiArrayList.add(noti);
                    }

                    notiAdapter.sortDataByTime();
                    notiAdapter.notifyDataSetChanged();
                });
    }

    private void deleteNotification(Noti noti) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận xóa thông báo");
        builder.setMessage("Bạn có chắc chắn muốn xóa thông báo này?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            // Delete the notification from Firestore
            db.collection("Notification")
                    .document(noti.getDocumentId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(requireContext(), "Thông báo đã được xóa", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore error", "Lỗi: " + e.getMessage());
                        Toast.makeText(requireContext(), "Lỗi khi xóa thông báo", Toast.LENGTH_SHORT).show();
                    });
        });
        builder.setNegativeButton("Không", (dialog, which) -> {
            // Do nothing, user canceled the deletion
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}