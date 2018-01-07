package victor.tarasov.service;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BalkOperationExecutor {

    @SneakyThrows
    public <T, R> List<R> runBalkOperation(List<T> data, Function<T, R> requestOperation) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println();
        List<Callable<R>> callables = data.stream()
                .map(request -> ((Callable<R>) () -> requestOperation.apply(request)))
                .collect(Collectors.toList());
        List<Future<R>> futures = callables.stream().map(executorService::submit).collect(Collectors.toList());
        List<R> responses = Lists.newArrayList();
        for (int i = 0; i < futures.size(); i++) {
            responses.add(futures.get(i).get());
            System.out.printf("\rRequests processed: %d/%d" , i + 1, futures.size());
        }
        System.out.println();
        executorService.shutdown();
        return responses;
    }
}
