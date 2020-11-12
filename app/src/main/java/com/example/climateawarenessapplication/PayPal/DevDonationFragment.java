package com.example.climateawarenessapplication.PayPal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.climateawarenessapplication.R;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class DevDonationFragment extends Fragment {

    private static final int REQUEST_CODE = 1234;


    final String API_GET_TOKEN = "https://shellyonthedole.rossmacd.com";
    final String API_CHECK_OUT = "https://shellyonthedole.rossmacd.com/checkout.php.php";

    //Variables
    private String token, amount;
    private HashMap<String,String> paramsHash;
    private Button btn_pay;
    private EditText edit_amount;
    LinearLayout group_waiting, group_payment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.braintree_dev_donation, container, false);


        //animation setup
        Animation SmallToBig = AnimationUtils.loadAnimation(getActivity(),R.anim.smalltobig);

        //init views
        group_payment = v.findViewById(R.id.payment_group);
        group_payment.setAnimation(SmallToBig);

        group_waiting = v.findViewById(R.id.waiting_group);


        btn_pay = v.findViewById(R.id.btn_pay);
        edit_amount = v.findViewById(R.id.edit_amount);

        new getToken().execute();

        //Event
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();
            }
        });

        return v;
    }

    private void submitPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(getActivity()), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if (!edit_amount.getText().toString().isEmpty()) {

                    amount = edit_amount.getText().toString();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount", amount);
                    paramsHash.put("nonce", strNonce);

                    sendPayments();

                } else {

                    Toast.makeText(getActivity(), "Please Enter A Valid Amount", Toast.LENGTH_SHORT).show();
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
            }

            else {
                Toast.makeText(getActivity(), "Error has occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendPayments() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (response.contains("Successful"))
                    Toast.makeText(getActivity(), "Transaction Successful!", Toast.LENGTH_SHORT).show();

                else {
                    Toast.makeText(getActivity(), "Transaction failed!", Toast.LENGTH_SHORT).show();

                }
                Log.d("TEST_LOG", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TEST_ERROR",error.toString());
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (paramsHash == null)
                    return null;
                Map<String,String> params = new HashMap<>();
                for (String key:paramsHash.keySet())
                {
                    params.put(key, paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);

    }

    private class getToken extends AsyncTask {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog);
            mDialog.setCancelable(false);
            mDialog.setMessage("Please Wait");
            mDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {

                @Override
                public void success(final String responseBody) {

                    if(getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //Hide group waiting
                            group_waiting.setVisibility(View.GONE);
                            //Show group payment
                            group_payment.setVisibility(View.VISIBLE);

                            //set token
                            token = responseBody;
                        }
                    });
                    }
                }

                @Override
                public void failure(Exception exception) {
                    Toast.makeText(getActivity(), "Problem Encountered...", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mDialog.dismiss();
        }
    }
}
