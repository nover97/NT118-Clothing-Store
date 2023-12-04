package app.nover.clothingstore.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import app.nover.clothingstore.CartFragment;
import app.nover.clothingstore.DetailProduct;
import app.nover.clothingstore.R;
import app.nover.clothingstore.models.ItemCart;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<ItemCart> items;
    static int totalAmount=0;
    String[] size;
    String[] color;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CartAdapter( List<ItemCart> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName, tvPrice, tvTotal,tvDe,tvIn,tvCount, tvDelete;
        Spinner spinnerColor,spinnerSize;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPrice = itemView.findViewById(R.id.tv_price);
//            tvOriginalPrice = itemView.findViewById(R.id.tv_price_original_item);
            tvName = itemView.findViewById(R.id.tv_name_item);
            tvDe = itemView.findViewById(R.id.ib_decrease);
            tvIn = itemView.findViewById(R.id.ib_increase);
            tvCount = itemView.findViewById(R.id.tv_count);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            imageView = itemView.findViewById(R.id.iv_cart);
            spinnerColor = itemView.findViewById(R.id.spinner_color_cart);
            spinnerSize = itemView.findViewById(R.id.spinner_size_cart);
        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((LayoutInflater.from(parent.getContext())).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemCart itemCart = items.get(position);
        //get data
        String name = items.get(position).getName();
        String price = items.get(position).getPrice();
        String oriPrice = items.get(position).getOriPrice();
        String url = items.get(position).getImageUrl();
        String count = (items.get(position).getCount());
        String colorChose = items.get(position).getColor();
        String arrayColor = items.get(position).getArrayColor();
        String sizeChose = items.get(position).getSize();
        String arraySize = items.get(position).getArraySize();
        String id = items.get(position).getId();

        size = convertStringArray(arraySize,sizeChose);
        color = convertStringArray(arrayColor,colorChose);

        //set data
        holder.tvName.setText(name);
        holder.tvPrice.setText(convertDot(price));
        holder.tvCount.setText(String.valueOf(count));
        Glide.with(holder.imageView).load(url).into(holder.imageView);

        int priceInt = Integer.parseInt(price);
        int countInt =Integer.parseInt(count);

        setTotalAmount(totalAmount+priceInt);

        //handle decrease count
        holder.tvDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(Integer.parseInt(itemCart.getCount()) > 1) {
                 itemCart.setCount(String.valueOf(Integer.parseInt(itemCart.getCount())-1));
                 holder.tvCount.setText(itemCart.getCount());

                 String temp = convertDot(String.valueOf(Integer.parseInt(itemCart.getPrice())-Integer.parseInt(price)));
                 itemCart.setPrice(String.valueOf(Integer.parseInt(itemCart.getPrice())-Integer.parseInt(price)));
                 holder.tvPrice.setText(temp);
             }

            }
        });

        //handle increase count
        holder.tvIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemCart.setCount(String.valueOf(Integer.parseInt(itemCart.getCount())+1));
                holder.tvCount.setText(itemCart.getCount());

                String temp = convertDot(String.valueOf(Integer.parseInt(itemCart.getPrice())+Integer.parseInt(price)));
                itemCart.setPrice(String.valueOf(Integer.parseInt(itemCart.getPrice())+Integer.parseInt(price)));
                holder.tvPrice.setText(temp);
            }
        });



        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("AddToCart")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .collection("Users").document(id)
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(holder.imageView.getContext(), "You successfully deleted this property", Toast.LENGTH_SHORT).show();
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, items.size());
                                notifyDataSetChanged();
                            }
                        });
            }

        });

        //set spinner color
        ArrayAdapter<String> adapterColor = new ArrayAdapter<String>(holder.spinnerColor.getContext(),
                android.R.layout.simple_spinner_item, color);

        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerColor.setAdapter(adapterColor);
        holder.spinnerColor.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        String colorHex = checkColor(item.toString());
                        holder.spinnerColor.setBackgroundColor(Color.parseColor(colorHex));
//                        saveColor = item.toString();

                        //prints the text in spinner item.
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        //set spinner size
        ArrayAdapter<String> adapterSize = new ArrayAdapter<String>(holder.spinnerSize.getContext(),
                android.R.layout.simple_spinner_item, size);

        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerSize.setAdapter(adapterSize);
        holder.spinnerSize.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
//                        String colorHex = checkColor(item.toString());
//                        spinnerColor.setBackgroundColor(Color.parseColor(colorHex));
//                        saveColor = item.toString();

                        //prints the text in spinner item.
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public String convertDot(String no)
    {
        if(no.length()==0) {
            return "";
        }
        Integer no1 = Integer.parseInt(no);
        return  String.format(Locale.US,"%,d", no1).replace(',','.')+ "D";
    }

    public String[] convertStringArray(String sizes, String choose) {
        sizes = sizes.replace(choose+",","");
        sizes = sizes.replace(choose,"");
        sizes = sizes.replace("[", choose + ",");
        sizes = sizes.replace("]", "");
        sizes = sizes.replaceAll(" ", "");
        String[] sizeArray = sizes.split(",");
        return sizeArray;
    }



    public String checkColor(String color) {
        String[] stringColor = {"Yellow","Red","White", "Black"};
        String[] hexColor = {"#ffe040", "#f44336", "#ffffff", "#000000"};
        for(int i =0 ; i < stringColor.length; i++) {
            if(color.equals(stringColor[i])) {
                return hexColor[i];
            }
        }
        return "#ffffff";
    }

    public void deleteItem(int index) {

    }


}

