package it.adriano.tumino.gamepoint.processes;

public interface AsyncResponse<R> {
    void processFinish(R result);
}
