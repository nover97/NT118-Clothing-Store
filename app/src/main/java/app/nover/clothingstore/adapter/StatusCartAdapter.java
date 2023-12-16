package app.nover.clothingstore.adapter;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import app.nover.clothingstore.DetailOrder;
import app.nover.clothingstore.HomeFragment;
import app.nover.clothingstore.MainActivity;
import app.nover.clothingstore.R;
import app.nover.clothingstore.login.LoginActivity;
import app.nover.clothingstore.models.StatusCart;

public class StatusCartAdapter extends RecyclerView.Adapter<StatusCartAdapter.ViewHolder> implements Serializable {
    List<StatusCart> items;


    public StatusCartAdapter(List<StatusCart> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public StatusCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.status_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusCartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StatusCart item = items.get(position);
        String id = items.get(position).getId();
        String date = items.get(position).getDateCreateAt();
        String time = items.get(position).getTimeCreateAt();
        String payment = items.get(position).getPayment();
        String address = items.get(position).getAddress();
        String name = items.get(position).getName();
        String total = items.get(position).getTotal();
        String phone = items.get(position).getPhoneNumber();
        String status = items.get(position).getStatusCode();

        holder.tvId.setText("ID Order: " + id);
        holder.tvName.setText("Name: " + name);
        holder.tvPhone.setText("Phone number: " + phone);
        holder.tvTotal.setText("Total: " + convertDot(total));
        holder.tvDate.setText("Date: " + date);
        holder.tvTime.setText("Time: " + time);
        holder.tvPayment.setText("Payment: " + payment);
        holder.tvAddress.setText("Address: " + address);

        if (status.equals("1")) {
            holder.tvStatus.setText("Status: Pending");
        } else if (status.equals("2")) {
            holder.btnCancel.setVisibility(View.GONE);
            holder.tvStatus.setText("Status: Delivery");
        } else if (status.equals("3")) {
            holder.btnReceived.setVisibility(View.VISIBLE);
            holder.tvStatus.setText("Status: Success");
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.layout.getContext(), DetailOrder.class);
                intent.putExtra("obj", item.getArrayIdItem().toString());
                intent.putExtra("id", id);
                holder.layout.getContext().startActivity(intent);
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HashMap<String, Object> checkoutMap = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.layout.getContext());
                builder.setTitle("Confirm cancel order");
                builder.setMessage("Are you sure cancel order?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkoutMap.put("dateUpdateAt", formatterDate.format(date));
                        checkoutMap.put("timeUpdateAt", formatterTime.format(date));
                        checkoutMap.put("timestampUpdateAt", tsLong);

                        if (status.equals("1")) {
                            checkoutMap.put("statusCode", "4");
                        }
                        if (status.equals("3")) {
                            checkoutMap.put("statusCode", "5");
                        }

                        holder.firestore.collection("AddToCheckout")
                                .document(holder.firebaseAuth.getCurrentUser().getUid())
                                .collection("Users").document(id).update(checkoutMap);
                        Toast.makeText(holder.layout.getContext(), "Cancel order successfully", Toast.LENGTH_SHORT).show();
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, items.size());
                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                builder.show();
            }
        });

        holder.btnReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HashMap<String, Object> checkoutMap = new HashMap<>();

                Date date = new Date();
                SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();


                AlertDialog.Builder builder = new AlertDialog.Builder(holder.layout.getContext());
                builder.setTitle("Confirm receive");
                builder.setMessage("Are you sure receive?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        checkoutMap.put("dateUpdateAt", formatterDate.format(date));
                        checkoutMap.put("timeUpdateAt", formatterTime.format(date));
                        checkoutMap.put("timestampUpdateAt", tsLong);
                        checkoutMap.put("statusCode", "6");

                        holder.firestore.collection("AddToCheckout")
                                .document(holder.firebaseAuth.getCurrentUser().getUid())
                                .collection("Users").document(id).update(checkoutMap);

                        Toast.makeText(holder.layout.getContext(), "Receive order successfully", Toast.LENGTH_SHORT).show();
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, items.size());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public String convertDot(String no) {
        if (no.length() == 0) {
            return "";
        }
        Integer no1 = Integer.parseInt(no);
        return String.format(Locale.US, "%,d", no1).replace(',', '.') + "đ";
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvDate, tvTime, tvTotal, tvPayment, tvName, tvPhone, tvAddress, tvStatus;
        LinearLayout layout;
        Button btnCancel, btnReceived;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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
            layout = itemView.findViewById(R.id.ln_status_cart);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnReceived = itemView.findViewById(R.id.btn_received);
        }
    }
}
