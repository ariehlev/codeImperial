import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PicActionListener implements ActionListener {
    private String location,name,desc;

    public PicActionListener(String Location, String Name, String Description){
        this.location=Location;
        this.name=Name;
        this.desc=Description;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame new_frame = new JFrame("Enlarged Picture");
        Image image1 = null;
        URL url = null;
        try {
            url = new URL(location);
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        }
        try {
            image1=ImageIO.read(url);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        JLabel pic = new JLabel();
        JLabel nam = new JLabel("ID: " + name);
        JLabel descr = new JLabel("Modality: " + desc);
        JPanel big_pic = new JPanel();
        JPanel text = new JPanel();
        JButton download = new JButton("Download image");
        download.addActionListener(new Download_img(location,name));

        big_pic.setSize(750,600);
        pic.setSize(750,600);

        pic.setIcon(new ImageIcon(new ImageIcon(image1).getImage().getScaledInstance(750, 600, Image.SCALE_DEFAULT)));
        big_pic.add(pic);
        text.add(nam);
        text.add(descr);
        text.add(download);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        nam.setAlignmentX(Component.LEFT_ALIGNMENT);
        descr.setAlignmentX(Component.LEFT_ALIGNMENT);


        text.setBounds(750,0,400,600);


        new_frame.add(big_pic);
        new_frame.add(text);
        new_frame.setSize(1000,600);
        new_frame.setLayout(null);
        new_frame.setVisible(true);
        new_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



    }
}
