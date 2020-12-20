import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Download_img implements ActionListener {
    private String location,name;

    public Download_img(String Location, String Name){
        this.location=Location;
        this.name=Name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String os=System.getProperty("os.name");
        String win="Win";
        String mac="Mac";
        String short_os = os.substring(0,3);
        if(win.equals(short_os)) {
            try(InputStream in = new URL(location).openStream()){
                String home = System.getProperty("user.home");
                String path=home+"/Downloads/"+name+".jpg";
                Files.copy(in, Paths.get(path));
                JOptionPane.showMessageDialog(null, "The Image has been downloaded to: "+path);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if(mac.equals(short_os)) {
            try(InputStream in = new URL(location).openStream()){
                String home = System.getProperty("user.home");
                String path=home+"/Downloads/"+name+".jpg";
                Files.copy(in, Paths.get(path));
                JOptionPane.showMessageDialog(null, "The Image has been downloaded to: "+path);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


    }
}
