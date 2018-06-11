package com.example.android.cabbysg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cabbysg.creditcard_pager.CardFragmentAdapter;
import com.example.android.cabbysg.creditcard_pager.CardFragmentAdapter.ICardEntryCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.example.android.cabbysg.CreditCardUtils.CARD_NAME_PAGE;
import static com.example.android.cabbysg.CreditCardUtils.EXTRA_CARD_CVV;
import static com.example.android.cabbysg.CreditCardUtils.EXTRA_CARD_EXPIRY;
import static com.example.android.cabbysg.CreditCardUtils.EXTRA_CARD_HOLDER_NAME;
import static com.example.android.cabbysg.CreditCardUtils.EXTRA_CARD_NUMBER;
import static com.example.android.cabbysg.CreditCardUtils.EXTRA_ENTRY_START_PAGE;
import static com.example.android.cabbysg.nav_lostandfound.isNameValid;


public class CardEditActivity extends AppCompatActivity {

    DatabaseReference creditCard_db,rider_db;
    int mLastPageSelected = 0;
    private CreditCardView mCreditCardView;

    private String mCardNumber;
    private String mCVV;
    private String mCardHolderName;
    private String mExpiry;
    private int mStartPage = 0;
    private CardFragmentAdapter mCardAdapter;

    //AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
//new ContextThemeWrapper(getApplication(), R.style.Theme_AppCompat_Dialog_Alert)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_card_edit);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewPager pager = findViewById(R.id.card_field_container_pager);

                int max = pager.getAdapter().getCount();

                if (pager.getCurrentItem() == max - 1) {
                    // if last card.
                    onDoneTapped();
                }else{
                    showNext();
                }
                /*if (pager.getCurrentItem() == 0 && mCardNumber.length()==16 ){
                    if(CreditCardUtils.selectCardType(mCardNumber).toString()=="VISA_CARD" || CreditCardUtils.selectCardType(mCardNumber).toString()=="MASTER_CARD") {
                        showNext();
                    }
                }else if (pager.getCurrentItem() == 0){
                    Toast.makeText(getApplication(),"Please key in valid Master or Visa Card Number",Toast.LENGTH_SHORT).show();
                }
                if (pager.getCurrentItem() == 1 && !mExpiry.e){
                    showNext();
                }else if (pager.getCurrentItem() == 0){
                    Toast.makeText(getApplication(),"Please key in valid Master or Visa Card Number",Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrevious();
            }
        });

        setKeyboardVisibility(true);
        mCreditCardView = findViewById(R.id.credit_card_view);
        Bundle args = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();

        loadPager(args);
        checkParams(args);
    }

    private void checkParams(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        mCardHolderName = bundle.getString(EXTRA_CARD_HOLDER_NAME);
        mCVV = bundle.getString(EXTRA_CARD_CVV);
        mExpiry = bundle.getString(EXTRA_CARD_EXPIRY);
        mCardNumber = bundle.getString(EXTRA_CARD_NUMBER);
        mStartPage = bundle.getInt(EXTRA_ENTRY_START_PAGE);

        final int maxCvvLength = CardSelector.selectCard(mCardNumber).getCvvLength();
        if (mCVV != null && mCVV.length() > maxCvvLength) {
            mCVV = mCVV.substring(0, maxCvvLength);
        }

        mCreditCardView.setCVV(mCVV);
        mCreditCardView.setCardHolderName(mCardHolderName);
        mCreditCardView.setCardExpiry(mExpiry);
        mCreditCardView.setCardNumber(mCardNumber);

        if (mCardAdapter != null) {
            mCreditCardView.post(new Runnable() {
                @Override
                public void run() {
                    mCardAdapter.setMaxCVV(maxCvvLength);
                    mCardAdapter.notifyDataSetChanged();
                }});
        }

        int cardSide =  bundle.getInt(CreditCardUtils.EXTRA_CARD_SHOW_CARD_SIDE, CreditCardUtils.CARD_SIDE_FRONT);
        if (cardSide == CreditCardUtils.CARD_SIDE_BACK) {
            mCreditCardView.showBack();
        }
        if (mStartPage > 0 && mStartPage <= CARD_NAME_PAGE) {
            getViewPager().setCurrentItem(mStartPage);
        }
    }

    public void refreshNextButton() {
        ViewPager pager = findViewById(R.id.card_field_container_pager);

        int max = pager.getAdapter().getCount();

        int text = R.string.next;

        if (pager.getCurrentItem() == max - 1) {
            text = R.string.done;
        }

        ((TextView) findViewById(R.id.next)).setText(text);
    }

    ViewPager getViewPager() {
        return (ViewPager) findViewById(R.id.card_field_container_pager);
    }

    public void loadPager(Bundle bundle) {
        ViewPager pager = getViewPager();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                mCardAdapter.focus(position);

                if ((mCreditCardView.getCardType() != CreditCardUtils.CardType.AMEX_CARD) && (position == 2)) {
                    mCreditCardView.showBack();
                } else if (((position == 1) || (position == 3)) && (mLastPageSelected == 2) && (mCreditCardView.getCardType() != CreditCardUtils.CardType.AMEX_CARD)) {
                    mCreditCardView.showFront();
                }
                mLastPageSelected = position;

                refreshNextButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        pager.setOffscreenPageLimit(4);

        mCardAdapter = new CardFragmentAdapter(getSupportFragmentManager(), bundle);
        mCardAdapter.setOnCardEntryCompleteListener(new ICardEntryCompleteListener() {
            @Override
            public void onCardEntryComplete(int currentIndex) {
                showNext();
            }

            @Override
            public void onCardEntryEdit(int currentIndex, String entryValue) {
                switch (currentIndex) {
                    case 0:
                        mCardNumber = entryValue.replace(CreditCardUtils.SPACE_SEPERATOR, "");
                        mCreditCardView.setCardNumber(mCardNumber);
                        if (mCardAdapter != null) {
                            mCardAdapter.setMaxCVV(CardSelector.selectCard(mCardNumber).getCvvLength());
                        }
                        break;
                    case 1:
                        mExpiry = entryValue;
                        mCreditCardView.setCardExpiry(entryValue);
                        break;
                    case 2:
                        mCVV = entryValue;
                        mCreditCardView.setCVV(entryValue);
                        break;
                    case 3:
                        mCardHolderName = entryValue;
                        mCreditCardView.setCardHolderName(entryValue);
                        break;
                }
            }
        });

        pager.setAdapter(mCardAdapter);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_CARD_CVV, mCVV);
        outState.putString(EXTRA_CARD_HOLDER_NAME, mCardHolderName);
        outState.putString(EXTRA_CARD_EXPIRY, mExpiry);
        outState.putString(EXTRA_CARD_NUMBER, mCardNumber);

        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        checkParams(inState);
    }


    public void showPrevious() {
        final ViewPager pager = findViewById(R.id.card_field_container_pager);
        int currentIndex = pager.getCurrentItem();

        if (currentIndex == 0) {
            setResult(RESULT_CANCELED);
            finish();
        }

        if (currentIndex - 1 >= 0) {
            pager.setCurrentItem(currentIndex - 1);
        }

        refreshNextButton();
    }

    public void showNext() {
        final ViewPager pager = findViewById(R.id.card_field_container_pager);
        CardFragmentAdapter adapter = (CardFragmentAdapter) pager.getAdapter();
            int max = adapter.getCount();
            int currentIndex = pager.getCurrentItem();

            if (currentIndex + 1 < max) {

                pager.setCurrentItem(currentIndex + 1);
            } else {
                // completed the card entry.
                setKeyboardVisibility(false);
            }

            refreshNextButton();
    }

    private void onDoneTapped() {
        final Intent intent = new Intent();

        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();

        creditCard_db= FirebaseDatabase.getInstance().getReference().child("creditCard");
        rider_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(user.getUid()).child("creditCard");

        intent.putExtra(EXTRA_CARD_CVV, mCVV);
        intent.putExtra(EXTRA_CARD_HOLDER_NAME, mCardHolderName);
        intent.putExtra(EXTRA_CARD_EXPIRY, mExpiry);
        intent.putExtra(EXTRA_CARD_NUMBER, mCardNumber);

        //put database
        final Map newPost = new HashMap();
        //newPost.put("email",str_email);
        if(!mCVV.equals("") && mCVV.length()==3 && mCardHolderName!="" && mCardNumber.length()==16 && !mExpiry.equals("")) {
            if ((CreditCardUtils.selectCardType(mCardNumber).toString() == "VISA_CARD" || CreditCardUtils.selectCardType(mCardNumber).toString() == "MASTER_CARD")&& isNameValid(mCardHolderName)) {
                newPost.put("cvv", mCVV);
                newPost.put("cardHolderName", mCardHolderName);
                newPost.put("expiry", mExpiry);
                newPost.put("cardType", mCreditCardView.getCardType());
                rider_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot creditCard : dataSnapshot.getChildren()) {
                                final String cardNum = creditCard.getKey();
                                if (CreditCardUtils.selectCardType(cardNum).toString() == "VISA_CARD" && mCreditCardView.getCardType().toString().equals("VISA_CARD")) {
                                    creditCard_db.child(mCardNumber).updateChildren(newPost);
                                    creditCard_db.child(cardNum).removeValue();
                                    rider_db.child(cardNum).removeValue();
                                    rider_db.child(mCardNumber).setValue(true);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                /*builder.setTitle("VISA card Replacement");
                                builder.setMessage("You have a VISA card in our system. Would you like to replace it?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        creditCard_db.child(mCardNumber).updateChildren(newPost);
                                        creditCard_db.child(cardNum).removeValue();
                                        rider_db.child(cardNum).removeValue();
                                        rider_db.child(mCardNumber).setValue(true);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        return;
                                    }
                                });
                                AlertDialog dialog=builder.create();
                                dialog.show();*/
                                } else if (CreditCardUtils.selectCardType(cardNum).toString() == "MASTER_CARD" && mCreditCardView.getCardType().toString().equals("MASTER_CARD")) {
                                    creditCard_db.child(mCardNumber).updateChildren(newPost);
                                    creditCard_db.child(cardNum).removeValue();
                                    rider_db.child(cardNum).removeValue();
                                    rider_db.child(mCardNumber).setValue(true);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                /*builder.setTitle("Mastercard Replacement");
                                builder.setMessage("You have a Mastercard in our system. Would you like to replace it?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        creditCard_db.child(mCardNumber).updateChildren(newPost);
                                        creditCard_db.child(cardNum).removeValue();
                                        rider_db.child(cardNum).removeValue();
                                        rider_db.child(mCardNumber).setValue(true);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        return;
                                    }
                                });
                                AlertDialog dialog=builder.create();
                                dialog.show();*/
                                }
                            }
                        } else {
                            creditCard_db.child(mCardNumber).updateChildren(newPost);
                            rider_db.child(mCardNumber).setValue(true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            /*creditCard_db.updateChildren(newPost);
            rider_db.child(mCardNumber).setValue(true);
            setResult(RESULT_OK, intent);
            finish();*/
            else {
                Toast.makeText(getApplication(), "Please head previous to add in your details", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplication(), "Please head previous to add in your details", Toast.LENGTH_SHORT).show();
        }
    }

    // from the link above
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            RelativeLayout parent = findViewById(R.id.parent);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) parent.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
            parent.setLayoutParams(layoutParams);
        }
    }

    private void setKeyboardVisibility(boolean visible) {
        final EditText editText = findViewById(R.id.card_number_field);

        if (!visible) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}