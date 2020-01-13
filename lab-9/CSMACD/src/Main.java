import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends JFrame implements Runnable {

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    private BufferedImage I;
    private Graphics2D G;
    private ConcentricCable concentricCable;

    public Main() throws InterruptedException, ExecutionException, IOException {
        super("CSMA/CD Simulation");
        setBounds(100, 700, 1024, 768);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        G = I.createGraphics();
        G.setBackground(new Color(0));
        concentricCable = new ConcentricCable();
        Thread mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
    public void run() {
        final int hostsNum = 10;
        Random random = new Random();
        LinkedList<Host> hosts = new LinkedList<>();
        for (int i = 0; i < hostsNum; i++)
            hosts.add(new Host(concentricCable, 10 + (120 / (hostsNum - 1)) * i));
        long startTime = System.nanoTime();
        while (true) {
            boolean toBreak = true;
            concentricCable.updateCable();
            for (Host host : hosts)
                host.execute();
//            G.clearRect(0, 0, I.getWidth(), I.getHeight());
//            concentricCable.drawCableSignalLevels(I, 20, 40, 0x00FF00);
//            this.repaint();
//            sleep(10);
            for (Host host : hosts)
                if (!host.toExit())
                    toBreak = false;
            if (toBreak)
                break;
        }
        long stopTIme = System.nanoTime();
        double deltaTime = (stopTIme - startTime) / 1.0e9;
        System.out.println("Time: " + deltaTime);
        for (Host host : hosts)
            host.showSummary();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) throws InterruptedException,
            ExecutionException, IOException {
        Main mainObject = new Main();
        mainObject.setVisible(true);
    }
}
