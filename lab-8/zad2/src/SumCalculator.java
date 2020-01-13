import java.util.concurrent.RecursiveTask;

public class SumCalculator extends RecursiveTask {
    public static final int THRESHOLD = 1000;

    private byte[] numbers;
    private int start;
    private int end;

    public SumCalculator(byte[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public SumCalculator(byte[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    public Long compute() {
        int length = end - start;
        long sum = 0;
        if (length < THRESHOLD) {
            for (int x = start; x < end; x++) {
                sum += numbers[x];
            }
            return sum;
        } else {
            int split = length / 2;
            SumCalculator left = new SumCalculator(numbers, start,
                    start + split);
            left.fork();
            SumCalculator right = new SumCalculator(numbers, start
                    + split, end);
            return right.compute() + (Long)left.join();
        }
    }
}
