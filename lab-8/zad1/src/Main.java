import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Main {

    public static void countViaSingleThread(){
        long wordCount = 0;
        try {
            wordCount =
                    Files.lines(Paths.get("pan-tadeusz.txt"))
                            .flatMap(line -> Arrays.stream(line.split("\\s+")))
                            .count();
            System.out.println("Words in text file: " + wordCount);
        } catch (IOException e) {
            System.err.println("An error occured during reading file!");
        }
    }

    public static void countViaMultiThread(){
        long wordCount = 0;
        try {
//            File file = new File(Paths.get("pan-tadeusz.txt"));
//            InputStream stream = new FileInputStream(file);

//            Stream <String> stream = Files.lines(Paths.get("pan-tadeusz.txt"));
            Stream <String> stream = Files.lines(Paths.get("pan-tadeusz.txt"));
            AtomicLong counter = new AtomicLong(0);
            CountTask mainTask = new CountTask(stream, counter, 4);
            Thread mainThread = new Thread(mainTask);
            mainThread.start();
            try {
                mainThread.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            wordCount = counter.get();
            System.out.println("Words in text file: " + wordCount);
        } catch (IOException e) {
            System.err.println("An error occured during reading file!");
        }
    }

    public static void main(String[] args) {
//        countViaSingleThread();
        countViaMultiThread();
    }
}
