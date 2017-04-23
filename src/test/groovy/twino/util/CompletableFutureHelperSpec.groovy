package twino.util

import org.springframework.util.concurrent.ListenableFuture
import spock.lang.Specification

import java.util.concurrent.CompletionException

class CompletableFutureHelperSpec extends Specification {

    def listenableFuture = Mock ListenableFuture

    def "cancels ListenableFuture when CompletableFuture is canceled"() {
        when:
        def completableFuture = CompletableFutureHelper.listenableToCompletable(listenableFuture)
        completableFuture.cancel(true)

        then:
        1 * listenableFuture.cancel(true)
    }

    def "passes value to CompletableFuture when ListenableFuture is completed successfully"() {
        given:
        def value = "vvv"
        listenableFuture.addCallback(_, _) >> { successCallback, failureCallback ->
            successCallback.onSuccess(value)
        }

        when:
        def completableFuture = CompletableFutureHelper.listenableToCompletable(listenableFuture)

        then:
        completableFuture.join() == value
    }

    def "converts and passes value to CompletableFuture when ListenableFuture is completed successfully"() {
        given:
        def value = "vvv"
        listenableFuture.addCallback(_, _) >> { successCallback, failureCallback ->
            successCallback.onSuccess(value)
        }

        when:
        def completableFuture = CompletableFutureHelper.listenableToCompletable(listenableFuture, { it + "aaa" })

        then:
        completableFuture.join() == value + "aaa"
    }

    def "completes CompletableFuture exceptionally when ListenableFuture is completed exceptionally"() {
        given:
        listenableFuture.addCallback(_, _) >> { successCallback, failureCallback ->
            failureCallback.onFailure(new IllegalStateException())
        }

        when:
        CompletableFutureHelper.listenableToCompletable(listenableFuture).join()

        then:
        def ex = thrown CompletionException
        ex.getCause() instanceof IllegalStateException
    }
}
