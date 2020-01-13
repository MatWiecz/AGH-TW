public class Consumer implements Runnable
{
    private BoundedBuffer buffer;
    private static int nextId = 1;
    private int id;

    public Consumer(BoundedBuffer buffer)
    {
        id = nextId++;
        this.buffer = buffer;
    }

    public void run()
    {

        for (int i = 0; i < 10000; i++)
        {
            try
            {
                String message = (String) buffer.take();
                System.out.println("Consumer " + id + " took \"" + message + "\"");
                System.out.flush();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
}

