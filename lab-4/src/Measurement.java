import java.io.FileWriter;
import java.io.IOException;

class Measurement {
    private static double[] measurementCountProducers;
    private static double[] timeSumProducers;
    private static double[] measurementCountConsumers;
    private static double[] timeSumConsumers;
    private static int prodConsNum;
    private static int xRange;

    static void init(int _prodConsNum, int _xRange) {
        prodConsNum = _prodConsNum;
        xRange = _xRange;
        measurementCountProducers = new double[xRange];
        timeSumProducers = new double[xRange];
        measurementCountConsumers = new double[xRange];
        timeSumConsumers = new double[xRange];
    }

    static synchronized void addMeasurementFromProducer(double timeDiff, int productSize) {
        measurementCountProducers[productSize]++;
        timeSumProducers[productSize] += timeDiff;
    }

    static synchronized void addMeasurementFromConsumer(double timeDiff, int productSize) {
        measurementCountConsumers[productSize]++;
        timeSumConsumers[productSize] += timeDiff;
    }

    static synchronized void summary() throws IOException
    {
        FileWriter writer = new FileWriter(
                "./data/" + xRange + "_" + prodConsNum + "_P"
        );

        for(int i = 1; i < xRange; i++) {
            if(measurementCountProducers[i] == 0) {
//                writer.append(String.valueOf(i)).append(" ")
//                        .append(String.valueOf(0))
//                        .append("\n");
            }
            else {
                writer.append(String.valueOf(i)).append(" ")
                        .append(String.valueOf(timeSumProducers[i] / measurementCountProducers[i]))
                        .append("\n");
            }
        }
        writer.close();

        writer = new FileWriter(
                "./data/" + xRange + "_" + prodConsNum + "_C"
        );

        for(int i = 1; i < xRange; i++) {
            if(measurementCountConsumers[i] == 0) {
//                writer.append(String.valueOf(i)).append(" ")
//                        .append(String.valueOf(0))
//                        .append("\n");
            }
            else {
                writer.append(String.valueOf(i)).append(" ")
                        .append(String.valueOf(timeSumConsumers[i] / measurementCountConsumers[i]))
                        .append("\n");
            }
        }

        writer.close();
    }
}