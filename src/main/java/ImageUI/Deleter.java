package ImageUI;

import Entities.*;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Deleter implements ActionListener {
    private Img img;
    public JFrame new_frame;

    public Deleter(Img img, JFrame new_frame){
        this.img=img;
        this.new_frame = new_frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //deletes the image from the url of the selected image from the database
        int dialogButton = JOptionPane.YES_NO_OPTION;
        Toolkit.getDefaultToolkit().beep();
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Image from the Database?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                System.out.println("Deleting: " + img.getFileName());
                makeDeleteRequest(img);
                JOptionPane.showMessageDialog(null, "The image was deleted successfully!");
                Searcher.searchAction(); //Does a new search to refresh the interface
                new_frame.dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.out.println("Could not connect to database");
                JOptionPane.showMessageDialog(null, "Could not connect to database, please check your internet connection");
            }
        }
    }

    protected static void makeDeleteRequest(Img deleteImage) throws IOException {
        // Set up the body data
        System.out.println("Sending delete request to server");
        // Sends image data to be deleted from database by converting the Img object of said image to json
        Gson gson = new Gson();
        String jsonString = gson.toJson(deleteImage);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);

        URL myURL = null;
        try {
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
        System.out.println("Delete Request Sent");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine;
        inputLine = bufferedReader.readLine();
        bufferedReader.close();
        System.out.println(inputLine);
    }

}
