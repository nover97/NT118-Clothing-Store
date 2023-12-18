package app.nover.clothingstore.modal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.nover.clothingstore.R;
import app.nover.clothingstore.adapter.AddressAdapter;
import app.nover.clothingstore.models.AddressModel;

public class DialogAddress extends DialogFragment {

    AddressAdapter adapter;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    List<AddressModel> items;

    public String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an animal");
// add a list
        String[] animals = {"horse", "cow", "camel", "sheep", "goat"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: setData("1"); break;
                    case 1:
                    case 2:
                    case 3: // sheep
                    case 4: // goat
                }
            }
        });
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


        return builder.create();
    }

    private void EventChangeListener() {
        firestore.collection("AddAddress").document(firebaseAuth.getCurrentUser().getUid()).collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("db", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                items.add(dc.getDocument().toObject(AddressModel.class));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
