package app.nover.clothingstore.modal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import app.nover.clothingstore.R;

public class DialogModal extends AppCompatDialogFragment {

    private EditText editTextUsername;
    private EditText editTextPhoneNumber;
    private EditText editTextAddress;
    private ExampleDialogListener listener;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.modal_form_checkout, null);

        builder.setView(view)
                .setTitle("Edit receiver's information")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = editTextUsername.getText().toString();
                        String phoneNumber = editTextPhoneNumber.getText().toString();
                        String address = editTextAddress.getText().toString();
                        listener.applyTexts(username,phoneNumber,address);
                    }
                });

        editTextUsername = view.findViewById(R.id.edit_username);
        editTextPhoneNumber = view.findViewById(R.id.edit_phone_number);
        editTextAddress = view.findViewById(R.id.edit_address);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String username, String phoneNumber, String address);
    }
}
