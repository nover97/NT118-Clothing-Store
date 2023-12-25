package app.nover.clothingstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        ImageButton btnAboutUs = view.findViewById(R.id.btn_aboutus);
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

//        btnAboutUs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openNotiAdminFragment();
//            }
//        });

//        btnOther.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openMessageListFragment();
//            }
//        });

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

//    private void openNotiAdminFragment() {
//        NotiAdminFragment notiAdminFragment = new NotiAdminFragment();
//
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.flFragment, notiAdminFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }

//    private void openMessageListFragment() {
//        MessageListFragment messageListFragment = new MessageListFragment();
//
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.flFragment, messageListFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
}