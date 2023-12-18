package app.nover.clothingstore;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ImageButton btnCategoryVay = view.findViewById(R.id.btn_category_vay);
        ImageButton btnCategoryPolo = view.findViewById(R.id.btn_category_polo);
        ImageButton btnCategoryJean = view.findViewById(R.id.btn_category_jean);
        ImageButton btnCategoryTShirt = view.findViewById(R.id.btn_category_tshirt);
        ImageButton btnCategoryQuan = view.findViewById(R.id.btn_category_quan);
        ImageButton btnChat = view.findViewById(R.id.btn_chat);

        btnCategoryVay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryFragment("Váy");
            }
        });

        btnCategoryPolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryFragment("Polo");
            }
        });

        btnCategoryJean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryFragment("Jean");
            }
        });

        btnCategoryTShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryFragment("T-Shirt");
            }
        });

        btnCategoryQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryFragment("Quần");
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMessageFragment();
            }
        });

        return view;
    }

    private void openCategoryFragment(String category) {
        CategoryFragment categoryFragment = CategoryFragment.newInstance(category);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, categoryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openMessageFragment() {
        MessageFragment messageFragment = new MessageFragment();

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, messageFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}