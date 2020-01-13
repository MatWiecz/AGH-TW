import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException
    {
//        final int bufferSize = 10;
//        final int procNum = 5;
//        LinkedList<DataPack<Integer>> buffer = new LinkedList<>();
//        for(int i = 0; i < bufferSize; i++)
//        {
//            buffer.add(new DataPack<Integer>(i));
//        }
//        ExecutorService executor = Executors.newCachedThreadPool();
//        for(int i = 0; i < procNum; i++)
//            executor.execute(new Process<Integer>(buffer));
        int M = 1000;
        int procConsNum = 10;
        int productsNum = 10000;
        if(args.length >= 2) {
            M = Integer.parseInt(args[0]);
            procConsNum = Integer.parseInt(args[1]);
        }
        Buffer buffer = new NaiveBuffer(2*M);
        BufferGuard bufferGuard = new BufferGuard(procConsNum, procConsNum);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Measurement.init(procConsNum, M);
        for(int i = 0; i < procConsNum; i++) {
//            executorService.execute(new Producer(M, productsNum, buffer));
//            executorService.execute(new Consumer(M, productsNum, buffer));
            executorService.execute(new Producer(M, productsNum, buffer, bufferGuard));
            executorService.execute(new Consumer(M, productsNum, buffer, bufferGuard));
        }
        sleep(10000);
        executorService.shutdownNow();
        Measurement.summary();
        System.exit(0);
    }
}
