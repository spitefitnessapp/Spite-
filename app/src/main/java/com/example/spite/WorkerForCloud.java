package com.example.spite;


import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkerForCloud extends Worker {

    private static final String TAG = "Worker";

    public WorkerForCloud(@NonNull Context appContext,@NonNull WorkerParameters workerParams){
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG,"Performing long task");
        return Result.success();
    }
}

