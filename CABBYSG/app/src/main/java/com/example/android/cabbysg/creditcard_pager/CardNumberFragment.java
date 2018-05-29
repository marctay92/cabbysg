package com.example.android.cabbysg.creditcard_pager;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.android.cabbysg.CreditCardUtils;
import com.example.android.cabbysg.R;

import static com.example.android.cabbysg.CreditCardUtils.CARD_NUMBER_FORMAT;
import static com.example.android.cabbysg.CreditCardUtils.CARD_NUMBER_FORMAT_AMEX;
import static com.example.android.cabbysg.CreditCardUtils.EXTRA_CARD_NUMBER;

public class CardNumberFragment extends CreditCardFragment {

    EditText mCardNumberView;

    public CardNumberFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state) {
        View v = inflater.inflate(R.layout.c_lyt_card_number, group, false);
        mCardNumberView = (EditText) v.findViewById(R.id.card_number_field);

        String number = "";

        if (getArguments() != null && getArguments().containsKey(EXTRA_CARD_NUMBER)) {
            number = getArguments().getString(EXTRA_CARD_NUMBER);
        }

        if (number == null) {
            number = "";
        }

        mCardNumberView.setText(number);
        mCardNumberView.addTextChangedListener(this);

        return v;
    }


    @Override
    public void afterTextChanged(Editable s) {
        int cursorPosition = mCardNumberView.getSelectionEnd();
        int previousLength = mCardNumberView.getText().length();

        String cardNumber = CreditCardUtils.handleCardNumber(s.toString());
        int modifiedLength = cardNumber.length();

        mCardNumberView.removeTextChangedListener(this);
        mCardNumberView.setText(cardNumber);
        String rawCardNumber = cardNumber.replace(CreditCardUtils.SPACE_SEPERATOR, "");
        CreditCardUtils.CardType cardType = CreditCardUtils.selectCardType(rawCardNumber);
        int maxLengthWithSpaces = ((cardType == CreditCardUtils.CardType.AMEX_CARD) ? CARD_NUMBER_FORMAT_AMEX : CARD_NUMBER_FORMAT).length();
        mCardNumberView.setSelection(cardNumber.length() > maxLengthWithSpaces ? maxLengthWithSpaces : cardNumber.length());
        mCardNumberView.addTextChangedListener(this);

        if (modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            mCardNumberView.setSelection(cursorPosition);
        }

        onEdit(cardNumber);

        if (rawCardNumber.length() == CreditCardUtils.selectCardLength(cardType)) {
            onComplete();
        }
    }

    @Override
    public void focus() {
        if (isAdded()) {
            mCardNumberView.selectAll();
        }
    }
}