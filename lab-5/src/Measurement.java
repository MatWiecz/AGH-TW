import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Measurement {
    private List<Long> timestampStart = new ArrayList<>();
    private List<Long> timestampEnd = new ArrayList<>();

    void startMeasurement() {
        timestampStart.add(System.nanoTime());
    }

    void endMeasurement() {
        timestampEnd.add(System.nanoTime());
    }

    void summary(int threadPool, int taskCount) throws IOException
    {
        List<Long> timestampDiff = new ArrayList<>();
        double average, standardDeviation;
        FileWriter writer = new FileWriter("./results.txt", true);

        Double measurementCount = 10D;
        for(int i = 0; i < measurementCount; i++) {
            timestampDiff.add(timestampEnd.get(i) - timestampStart.get(i));
        }

        average = timestampDiff.stream().reduce(0L, Long::sum) / measurementCount;
        standardDeviation = Math.sqrt(
                timestampDiff
                        .stream()
                        .reduce(0L,
                                (accumulator, element) -> (long)(accumulator + Math.pow(element - average, 2))
                        ) / measurementCount
        );

        writer.append("Threads Number: ")
                .append(String.valueOf(threadPool))
                .append(" Tasks number: ")
                .append(String.valueOf(taskCount))
                .append(" avg: ")
                .append(String.valueOf(average / 1000000000))
                .append("s std: ")
                .append(String.valueOf(standardDeviation / 1000000000))
                .append("s\n\n");

        writer.close();
    }
}