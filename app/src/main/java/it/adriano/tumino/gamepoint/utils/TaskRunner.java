package it.adriano.tumino.gamepoint.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class TaskRunner<I, O> {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public TaskRunner() {
    }

    @SafeVarargs
    public final void startBackground(I... i) {
        executor.execute(() -> {
            O o = doInBackground(i);
            handler.post(() -> onPostExecute(o));
        });
    }

    @SafeVarargs
    public final void execute(I... i) {
        startBackground(i);
    }

    public abstract O doInBackground(I... i);

    public abstract void onPostExecute(O o);
}
