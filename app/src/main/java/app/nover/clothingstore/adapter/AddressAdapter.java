package app.nover.clothingstore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import app.nover.clothingstore.AddAddress;
import app.nover.clothingstore.R;
import app.nover.clothingstore.models.AddressModel;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<AddressModel> items;

    public AddressAdapter(List<AddressModel> items) {
        this.items = items;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        EditText etName, etPhoneNumber, etAddress;
        Button btnDelete, btnSave, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            etName = itemView.findViewById(R.id.et_full_name);
            etPhoneNumber = itemView.findViewById(R.id.et_phone_number);
            etAddress = itemView.findViewById(R.id.et_address);
            btnSave = itemView.findViewById(R.id.btn_save);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((LayoutInflater.from(parent.getContext())).inflate(R.layout.address_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name = items.get(position).getFullName();
        String phone = items.get(position).getPhoneNumber();
        String address = items.get(position).getAddressUser();
        String id = items.get(position).getId();

        holder.etName.setText(name);
        holder.etPhoneNumber.setText(phone);
        holder.etAddress.setText(address);

        holder.etName.setEnabled(false);
        holder.etPhoneNumber.setEnabled(false);
        holder.etAddress.setEnabled(false);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.etName.setEnabled(true);
                holder.etPhoneNumber.setEnabled(true);
                holder.etAddress.setEnabled(true);
                holder.btnSave.setEnabled(true);
            }

        });

        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleUpdateAddress(id, holder,position);
                holder.etName.setEnabled(false);
                holder.etPhoneNumber.setEnabled(false);
                holder.etAddress.setEnabled(false);
                holder.btnSave.setEnabled(false);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDeleteAddress(id, holder.etName.getContext(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void handleUpdateAddress(String id, ViewHolder holder, int position){
        HashMap<String,Object> addressUpdate = new HashMap<>();
        addressUpdate.put("fullName", holder.etName.getText().toString());
        addressUpdate.put("phoneNumber", holder.etPhoneNumber.getText().toString());
        addressUpdate.put("addressUser", holder.etAddress.getText().toString());

        items.get(position).setAddressUser(holder.etAddress.getText().toString());
        items.get(position).setFullName(holder.etName.getText().toString());
        items.get(position).setPhoneNumber(holder.etPhoneNumber.getText().toString());

        firestore.collection("AddAddress").document(firebaseAuth.getCurrentUser()
                .getUid()).collection("Users").document(id).update(addressUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(holder.btnSave.getContext(), "Update address successfully!!", Toast.LENGTH_SHORT).show();

                        notifyItemChanged(position);
                    }
                });
    }

    public void handleDeleteAddress(String id, Context context, int position){
        firestore.collection("AddAddress").document(firebaseAuth.getCurrentUser()
                .getUid()).collection("Users").document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, items.size());
                        notifyDataSetChanged();
                        Toast.makeText(context, "Delete address successfully!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
