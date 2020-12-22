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
    private int ID;

    public Delete_img(int ID){
        this.ID=ID;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Image from the Database?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

        }
    }
}
