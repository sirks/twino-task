package twino.util;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

public class CompletableFutureHelper {
    private CompletableFutureHelper() {
    }

    public static <T> CompletableFuture<T> listenableToCompletable(ListenableFuture<T> future) {
        return listenableToCompletable(future, Function.identity());
    }

    public static <T, R> CompletableFuture<R> listenableToCompletable(ListenableFuture<T> future,
            Function<T, R> valueConvertor) {
        CompletableFuture<R> completableFuture = new CompletableFuture<R>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean result = future.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return result;
            }
        };

        future.addCallback(value -> completableFuture.complete(valueConvertor.apply(value)),
                completableFuture::completeExceptionally);
        return completableFuture;
    }

    public static <T> DeferredResult<T> completableToDeferred(CompletableFuture<T> future) {
        DeferredResult<T> deferredResult = new DeferredResult<>();
        future.thenAccept(deferredResult::setResult)
                .exceptionally(ex -> {
                    if (ex instanceof CompletionException) {
                        deferredResult.setErrorResult(ex.getCause());
                    } else {
                        deferredResult.setErrorResult(ex);
                    }
                    return null;
                });
        return deferredResult;
    }
}
