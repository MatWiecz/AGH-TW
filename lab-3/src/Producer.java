public class Producer implements Runnable
{
    private BoundedBuffer buffer;
    private static int nextId = 1;
    private int id;

    public Producer(BoundedBuffer buffer)
    {
        id = nextId++;
        this.buffer = buffer;
    }

    public void run()
    {

        for (int i = 0; i < 100; i++)
        {
            String message = "message " + i + " from Producer " + id;
            try
            {
                buffer.put(message);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            System.out.println("Producer " + id + " put \"" + message + "\"");
            System.out.flush();
        }

    }
}

