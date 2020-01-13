import java.util.ArrayList;

public class BufferGuard
{
    private int producersNum;
    private int consumersNum;
    private int availableProducers;
    private int availableConsumers;
    private int producersStatus[];
    private int consumersStatus[];
    private int producerReady;
    private int consumerReady;

    BufferGuard(int producersNum, int consumersNum)
    {
        this.producersNum = producersNum;
        this.consumersNum = consumersNum;
        availableProducers = producersNum;
        availableConsumers = consumersNum;
        producersStatus = new int[producersNum];
        consumersStatus = new int[consumersNum];
        producerReady = 0;
        consumerReady = 0;
    }

    public synchronized void tryDoActionByProducer(Producer producer) throws InterruptedException
    {
        while (producersStatus[producer.getId()] != producerReady)
            wait();
        producersStatus[producer.getId()] = producerReady + 1;
        availableProducers--;
        if (availableProducers == 0)
        {
            availableProducers = producersNum;
            producerReady++;
            notifyAll();
        }
    }

    public synchronized void tryDoActionByConsumer(Consumer consumer) throws InterruptedException
    {
        while (consumersStatus[consumer.getId()] != consumerReady)
            wait();
        consumersStatus[consumer.getId()] = consumerReady + 1;
        availableConsumers--;
        if (availableConsumers == 0)
        {
            availableConsumers = consumersNum;
            consumerReady++;
            notifyAll();
        }
    }
}
