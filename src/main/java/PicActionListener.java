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
    private String location,name,desc, body_part, date;

    public PicActionListener(String Location, String Name, String Description, String Body_Part, String Date){
        this.location=Location;
        this.name=Name;
        this.desc=Description;
        this.body_part=Body_Part;
        this.date=Date;
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
        JLabel nam = new JLabel(" ID: " + name);
        nam.setFont(nam.getFont().deriveFont(20.0f));
        JLabel descr = new JLabel(" Modality: " + desc);
        descr.setFont(descr.getFont().deriveFont(20.0f));
        JLabel body = new JLabel(" Body Part: " + body_part);
        body.setFont(body.getFont().deriveFont(20.0f));
        JLabel date_label = new JLabel(" Date: " + date);
        date_label.setFont(date_label.getFont().deriveFont(20.0f));
        JPanel big_pic = new JPanel();
        JPanel text = new JPanel();
        JButton download = new JButton("Download Image");
        download.addActionListener(new Download_img(location,name));
        JButton delete_button = new JButton("Delete Image");

        big_pic.setSize(750,600);
        pic.setSize(750,600);

        pic.setIcon(new ImageIcon(new ImageIcon(image1).getImage().getScaledInstance(750, 600, Image.SCALE_DEFAULT)));
        big_pic.add(pic);
        text.add(nam);
        text.add(descr);
        text.add(body);
        text.add(date_label);
        text.add(download);
        text.add(delete_button);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        nam.setAlignmentX(Component.LEFT_ALIGNMENT);
        descr.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.setAlignmentX(Component.LEFT_ALIGNMENT);


        text.setBounds(750,0,400,600);


        new_frame.add(big_pic);
        new_frame.add(text);
        new_frame.setSize(1000,600);
        new_frame.setLayout(null);
        new_frame.setVisible(true);
        new_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



    }
}
