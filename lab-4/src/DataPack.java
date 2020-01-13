public class DataPack<T extends Object>
{
    static private int nextId = 1;
    private int id;
    private int modifiedBy;
    private T data;
    private int editOn;

    DataPack(T srcData)
    {
        id = nextId++;
        modifiedBy = 0;
        data = srcData;
        editOn = 0;
    }

    synchronized T beginEdit(int proccessId)
    {
        while (editOn != 0 || modifiedBy + 1 != proccessId)
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        editOn = proccessId;
        System.out.println("Process " + proccessId + " begin editing data" +
                " pack " + id + ".");
        return data;
    }

    synchronized void endEdit(int proccessId)
    {
        if (editOn != proccessId)
            return;
        System.out.println("Process " + proccessId + " end editing data" +
                " pack " + id + ".");
        editOn = 0;
        modifiedBy = proccessId;
        notifyAll();
    }
}
