package dev.b3nedikt.restring.shadow

import android.os.AsyncTask
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowLegacyAsyncTask
import java.util.concurrent.Executor

@Implements(AsyncTask::class)
class MyShadowAsyncTask<Params, Progress, Result> : ShadowLegacyAsyncTask<Params, Progress, Result>() {


    override fun executeOnExecutor(executor: Executor, vararg params: Params): AsyncTask<Params, Progress, Result> {
        return super.execute(*params)
    }

}
