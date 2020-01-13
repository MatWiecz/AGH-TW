import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String mainURLPath = "http://download.savannah.gnu.org/releases/";
        GetRequest mainRequest = new GetRequest(mainURLPath);
        String mainResponse = mainRequest.getResponseString();
        String [] directoriesDirty = mainResponse.split("<a href=\"");
        ArrayList<String> directories = new ArrayList<>();
        for(String dirDirty : directoriesDirty) {
            if(!dirDirty.contains("/\">"))
                continue;
            String [] splitted = dirDirty.split("/\">");
            directories.add(splitted[0]);
        }

        int requestedDirsNum = 0;
        ArrayList<GetRequest> requests = new ArrayList<>();
        for(String directory : directories){
            GetRequest request = new GetRequest(mainURLPath + directory + "/");
            requests.add(request);
            requestedDirsNum++;
            if(requestedDirsNum == 100)
                break;
        }

        for(GetRequest request : requests){
            System.out.println(request.getResponseString());
        }
    }
}
