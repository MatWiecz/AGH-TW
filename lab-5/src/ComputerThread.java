import java.util.LinkedList;

public class ComputerThread implements Runnable
{
    private LinkedList<ComputeTask> tasksArray;

    public ComputerThread(LinkedList<ComputeTask> tasksArray)
    {
        this.tasksArray = tasksArray;
    }

    @Override
    public void run()
    {
        for(ComputeTask curTask : tasksArray)
        {
            if(curTask.changeStatus(1))
            {
                curTask.execute();
                curTask.changeStatus(2);
            }
        }
    }
}
