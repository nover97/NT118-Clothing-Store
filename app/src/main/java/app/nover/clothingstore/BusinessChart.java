package app.nover.clothingstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.nover.clothingstore.models.StatusCart;
import app.nover.clothingstore.models.StatusCartComparator;


public class BusinessChart extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    List<Integer> items;
    PieChart pieChart;
    ImageView imBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_chart);

       pieChart = findViewById(R.id.chart);
        firestore = FirebaseFirestore.getInstance();
        fillData();

        imBack = findViewById(R.id.iv_back);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }

    private void fillData() {
        firestore.collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final int[] statusCode1 = {0};
                        final int[] statusCode2 = {0};
                        final int[] statusCode3 = {0};
                        final int[] statusCode45 = {0};
                        final int[] statusCode6 = {0};
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firestore.collection("AddToCheckout")
                                        .document(document.getId()).collection("Users")
                                        .get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if(document.toObject(StatusCart.class).getStatusCode().equals("1")) {
                                                            statusCode1[0]++;
                                                        }
                                                        if(document.toObject(StatusCart.class).getStatusCode().equals("2")) {
                                                            statusCode2[0]++;
                                                        }
                                                        if(document.toObject(StatusCart.class).getStatusCode().equals("3")) {
                                                            statusCode3[0]++;
                                                        }
                                                        if(document.toObject(StatusCart.class).getStatusCode().equals("4") ||
                                                                document.toObject(StatusCart.class).getStatusCode().equals("5")) {
                                                            statusCode45[0]++;
                                                        }
                                                        if(document.toObject(StatusCart.class).getStatusCode().equals("6")) {
                                                            statusCode6[0]++;
                                                        }
                                                    }
                                                    ArrayList<PieEntry> entries = new ArrayList<>();
                                                    entries.add(new PieEntry(statusCode1[0], "Pending" ));
                                                    entries.add(new PieEntry(statusCode2[0], "Delivery" ));
                                                    entries.add(new PieEntry(statusCode3[0], "Confirm" ));
                                                    entries.add(new PieEntry(statusCode45[0], "Cancel" ));
                                                    entries.add(new PieEntry(statusCode6[0], "Success" ));

                                                    PieDataSet pieDataSet = new PieDataSet(entries, "Sub");
                                                    pieDataSet.setColors(Color.BLUE, Color.MAGENTA,Color.GRAY, Color.RED,Color.GREEN);
                                                    pieDataSet.setValueTextSize(20);
                                                    pieChart.setEntryLabelTextSize(20);
                                                    PieData pieData = new PieData(pieDataSet);
                                                    pieChart.setData(pieData);

                                                    pieChart.getDescription().setEnabled(false);
                                                    pieChart.animateY(1000);
                                                    pieChart.invalidate();
                                                } else {
                                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                            }
                        } else {
                            Log.d("123", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}