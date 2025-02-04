package org.vse.bookworm.telegram;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WrappedFuture<T, U> implements Future<T> {
    private final T payload;
    private final Future<U> delegate;
    private U unwrapped;

    public WrappedFuture(@NotNull T payload, Future<U> delegate) {
        this.payload = payload;
        this.delegate = delegate;
    }

    @Nullable
    public U unwrapResult() {
        return unwrapped;
    }

    @NotNull
    public T payload() {
        return payload;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        unwrapped = delegate.get();
        return payload;
    }

    @Override
    public T get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        unwrapped = delegate.get(timeout, unit);
        return payload;
    }
}
