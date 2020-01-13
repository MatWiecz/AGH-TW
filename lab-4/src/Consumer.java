import java.util.LinkedList;
import java.util.Random;

public class Consumer implements Runnable
{
    private int id;
    private static int nextId = 0;
    private int maxProductSize;
    private int productsNum;
    Buffer buffer;
    BufferGuard bufferGuard;

    Consumer(int maxProductSize, int productsNum, Buffer buffer)
    {
        id = nextId++;
        this.maxProductSize = maxProductSize;
        this.productsNum = productsNum;
        this.buffer = buffer;
        this.bufferGuard = null;
    }

    Consumer(int maxProductSize, int productsNum, Buffer buffer,
             BufferGuard bufferGuard)
    {
        id = nextId++;
        this.maxProductSize = maxProductSize;
        this.productsNum = productsNum;
        this.buffer = buffer;
        this.bufferGuard = bufferGuard;
    }

    public int getId()
    {
        return id;
    }

    @Override
    public void run() {
        for(int productId = 0; productId < productsNum; productId++)
        {
            Random random = new Random();
            int productSize = random.nextInt(maxProductSize);
            long startTime = System.nanoTime();
            if(bufferGuard != null)
                try
                {
                    bufferGuard.tryDoActionByConsumer(this);
                } catch (InterruptedException e)
                {
                }
            LinkedList<String> product = buffer.get(productSize);
            Measurement.addMeasurementFromConsumer(System.nanoTime() - startTime,
                    productSize);
            System.out.println("Consumer " + id + " get product " + productId + " with size " + productSize + ".");
        }
    }
}
