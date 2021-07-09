package it.adriano.tumino.gamepoint.backgroundprocesses;

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
    public final void startBackground(I... input) {
        executor.execute(() -> {
            O output = doInBackground(input);
            handler.post(() -> onPostExecute(output));
        });
    }

    @SafeVarargs
    public final void execute(I... input) {
        startBackground(input);
    }

    public abstract O doInBackground(I... input);

    public abstract void onPostExecute(O output);
}
