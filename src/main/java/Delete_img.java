import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Delete_img implements ActionListener {
    private String location,name;

    public Delete_img(String Location, String Name){
        this.location=Location;
        this.name=Name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try(InputStream in = new URL(location).openStream()){
            int dialogButton = JOptionPane.YES_NO_OPTION;

            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Image from the Database?", "WARNING",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // yes option
            } else {
                // no option
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
