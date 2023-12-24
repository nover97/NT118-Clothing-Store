package app.nover.clothingstore;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.nover.clothingstore.adapter.MessageAdminAdapter;
import app.nover.clothingstore.models.Message;

public class MessageAdminFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Message> messageList;
    private MessageAdminAdapter messageAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private ListenerRegistration listenerRegistration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frament_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdminAdapter(requireContext(), (ArrayList<Message>) messageList);
        recyclerView.setAdapter(messageAdapter);

        ImageButton btnAddMessage = view.findViewById(R.id.imageButton);
        btnAddMessage.setOnClickListener(v -> addMessageToFirestore());

        Bundle args = getArguments();
        String documentId = null;
        if (args != null) {
            documentId = args.getString("documentId");
        }

        // Add snapshot listener to the "Message" collection
        listenerRegistration = db.collection("Message")
                .document(documentId)
                .collection("Users")
                .orderBy("time")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore error", error.getMessage());
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Message message = dc.getDocument().toObject(Message.class);
                                messageList.add(message);
                            }
                        }

                        // Notify adapter
                        messageAdapter.sortDataByTime();
                        messageAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the listener when the fragment is destroyed
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    private void addMessageToFirestore() {
        // Get the message content from EditText
        EditText etText = requireView().findViewById(R.id.etText);
        String messageContent = etText.getText().toString().trim();

        // Check if the message content is empty
        if (TextUtils.isEmpty(messageContent)) {
            Toast.makeText(requireContext(), "Vui lòng nhập nội dung tin nhắn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Message object
        Message message = new Message();
        message.setMessageContent(messageContent);
        message.setTime(new Timestamp(new Date()).toDate());
        message.setWho("admin");

        Bundle args = getArguments();
        String documentId = null;
        if (args != null) {
            documentId = args.getString("documentId");
        }

        // Add the message object to Firestore collection "Messages" under the current user's document
        db.collection("Message")
                .document(documentId)
                .collection("Users")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    // Reset the EditText content to empty
                    etText.setText("");

                    // Display success message
                    Toast.makeText(requireContext(), "Đã thêm tin nhắn thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Display error message if the addition fails
                    Toast.makeText(requireContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}