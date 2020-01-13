import java.util.LinkedList;

public class NaiveBuffer implements Buffer{
    private int size;
    private int freeSize;
    private LinkedList<String> data;

    public NaiveBuffer(int size) {
        this.size = size;
        this.freeSize = size;
        data = new LinkedList<>();
    }

    public synchronized void put (LinkedList<String> dataToPut)
    {
        int dataToPutSize = dataToPut.size();
        while(dataToPutSize > freeSize)
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
            }
            data.addAll(dataToPut);
            freeSize -= dataToPutSize;
            notifyAll();
    }

    public synchronized LinkedList<String> get(int dataSizeToGet)
    {
        LinkedList<String> returnValue = new LinkedList<>();
        while (dataSizeToGet > size - freeSize)
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
            }
            for(int i = 0; i < dataSizeToGet; i++)
            {
                returnValue.add(data.peek());
            }
            freeSize += dataSizeToGet;
            notifyAll();
            return returnValue;
    }
}
