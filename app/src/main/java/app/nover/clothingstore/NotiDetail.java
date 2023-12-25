package app.nover.clothingstore;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotiDetail extends AppCompatActivity {

    private TextView titleTextView;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notidetail);

        titleTextView = findViewById(R.id.tvTitle);
        contentTextView = findViewById(R.id.tvContent);

        if (getIntent() != null) {
            String title = getIntent().getStringExtra("title");
            String content = getIntent().getStringExtra("content");

            if (title != null && content != null) {
                titleTextView.setText(title);
                contentTextView.setText(content);
            }
        }

        ImageButton backButton = findViewById(R.id.backbutton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}