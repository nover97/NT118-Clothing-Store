package app.nover.clothingstore.repos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import app.nover.clothingstore.models.ItemCart;

public class CartRepository {
    private MutableLiveData<List<ItemCart>> mutableCart = new MutableLiveData<>();
    private MutableLiveData<Double> mutableTotalPrice = new MutableLiveData<>();


    public LiveData<List<ItemCart>> getCart() {
        if (mutableCart.getValue() == null) {
            initCart();
        }
        return mutableCart;
    }

    private void initCart() {
        mutableCart.setValue(new ArrayList<ItemCart>());
    }


}

