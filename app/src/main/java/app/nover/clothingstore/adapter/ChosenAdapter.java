package app.nover.clothingstore.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import app.nover.clothingstore.CheckoutActivity;
import app.nover.clothingstore.R;
import app.nover.clothingstore.models.AddressModel;

public class ChosenAdapter extends RecyclerView.Adapter<ChosenAdapter.ViewHolder> {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<AddressModel> items;

    public ChosenAdapter(List<AddressModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((LayoutInflater.from(parent.getContext())).inflate(R.layout.item_chosen_address, parent, false));

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

        holder.btnChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.layout.getContext(), CheckoutActivity.class);
                intent.putExtra("id", id);
                holder.layout.getContext().startActivity(intent);
            }

        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText etName, etPhoneNumber, etAddress;
        Button btnChosen;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            etName = itemView.findViewById(R.id.et_full_name);
            etPhoneNumber = itemView.findViewById(R.id.et_phone_number);
            etAddress = itemView.findViewById(R.id.et_address);
            btnChosen = itemView.findViewById(R.id.btn_chosen);
            layout = itemView.findViewById(R.id.ln_status_cart);


        }
    }


}
