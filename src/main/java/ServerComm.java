import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ServerComm {
    public static Img_lib makeSearchRequest(SearchParameters pars) throws IOException {
        // Set up the body data
        System.out.println("Sending search to servlet");
        Gson gson = new Gson();
        String jsonString = gson.toJson(pars);
        System.out.println(jsonString);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);

        URL myURL = null;
        try {
            //myURL = new URL("http://localhost:8080/LocalServlet/main");
            myURL = new URL("https://hlabsmedimagedatabase.herokuapp.com/main");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) myURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up the header
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(body.length));
        conn.setDoOutput(true);
        // Write the body of the request
        try (OutputStream outputStream = conn.getOutputStream()) {
            try {
                outputStream.write(body, 0, body.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        inputLine = bufferedReader.readLine();
        bufferedReader.close();
        Gson gson2 = new Gson();
        Img_lib libr = gson2.fromJson(inputLine,Img_lib.class);
        System.out.println(inputLine);
        //libr.Details();
        if (libr.isEmpty()){
            throw new InvalidObjectException("No images found in database");
        }
        return libr;
    }

    protected static void makeDeleteRequest(Img deleteImage) throws IOException {
        // Set up the body data
        Gson gson = new Gson();
        String jsonString = gson.toJson(deleteImage);
        System.out.println(jsonString);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);

        URL myURL = null;
        try {
            //myURL = new URL("http://localhost:8080/LocalServlet/delete");
            myURL = new URL("https://hlabsmedimagedatabase.herokuapp.com/delete");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) myURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up the header
        try {
            conn.setRequestMethod("DELETE");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(body.length));
        conn.setDoOutput(true);
        // Write the body of the request
        try (OutputStream outputStream = conn.getOutputStream()) {
            try {
                outputStream.write(body, 0, body.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        // Read the body of the response
        //while ((inputLine = bufferedReader.readLine()) != null) { System.out.println(inputLine);
        //System.out.println("     Breakpoint     ");
        //}
        inputLine = bufferedReader.readLine();
        bufferedReader.close();
        System.out.println(inputLine);
    }

    protected static String makeUploadImagePOSTRequest(File file, String name) throws IOException {
        //final String UPLOAD_URL = "http://localhost:8080/LocalServlet/uploadimage";
        final String UPLOAD_URL = "https://hlabsmedimagedatabase.herokuapp.com/uploadimage";
        final int BUFFER_SIZE = 4096;

        // takes file path from first program's argument
        //String filePath = "/Users/lilmaga/Desktop/test.dcm";
        //File uploadFile = new File(filePath);
        System.out.println("File to upload: " + name);

        // creates a HTTP connection
        URL url = new URL(UPLOAD_URL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");
        // sets file name as a HTTP header
        httpConn.setRequestProperty("fileName", name);

        // opens output stream of the HTTP connection for writing data
        OutputStream outputStream = httpConn.getOutputStream();

        // Opens input stream of the file for reading data
        FileInputStream inputStream = new FileInputStream(file);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        System.out.println("Start writing data...");

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        System.out.println("Data was written.");
        outputStream.close();
        inputStream.close();

        // always check HTTP response code from server
        int responseCode = httpConn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // reads server's response
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String response = reader.readLine();
            System.out.println("Server's response: " + response);
            if (response.equals("Duplicate file")) {
                System.out.println("Exception throw");
                throw new InvalidObjectException("Another file on the database already has that name, please choose a unique name");
            }
            else return response;
        } else {
            System.out.println("Server returned non-OK code: " + responseCode);
            throw new ConnectException("Failed to connect to server");
        }

    }

    protected static void makeUploadPostRequest(Img newImage) throws IOException {
        // Set up the body data
        Gson gson = new Gson();
        String jsonString = gson.toJson(newImage);
        System.out.println(jsonString);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);

        URL myURL = null;
        try {
            //myURL = new URL("http://localhost:8080/LocalServlet/upload");
            myURL = new URL("https://hlabsmedimagedatabase.herokuapp.com/upload");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;

        try {
            assert myURL != null;
            conn = (HttpURLConnection) myURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up the header
        try {
            assert conn != null;
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(body.length));
        conn.setDoOutput(true);
        // Write the body of the request
        try (OutputStream outputStream = conn.getOutputStream()) {
            try {
                outputStream.write(body, 0, body.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        // Read the body of the response
        //while ((inputLine = bufferedReader.readLine()) != null) { System.out.println(inputLine);
        //System.out.println("     Breakpoint     ");
        //}
        inputLine = bufferedReader.readLine();
        bufferedReader.close();
        System.out.println(inputLine);
    }
}
