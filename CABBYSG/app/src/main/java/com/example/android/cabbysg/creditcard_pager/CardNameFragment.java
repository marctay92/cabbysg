package com.example.android.cabbysg.creditcard_pager;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.android.cabbysg.R;

import static com.example.android.cabbysg.CreditCardUtils.EXTRA_CARD_HOLDER_NAME;

public class CardNameFragment extends CreditCardFragment {


    private EditText mCardNameView;

    public CardNameFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state) {

        View v = inflater.inflate(R.layout.c_lyt_card_holder_name, group,false);
        mCardNameView = (EditText) v.findViewById(R.id.card_name);

        String name = "";
        if(getArguments() != null && getArguments().containsKey(EXTRA_CARD_HOLDER_NAME)) {
            name = getArguments().getString(EXTRA_CARD_HOLDER_NAME);
        }


        if(name == null) {
            name = "";
        }

        mCardNameView.setText(name);
        mCardNameView.addTextChangedListener(this);

        return v;
    }

    @Override
    public void afterTextChanged(Editable s) {

        onEdit(s.toString());
        if(s.length() == getResources().getInteger(R.integer.card_name_len)) {
            onComplete();
        }
    }

    @Override
    public void focus() {

        if(isAdded()) {
            mCardNameView.selectAll();
        }
    }
}