import com.google.gson.Gson;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Delete_img implements ActionListener {
    private Img img;

    public Delete_img(Img img){
        this.img=img;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Image from the Database?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                makeDeleteRequest(img);
                JOptionPane.showMessageDialog(null, "The image was deleted successfully! Please click 'Search' again on the main window to refresh the search");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    protected static void makeDeleteRequest(Img deleteImage) throws IOException {
        // Set up the body data
        Gson gson = new Gson();
        String jsonString = gson.toJson(deleteImage);
        System.out.println(jsonString);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);

        URL myURL = null;
        try {
            myURL = new URL("http://localhost:8080/LocalServlet/delete");
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
}
