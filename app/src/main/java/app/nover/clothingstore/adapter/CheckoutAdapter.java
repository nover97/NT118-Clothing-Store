package app.nover.clothingstore.adapter;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

import app.nover.clothingstore.R;
import app.nover.clothingstore.models.ItemCart;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    List<ItemCart> items;

    public CheckoutAdapter(List<ItemCart> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = items.get(position).getName();
        String price = items.get(position).getPrice();
        String url = items.get(position).getImageUrl();
        String count = (items.get(position).getCount());
        String colorChose = items.get(position).getColor();
        String sizeChose = items.get(position).getSize();
        String id = items.get(position).getId();
        Boolean check = items.get(position).getIsCheck();

        holder.tvName.setText(name);
        holder.tvColorSize.setText("Color: " + colorChose + ", Size: " + sizeChose);
        holder.tvPrice.setText(convertDot(price));
        holder.tvCount.setText("x" + count);
        Glide.with(holder.imageView).load(url).into(holder.imageView);

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
        ImageView imageView;
        TextView tvName, tvColorSize, tvPrice, tvCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_checkout);
            tvName = itemView.findViewById(R.id.tv_name_checkout);
            tvColorSize = itemView.findViewById(R.id.tv_color_size_checkout);
            tvPrice = itemView.findViewById(R.id.tv_price_checkout);
            tvCount = itemView.findViewById(R.id.tv_count_checkout);

        }
    }
}

