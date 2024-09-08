package ir.jibit.data_access.db;

import ir.jibit.presentation.request.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * This class provides tools to interact with db.
 * It has a queue of requests. It consumes the data in the queue and query the database according to type of each request.
 */
public abstract class DBInteract implements Runnable {

    private Queue<Request> requestsQueue;

    /**
     * Initialize queue
     *
     * @param requestsQueue queue to use
     */
    DBInteract(Queue requestsQueue) {
        this.requestsQueue = requestsQueue;
    }

    /**
     * Add a request to the queue
     *
     * @param request to add
     */
    public void addToQueue(Request request) {
        requestsQueue.add(request);
    }

    /**
     * Get the head of the queue
     *
     * @return head
     */
    public Request fetchHead() {
        return requestsQueue.poll();
    }

    public int queueSize() {
        return requestsQueue.size();
    }

    /**
     * This method executes the query on a list of requests periodically according to time in configGetter.
     * also it will be called whenever it arrives to size threshold in configGetter.
     *
     * @param input list of requests which query will be executed according to them.
     */
    public abstract void executeQuery(List<Request> input, String executeType);

    /**
     * This method returns a specific number of the queue
     *
     * @param size the specified number
     * @return list of items
     */
    public List getNItems(int size) {
        List<Request> list = new ArrayList<>();
        var req = requestsQueue.poll();
        while (req != null && list.size() < size) {
            list.add(req);
            req = fetchHead();
        }
        return list;
    }

    @Override
    public void run() {
        if (!requestsQueue.isEmpty()) {
            int n = requestsQueue.size();
            executeQuery(getNItems(n), "time limit");
        }
    }
}
