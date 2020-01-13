import java.util.Arrays;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class CountTask implements Runnable {
    private Spliterator<String> split1;
    private Spliterator<String> split2;
    private AtomicLong counter;
    int maxSplitTimes;

    CountTask(Stream<String> fileStream, AtomicLong counter, int maxSplitTimes) {
        split1 = fileStream.spliterator();
        split2 = split1.trySplit();
        this.counter = counter;
        this.maxSplitTimes = maxSplitTimes;
    }

    CountTask(Spliterator<String> textSpliterator, AtomicLong counter, int maxSplitTimes) {
        split1 = textSpliterator;
        split2 = split1.trySplit();
        this.counter = counter;
        this.maxSplitTimes = maxSplitTimes;
    }

    @Override
    public void run() {
        if (maxSplitTimes > 0) {
            CountTask task1 = new CountTask(split1, counter, maxSplitTimes - 1);
            CountTask task2 = new CountTask(split2, counter, maxSplitTimes - 1);
            Thread thread1 = new Thread(task1);
            Thread thread2 = new Thread(task2);
            thread1.start();
            if (split2 != null)
                thread2.start();
            try {
                thread1.join();
                if (split2 != null)
                    thread2.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        } else {
            if (split1 != null)
                split1.forEachRemaining((line) -> {
                    counter.addAndGet(Arrays.stream(line.split("\\s+")).count());
                });
            if (split2 != null)
                split2.forEachRemaining((line) -> {
                    counter.addAndGet(Arrays.stream(line.split("\\s+")).count());
                });
        }
    }
}
