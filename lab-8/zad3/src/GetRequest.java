import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetRequest {
    private String urlPath;
    private BufferedReader in;

    public GetRequest(String urlPath) {
        this.urlPath = urlPath;

        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/html");

            conn.setRequestMethod("GET");
            //add request header
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            System.out.println("\nSending 'GET' request to URL : " + url);
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
        } catch (
                MalformedURLException e) {

            e.printStackTrace();

        } catch (
                IOException e) {

            e.printStackTrace();

        }
    }

    public String getResponseString() {
        try {
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();

        } catch (
                MalformedURLException e) {

            e.printStackTrace();

        } catch (
                IOException e) {

            e.printStackTrace();

        }
        return "";
    }

    public void getResponse() {
        try {
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());

        } catch (
                MalformedURLException e) {

            e.printStackTrace();

        } catch (
                IOException e) {

            e.printStackTrace();

        }
    }
}