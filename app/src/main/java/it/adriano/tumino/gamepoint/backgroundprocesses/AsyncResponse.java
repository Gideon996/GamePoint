package it.adriano.tumino.gamepoint.backgroundprocesses;

public interface AsyncResponse<R> {
    void processFinish(R result);
}
