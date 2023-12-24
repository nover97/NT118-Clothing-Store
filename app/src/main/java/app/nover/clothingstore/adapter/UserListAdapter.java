package app.nover.clothingstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.nover.clothingstore.R;
import app.nover.clothingstore.models.UserModel;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private Context context;
    private List<UserModel> userList;
    private Map<Integer, String> documentIdMap; // Danh sách ID tạm thời của từng người dùng
    private OnItemClickListener itemClickListener;

    public UserListAdapter(Context context, List<UserModel> userList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.userList = userList;
        this.itemClickListener = itemClickListener;
        documentIdMap = new HashMap<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.tvName.setText(user.getEmail());

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                String documentId = documentIdMap.get(position); // Lấy ID tương ứng với vị trí
                itemClickListener.onItemClick(documentId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setDocumentId(int position, String documentId) {
        documentIdMap.put(position, documentId); // Lưu trữ ID vào danh sách tạm thời
    }

    public interface OnItemClickListener {
        void onItemClick(String documentId);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvname);
        }
    }
}