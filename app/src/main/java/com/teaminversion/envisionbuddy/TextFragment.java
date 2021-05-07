package com.teaminversion.envisionbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class TextFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_text, container, false);

        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            getActivity().getCurrentFocus().clearFocus();
        }

        TextInputLayout inputLayout = root.findViewById(R.id.filledTextField);
        EditText editText = root.findViewById(R.id.editText);
        inputLayout.setError(null);
        editText.setText(HomeFragment.resultText);
        Button continueButton = root.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()){
                    inputLayout.setError("Field cannot be empty");
                }else {
                    inputLayout.setError(null);
                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    editText.clearFocus();
                    Intent intent = new Intent(getActivity(), ChoiceActivity.class);
                    intent.putExtra("text", editText.getText().toString());
                    startActivity(intent);
                }
            }
        });
        return root;
    }
}