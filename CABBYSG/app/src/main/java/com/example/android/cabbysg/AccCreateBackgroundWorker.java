package com.example.android.cabbysg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static android.widget.Toast.LENGTH_SHORT;

public class AccCreateBackgroundWorker extends AsyncTask<String,Void,String> {
    Activity activity;
    AlertDialog.Builder alertDialog;
    ProgressDialog pd;
    VolleyCallback callback;

    AccCreateBackgroundWorker(Activity ctx,VolleyCallback callback) {
        this.activity = ctx;
        this.callback = callback;
        alertDialog = new AlertDialog.Builder(activity)
                                .setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setTitle("Status");
    }
    @Override
    protected String doInBackground(String...params){

        String type = params[0];
        String link = "http://172.20.10.4/" + type + ".php";
        String result = "";
        String line;
        switch (type) {
            //Processing Rider Login
            case "RiderLogin":
                try {
                    String mobileNum = params[1];
                    String password = params[2];
                    URL url = new URL(link);
                    //Open connection to PHP file
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    System.out.println("successful connection");
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    //Get data coming out of your java file
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    System.out.println("successful output stream");
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("mobileNum", "UTF-8") + "=" + URLEncoder.encode(mobileNum, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(post_data);
                    System.out.println("successful write");
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    //receiving from php login
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println(line+ " asdasd");
                        result += line;// receive json object
                        System.out.println(result + "abc");
                    }
                    //Creating the json object from the string line
                    try {
                        result = result.trim();
                        System.out.println(result);
                        final JSONObject json = new JSONObject(result);
                        final boolean success =  json.getBoolean("success");

                        (activity).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success){
                                    String riderID = "";
                                    try {
                                        riderID = json.getString("riderID");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    callback.onSuccessLogin(riderID);
                                }
                                else{
                                    callback.onFailLogin();
                                }

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            /*case "RiderLogin":
                try {
                    String mobileNum = params[1];
                    String password = params[2];
                    URL url = new URL(link);
                    //Open connection to PHP file
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    //Get data coming out of your java file
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("mobileNum", "UTF-8") + "=" + URLEncoder.encode(mobileNum, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    //System.out.println(post_data);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //System.out.println("test");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    //System.out.println("test");
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;*/

            //Accessing to Driver Login page
            case "DriverLogin":
                try {
                    String mobileNum = params[1];
                    String password = params[2];
                    URL url = new URL(link);
                    //Open connection to PHP file
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    System.out.println("successful connection");
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    //Get data coming out of your java file
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    System.out.println("successful output stream");
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("mobileNum", "UTF-8") + "=" + URLEncoder.encode(mobileNum, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(post_data);
                    System.out.println("successful write");
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    //receiving from php login
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println(line+ " asdasd");
                        result += line;// receive json object
                        System.out.println(result + "abc");
                    }
                    //Creating the json object from the string line
                    try {
                        result = result.trim();
                        System.out.println(result);
                        final JSONObject json = new JSONObject(result);
                        final boolean success =  json.getBoolean("success");

                        (activity).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success){
                                    String driverID = "";
                                    try {
                                       driverID = json.getString("driverID");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    callback.onSuccessLogin(driverID);
                                }
                                else{
                                    callback.onFailLogin();
                                }

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "register":
                try {
                    String firstName = params[1];
                    String lastName = params[2];
                    String dob = params[3];
                    String email = params[4];
                    String mobileNb = params[5];
                    String password = params[6];
                    URL url = new URL(link);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    System.out.println("data got out");
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(firstName, "UTF-8") + "&"
                            + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(lastName, "UTF-8") + "&"
                            + URLEncoder.encode("dob", "UTF-8") + "=" + URLEncoder.encode(dob, "UTF-8") + "&"
                            + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                            + URLEncoder.encode("mobileNb", "UTF-8") + "=" + URLEncoder.encode(mobileNb, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    System.out.println("got back result");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    //receiving from php login
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println(line+ " asdasd");
                        result += line;// receive json object
                        System.out.println(result + "abc");
                    }
                    //Creating the json object from the string line
                    try {
                        result = result.trim();
                        System.out.println(result);
                        final JSONObject json = new JSONObject(result);
                        final boolean success =  json.getBoolean("success");

                        (activity).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success){
                                    String riderID = null;
                                    try {
                                        riderID = json.getString("driverID");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    callback.onSuccessLogin(riderID);
                                }
                                else{
                                    String remarks = null;
                                    try {
                                        remarks = json.getString("remarks");
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                    callback.onFailRegister(remarks);
                                }

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    @Override
    protected void onPreExecute(){
        //Create progress dialog
        pd = new ProgressDialog(this.activity, STYLE_SPINNER);
        //Set cancelable to not let users click it away
        pd.setCancelable(false);
        //Set message and show
        pd.setMessage("Please Wait...");
        pd.show();

    }

    @Override
    protected void onPostExecute(String result){
            //To dismiss

            pd.dismiss();

            /*alertDialog.setMessage(result);
            alertDialog.create();
            alertDialog.show();*/
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }
}
