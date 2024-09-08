package ir.jibit.data_access;

import ir.jibit.presentation.response.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is used to connect data access layer to presentation layer.
 * It has a queue that data access writes its data on it and presentation consumes that.
 */
public class ResponseQueue {
    private static final ResponseQueue responseQueue = new ResponseQueue();

    private final BlockingQueue<Response> queue;

    List<Long> produces = new ArrayList<>(100000);
    List<Long> consumes = new ArrayList<>(100000);

    private ResponseQueue() {
        queue = new LinkedBlockingQueue<>();
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::showResults,6000,5000, TimeUnit.MILLISECONDS);
    }

    public static ResponseQueue getInstance() {
        return responseQueue;
    }


    public void addElement(Response response) {
        produces.add(System.currentTimeMillis());
        queue.add(response);
    }

    public Response popElement() throws InterruptedException {
        var response = queue.take();
        consumes.add(System.currentTimeMillis());
        return response;
    }

    private void showResults(){
        Arrays.sort(produces.toArray());
        Arrays.sort(consumes.toArray());
        var firstRes = produces.get(0);
        var batch = 1;
        while (firstRes < produces.get(produces.size()-1)){
            int prod = 0;
            int cons = 0;
            for (int i = 0; i < Math.max(produces.size(), consumes.size()); i++) {
                if (i<produces.size() && firstRes <= produces.get(i) && produces.get(i) < firstRes+100){
                    prod++;
                }
                if (i<consumes.size() && consumes.get(i)!=null && firstRes <= consumes.get(i) && consumes.get(i) < firstRes+100){
                    cons++;
                }
            }
            System.out.printf("%d responses was produced and %d responses was consumed in batch %d\n",prod,cons,batch);
            firstRes+=100;
            batch++;
        }
        produces = new ArrayList<>();
        consumes = new ArrayList<>();
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
    }
}
