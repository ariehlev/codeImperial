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
import java.nio.file.Files;
import java.nio.file.Paths;

public class Delete_img implements ActionListener {
    private Img img;
    public JFrame new_frame;

    public Delete_img(Img img, JFrame new_frame){
        this.img=img;
        this.new_frame = new_frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        Toolkit.getDefaultToolkit().beep();
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Image from the Database?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                ServerComm.makeDeleteRequest(img);
                JOptionPane.showMessageDialog(null, "The image was deleted successfully!");
                Search.searchaction();
                new_frame.dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


}
