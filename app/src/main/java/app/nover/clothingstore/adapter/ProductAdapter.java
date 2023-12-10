package app.nover.clothingstore.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import app.nover.clothingstore.CartFragment;
import app.nover.clothingstore.DetailProduct;
import app.nover.clothingstore.MainActivity;
import app.nover.clothingstore.R;
import app.nover.clothingstore.SplashActivity;
import app.nover.clothingstore.models.ItemModel;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<ItemModel> items;

    public ProductAdapter(List<ItemModel> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName, tvPrice, tvOriginalPrice;
        CardView mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_cardview_image);
            tvPrice = itemView.findViewById(R.id.item_cardview_price);
            tvOriginalPrice = itemView.findViewById(R.id.item_cardview_original_price);
            tvName = itemView.findViewById(R.id.item_cardview_name);
            mainLayout = itemView.findViewById(R.id.main_layout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((LayoutInflater.from(parent.getContext())).inflate(R.layout.item_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name = items.get(position).getName();
        String price = items.get(position).getPrice();
        String oriPrice = items.get(position).getOriginalPrice();
        String url = items.get(position).getImageUrl();
        String description = items.get(position).getDescription();
        String arraySize = items.get(position).getArraySize().toString();
        String arrayColor = items.get(position).getArrayColor().toString();
//        Log.e("AS", Arrays.toString(arraySize));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG1", items.get(position).getArraySize().toString());
                Intent intent = new Intent(holder.mainLayout.getContext(), DetailProduct.class);
                intent.putExtra("name", name);
                intent.putExtra("url", url);
                intent.putExtra("price", price);
                intent.putExtra("oriPrice", oriPrice);
                intent.putExtra("description", description);
                intent.putExtra("arraySize", arraySize);
                intent.putExtra("arrayColor", arrayColor);

//                intent.putStringArrayListExtra("arrayColor", arrayColor);
//                intent.putStringArrayListExtra("arraySize", arraySize);

                holder.mainLayout.getContext().startActivity(intent);
            }
        });


        holder.tvName.setText(name);
        if(price == "") {
            holder.tvPrice.setText( "đ " + convertDot(oriPrice));
//            Log.e("TAG",  String.format(Locale.US,"%,d", oriPrice).replace(',','.'));
        } else {
            holder.tvOriginalPrice.setText("đ " + convertDot(oriPrice));
            holder.tvPrice.setText("đ " +convertDot(price));
        }
        if(url != "") {
            String imageUrl;
            Glide.with(holder.imageView).load(url).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class FetchImage extends Thread {
        String URL;
        Bitmap bitmap;

        FetchImage(String URL) {
            this.URL = URL;
        }

        @Override
        public void run() {

        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
    public String convertDot(String no)
    {
        Integer no1 = Integer.parseInt(no);
            return  String.format(Locale.US,"%,d", no1).replace(',','.');
    }

    public void searchDataList(ArrayList<ItemModel> searchList){
        items = searchList;
        notifyDataSetChanged();
    }

}
