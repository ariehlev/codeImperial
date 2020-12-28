package ImageUI;

import Entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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
                Search.searchAction();
                new_frame.dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


}
