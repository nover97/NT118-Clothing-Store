package app.nover.clothingstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.nover.clothingstore.R;
import app.nover.clothingstore.adapter.UserListAdapter;
import app.nover.clothingstore.models.UserModel;

public class UserListFragment extends Fragment implements UserListAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private UserListAdapter userAdapter;
    private List<UserModel> userList;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = firestore.collection("Users");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_message_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        userAdapter = new UserListAdapter(getContext(), userList, this);
        recyclerView.setAdapter(userAdapter);

        loadUsers();

        return view;
    }

    private void loadUsers() {
        userCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String documentId = document.getId();
                    UserModel user = document.toObject(UserModel.class);
                    userList.add(user);
                    userAdapter.setDocumentId(userList.size() - 1, documentId); // Cập nhật Firestore ID vào adapter
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(String documentId) {
        // Mở fragment MessageAdminFragment và truyền documentId
        Fragment messageAdminFragment = new MessageAdminFragment();
        Bundle bundle = new Bundle();
        bundle.putString("documentId", documentId);
        messageAdminFragment.setArguments(bundle);

        // Thực hiện chuyển fragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.flFragment, messageAdminFragment)
                .addToBackStack(null)
                .commit();
    }
}