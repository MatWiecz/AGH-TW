import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JFrame;

public class Main extends JFrame
{

    private int MAX_ITER = 8000;
    private final double ZOOM = 400;
    private BufferedImage I;

    public Main() throws InterruptedException, ExecutionException, IOException
    {
        super("Mandelbrot Set");
        setBounds(100, 700, 1200, 900);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        ArrayList<Integer> threadsNumArray = new ArrayList<>();
        threadsNumArray.add(1);
        threadsNumArray.add(8);
        threadsNumArray.add(16);
        for(Integer threadsNum : threadsNumArray)
        {
            ArrayList<Integer> tasksNumArray = new ArrayList<>();
            tasksNumArray.add(threadsNum);
            tasksNumArray.add(10 * threadsNum);
            tasksNumArray.add(getWidth() * getHeight());
            for(Integer tasksNum : tasksNumArray)
            {
                int indexRange = getWidth() * getHeight();
                int indexRangeForTask = indexRange / tasksNum;
                Measurement measurement = new Measurement();
                for (int testId = 0; testId < 10; testId++)
                {
                    measurement.startMeasurement();
                    LinkedList<ComputeTask> tasks = new LinkedList<>();
                    for (int i = 0; i < tasksNum; i++)
                    {
                        tasks.add(new ComputeTask(I, getWidth(), getHeight(),
                                i * indexRangeForTask,
                                ((i + 1 == tasksNum) ?
                                        (indexRange) : ((i + 1) * indexRangeForTask)) - 1,
                                ZOOM, MAX_ITER));
                    }
                    ExecutorService executor = Executors.newFixedThreadPool(threadsNum);
                    LinkedList<Future> futureThreads = new LinkedList<>();
                    for (int i = 0; i < threadsNum; i++)
                    {
                        futureThreads.add(executor.submit(new ComputerThread(tasks)));
                    }

                    for (Future item : futureThreads)
                    {
                        item.get();
                    }
                    executor.shutdown();
                    measurement.endMeasurement();
                }
                measurement.summary(threadsNum, tasksNum);
            }
        }
    }

    @Override
    public void paint(Graphics g)
    {
        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) throws InterruptedException,
            ExecutionException, IOException
    {
        new Main().setVisible(true);
    }
}