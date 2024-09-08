package ir.jibit;

import com.google.gson.Gson;
import ir.jibit.business_logic.Dispatcher;
import ir.jibit.data_access.ResponseQueue;
import ir.jibit.data_access.db.DataSource;
import ir.jibit.presentation.request.GetMessagesRequest;
import ir.jibit.presentation.request.IntermediaryGetMessageRequest;

import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    private static Dispatcher dispatcher = new Dispatcher();
    private final static Gson gson = new Gson();
    static AtomicInteger counter = new AtomicInteger(0);

    static long start;
    static ExecutorService ex = Executors.newFixedThreadPool(1);

    static int size = 20000;
    static boolean flag = false;
    static Runnable runnable = () -> {
        try {
            var response = ResponseQueue.getInstance().popElement();
            var responseBody = gson.toJson(response.getMessage());
//            System.out.println(counter.get());
            if (counter.incrementAndGet() == size) {
//                System.out.println("size :" +size + " end : " + System.currentTimeMillis());
                System.out.println("took : " + (System.currentTimeMillis() - start));
                counter.set(0);
                flag = true;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    };


    public static void main(String[] args) throws SQLException {
        var x = DataSource.getConnection();
        x.close();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Random random = new Random();


//        ex.execute(() -> {
        for (int j = 0; ; j++) {
//            System.out.println("here");
            if (flag || j == 0) {
                start = System.currentTimeMillis();
//            System.out.println("start : " + start);
                for (int i = 0; i < size+150; i++) {
                    dispatcher.handle(new GetMessagesRequest("POST", new IntermediaryGetMessageRequest(23l, random.nextLong(100000))));
                    flag = false;
                    ex.execute(runnable);
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
//        });
//        System.out.println("producing finished");
    }
}
