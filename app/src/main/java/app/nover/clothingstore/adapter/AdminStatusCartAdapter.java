package app.nover.clothingstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import app.nover.clothingstore.AdminProfileFragment;
import app.nover.clothingstore.R;
import app.nover.clothingstore.models.StatusCart;

public class AdminStatusCartAdapter extends RecyclerView.Adapter<AdminStatusCartAdapter.ViewHolder> {

    List<StatusCart> items;

    public AdminStatusCartAdapter(List<StatusCart> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvDate, tvTime, tvTotal, tvPayment, tvName,tvPhone,tvAddress, tvStatus;
        LinearLayout layout;
        Button btnCancel, btnReceived;
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPayment = itemView.findViewById(R.id.tv_payment);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvStatus = itemView.findViewById(R.id.tv_status);
            layout= itemView.findViewById(R.id.ln_status_cart);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnReceived = itemView.findViewById(R.id.btn_received);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.status_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



}
