package ImageUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Download_img implements ActionListener {
    private String location;

    public Download_img(String Location){
        this.location=Location;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

            try(InputStream in = new URL(location).openStream()){
                String home = System.getProperty("user.home");
                File file_name = new File(location);
                String path=home+"/Downloads/"+file_name.getName();
                File file = new File(path);
                if(file.exists() && !file.isDirectory()) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "The Image already exists at: "+path);
                }
                else{
                    Files.copy(in, Paths.get(path));
                    JOptionPane.showMessageDialog(null, "The Image has been downloaded to: "+path);
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


    }
}
