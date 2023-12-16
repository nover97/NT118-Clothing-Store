package app.nover.clothingstore.repos;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ProductRepository {
    private static final String TAG = "ProductRepo";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef;

    public ProductRepository() {
        productRef = db.collection("Products");
    }

    public Task<QuerySnapshot> getAllProduct() {
        return productRef.get().addOnFailureListener(e -> Log.e(TAG, "getAllProduct: ", e));
    }

    public Query getAllProductAlt() {
        return productRef;
    }

    public DocumentReference getProductById(String id) {
        return productRef.document(id);
    }

    public Task<QuerySnapshot> getAllProductsByCategory(DocumentReference category) {
        return productRef.whereEqualTo("category_ref", category).get();
    }
}

