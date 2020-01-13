import java.util.LinkedList;
import java.util.Random;

public class Producer implements Runnable
{
    private int id;
    private static int nextId = 0;
    private int maxProductSize;
    private int productsNum;
    Buffer buffer;
    BufferGuard bufferGuard;

    Producer(int maxProductSize, int productsNum, Buffer buffer)
    {
        id = nextId++;
        this.maxProductSize = maxProductSize;
        this.productsNum = productsNum;
        this.buffer = buffer;
        this.bufferGuard = null;
    }

    Producer(int maxProductSize, int productsNum, Buffer buffer,
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
            LinkedList<String> product = new LinkedList<>();
            for(int i = 0; i < productSize; i++)
            {
                product.add("Part " + i + " of product "+ productId + " of producer " + id + ".");
            }
            long startTime = System.nanoTime();
            if(bufferGuard != null)
                try
                {
                    bufferGuard.tryDoActionByProducer(this);
                } catch (InterruptedException e)
                {
                }
            buffer.put(product);
            Measurement.addMeasurementFromProducer(System.nanoTime() - startTime, productSize);
            System.out.println("Producer " + id + " put product " + productId + " with size " + productSize + ".");
        }
    }
}
