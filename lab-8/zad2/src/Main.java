import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        try {
            byte[] arrayToSum = Files.readAllBytes(Paths.get("../zad1/pan-tadeusz.txt"));
            SumCalculator sumCalculator = new SumCalculator(arrayToSum);
            System.out.println(sumCalculator.compute());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
