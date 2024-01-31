package Server;
import java.util.LinkedList;
import java.util.Queue;

public class LimitedSizeQueue<T> extends LinkedList<T> {
    private final int maxSize;

    public LimitedSizeQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T element) {
        if (size() >= maxSize) {
            // Remove the oldest element if the queue is full
            super.remove();
        }
        return super.add(element);
    }
}