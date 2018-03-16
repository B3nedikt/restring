package com.ice.restring.shadow;

import android.os.AsyncTask;

import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowAsyncTask;

import java.util.concurrent.Executor;

@Implements(AsyncTask.class)
public class MyShadowAsyncTask<Params, Progress, Result> extends ShadowAsyncTask<Params, Progress, Result> {

    @Override
    public AsyncTask<Params, Progress, Result> executeOnExecutor(Executor executor, Params... params) {
        return super.execute(params);
    }
}
