import java.awt.image.BufferedImage;

public class ComputeTask
{
    private int status;
    private BufferedImage image;
    private int imageWidth;
    private int imageHeight;
    private int beginIndex;
    private int endIndex;
    private double zoom;
    private int maxIterNum;

    public ComputeTask(BufferedImage image, int imageWidth,
                       int imageHeight, int beginIndex,
                int endIndex, double zoom, int maxIterNum)
    {
        status = 0;
        this.image = image;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.zoom = zoom;
        this.maxIterNum = maxIterNum;
    }

    public synchronized boolean changeStatus(int newStatus)
    {
        if (newStatus == status + 1)
        {
            status = newStatus;
            return true;
        }
        return false;
    }

    public synchronized int getStatus ()
    {
        return status;
    }

    public void execute()
    {
        if (status != 1)
            return;
        int beginX = beginIndex % imageWidth;
        int beginY = beginIndex / imageWidth;
        int endX = endIndex % imageWidth;
        int endY = endIndex / imageWidth;
        double zx, zy, cX, cY, tmp;
        for (int y = beginY; y <= endY; y++)
        {
            for (int x = ((y == beginY) ? beginX : 0); x <= ((y == endY)?endX :
                    imageWidth-1); x++)
            {
                zx = zy = 0;
                cX = (x - imageWidth/2.0) / zoom;
                cY = (y - imageHeight/2.0) / zoom;
                int iter = maxIterNum;
                while (zx * zx + zy * zy < 4 && iter > 0)
                {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                image.setRGB(x, y, iter | (iter << 8));
            }
        }

    }
}
