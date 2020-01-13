import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MainParallel {

    public static void main(String[] args) {
        String mainURLPath = "http://download.savannah.gnu.org/releases/";
        GetRequest mainRequest = new GetRequest(mainURLPath);
        String mainResponse = mainRequest.getResponseString();
        String[] directoriesDirty = mainResponse.split("<a href=\"");
        ArrayList<String> directories = new ArrayList<>();
        for (String dirDirty : directoriesDirty) {
            if (!dirDirty.contains("/\">"))
                continue;
            String[] splitted = dirDirty.split("/\">");
            directories.add(splitted[0]);
        }

        int requestedDirsNum = 0;
//        ArrayList<Callable<GetRequest>> requestsCalls = new ArrayList<>();
//        ArrayList<GetRequest> requests = new ArrayList<>();
//        for (String directory : directories) {
//            Callable<GetRequest> requestCall = () -> (new GetRequest(mainURLPath + directory + "/"));
//            requestsCalls.add(requestCall);
//            requestedDirsNum++;
//            if (requestedDirsNum == 10)
//                break;
//        }
//        System.out.println("AHA");
//        for (Callable<GetRequest> requestCall : requestsCalls) {
//            try {
//                requests.add(requestCall.call());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("AHA2");
//        for(GetRequest request : requests){
//            System.out.println(request.getResponseString());
//        }
        ArrayList<CompletableFuture<GetRequest>> requestsCalls = new ArrayList<>();
        ArrayList<GetRequest> requests = new ArrayList<>();
        for (String directory : directories) {
            CompletableFuture<GetRequest> requestCall = CompletableFuture.supplyAsync(() ->
                    (new GetRequest(mainURLPath + directory + "/")));
            requestCall.thenAccept(GetRequest::getResponse);
            requestsCalls.add(requestCall);
            requestedDirsNum++;
            if (requestedDirsNum == 100)
                break;
        }
        System.out.println("AHA");
        for (CompletableFuture<GetRequest> requestCall : requestsCalls) {
            try {
                requestCall.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("AHA2");
    }
}
