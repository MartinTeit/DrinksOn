package com.example.drinkson;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.widget.Toast;

import java.util.List;

public class CollectMessagesJobService extends JobIntentService {

    final Handler mHandler = new Handler();

    private static final String TAG = "CollectMessagesJobService";
    private static final int JOB_ID = 2;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Repository repository = new Repository(this);
        List<messages> newMessages;

        int maxCount = intent.getIntExtra("maxCountValue", -1);

        for (int i = 0; i < maxCount; i++) {
            System.out.println("onHandleWork: The number is: " + i);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            newMessages = repository.getAllMyMessages();

            if (!newMessages.isEmpty()){

            }

        }

    }


    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, CollectMessagesJobService.class, JOB_ID, intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        showToast("Job Execution Started");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        showToast("Job Execution Finished");
    }


    // Helper for showing tests
    void showToast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CollectMessagesJobService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
