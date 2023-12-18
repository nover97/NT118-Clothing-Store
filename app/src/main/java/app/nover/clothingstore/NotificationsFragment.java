package app.nover.clothingstore;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import app.nover.clothingstore.R;
import app.nover.clothingstore.models.Noti;
import app.nover.clothingstore.NotiAdapter;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Noti> notiArrayList;
    private NotiAdapter notiAdapter;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Waiting");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db = FirebaseFirestore.getInstance();
        notiArrayList = new ArrayList<>();
        notiAdapter = new NotiAdapter(requireContext(), notiArrayList);

        recyclerView.setAdapter(notiAdapter);

        eventChangeListener();

        notiAdapter.setOnItemClickListener(new NotiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Noti noti) {

                String documentId = noti.getDocumentId(); // Lấy ID
                if (documentId != null) {
                    DocumentReference docRef = db.collection("Notification").document(documentId);
                    docRef.update("onclicked", true)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "Trường onclicked đã được cập nhật thành true.");
                                noti.setOnclicked(true);
                                notiAdapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore", "Lỗi khi cập nhật trường onclicked: " + e.getMessage());
                            });
                }

                Intent intent = new Intent(requireContext(), NotiDetail.class);
                intent.putExtra("title", noti.getTitle());
                intent.putExtra("content", noti.getContent());
                startActivity(intent);
            }
        });
    }

    private void eventChangeListener() {
        db.collection("Notification")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            String documentId = dc.getDocument().getId();
                            Noti noti = dc.getDocument().toObject(Noti.class);
                            noti.setDocumentId(documentId);

                            switch (dc.getType()) {
                                case ADDED:
                                    notiArrayList.add(noti);
                                    break;
                                case MODIFIED:
                                    // Tìm và cập nhật thông báo đã thay đổi trong danh sách hiển thị
                                    for (int i = 0; i < notiArrayList.size(); i++) {
                                        Noti temp = notiArrayList.get(i);
                                        if (temp.getDocumentId().equals(documentId)) {
                                            notiArrayList.set(i, noti);
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    // Xóa thông báo không còn tồn tại trong Firestore khỏi danh sách hiển thị
                                    for (int i = 0; i < notiArrayList.size(); i++) {
                                        Noti temp = notiArrayList.get(i);
                                        if (temp.getDocumentId().equals(documentId)) {
                                            notiArrayList.remove(i);
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }

                        notiAdapter.sortDataByTime();
                        notiAdapter.notifyDataSetChanged();

                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }
}