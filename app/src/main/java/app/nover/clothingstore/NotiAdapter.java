package app.nover.clothingstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import app.nover.clothingstore.models.Noti;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Noti> notiArrayList;
    private OnItemClickListener onItemClickListener;
    private SimpleDateFormat sdf;

    public NotiAdapter(Context context, ArrayList<Noti> notiArrayList) {
        this.context = context;
        this.notiArrayList = notiArrayList;
        this.sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_noti, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Noti noti = notiArrayList.get(position);
        holder.title.setText(noti.getTitle());
        holder.content.setText(noti.getContent());
        holder.time.setText(sdf.format(noti.getTime()));

        // Kiểm tra giá trị của onclicked và thiết lập màu nền tương ứng
        if (noti.isOnclicked()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.pink));
        }
    }

    @Override
    public int getItemCount() {
        return notiArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Noti noti);
    }

    public void sortDataByTime() {
        Collections.sort(notiArrayList, new Comparator<Noti>() {
            @Override
            public int compare(Noti noti1, Noti noti2) {
                return noti2.getTime().compareTo(noti1.getTime());
            }
        });
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, content, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvtitle);
            content = itemView.findViewById(R.id.tvcontent);
            time = itemView.findViewById(R.id.tvtime);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                Noti noti = notiArrayList.get(position);
                onItemClickListener.onItemClick(noti);

                // Cập nhật trạng thái onclicked của mục được nhấp thành true
                noti.setOnclicked(true);
                notifyItemChanged(position);
            }
        }
    }
}