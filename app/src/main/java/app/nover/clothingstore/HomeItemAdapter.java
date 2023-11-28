package app.nover.clothingstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.nover.clothingstore.models.ItemModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeItemAdapter extends ArrayAdapter<ItemModel> {
    private final int layoutResourceId;

    public HomeItemAdapter(Context context, int layoutResourceId, List<ItemModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemHolder();
            holder.itemImageIV = row.findViewById(R.id.item_cardview_image);
            holder.nameTV = row.findViewById(R.id.item_cardview_name);
            holder.priceTV = row.findViewById(R.id.item_cardview_price);
            holder.originalPriceTV = row.findViewById(R.id.item_cardview_original_price);

            row.setTag(holder);
        } else {
            holder = (ItemHolder) row.getTag();
        }

        ItemModel item = getItem(position);

        if (item != null) {
            Picasso.get().load(item.getImageUrl()).into(holder.itemImageIV);
            holder.nameTV.setText(item.getName());
            holder.priceTV.setText(String.valueOf(item.getPrice()));
            holder.originalPriceTV.setText(String.valueOf(item.getOriginalPrice()));
        }

        return row;
    }

    static class ItemHolder {
        ImageView itemImageIV;
        TextView nameTV;
        TextView priceTV;
        TextView originalPriceTV;
    }
}
