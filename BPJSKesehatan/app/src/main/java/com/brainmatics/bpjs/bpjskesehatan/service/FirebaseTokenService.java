package com.brainmatics.bpjs.bpjskesehatan.service;

import android.os.AsyncTask;
import android.util.Log;

import com.brainmatics.bpjs.bpjskesehatan.db.BpjsDbHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;


public class FirebaseTokenService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseTokenService";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        BpjsDbHelper db = new BpjsDbHelper(this);
        db.simpanFcmToken(refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        new AsyncTask<String, Void, Void>(){

            @Override
            protected Void doInBackground(String... token) {
                BackendService backendService = new BackendService(FirebaseTokenService.this);
                try {
                    Log.d(TAG, "Mengirim token ke server");
                    Log.d(TAG, "Token : "+token[0]);
                    backendService.registrasiToken(token[0]);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d(TAG, "Token tersimpan di server");
            }
        }.execute(token);
    }
}
