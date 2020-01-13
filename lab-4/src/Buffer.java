import java.util.LinkedList;

public interface Buffer {
    void put (LinkedList<String> dataToPut);
    LinkedList<String> get(int dataSizeToGet);

}
