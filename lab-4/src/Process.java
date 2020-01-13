import java.util.LinkedList;
import java.util.Random;

public class Process<T> implements Runnable
{
    static private int nextId = 1;
    static private Random execTimeRandom = new Random();
    private int id;
    private LinkedList<DataPack<T>> buffer;
    private int execTime;

    Process(LinkedList<DataPack<T>> srcBuffer)
    {
        id = nextId++;
        buffer = srcBuffer;
        execTime = execTimeRandom.nextInt(1000) + 1000;
    }

    @Override
    public void run()
    {
        for (DataPack<T> curPack : buffer)
        {
            curPack.beginEdit(id);
            System.out.println("Process " + id + " processing data (" + execTime + " ms)...");
            try
            {
                Thread.sleep(execTime);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            curPack.endEdit(id);
        }
    }
}
