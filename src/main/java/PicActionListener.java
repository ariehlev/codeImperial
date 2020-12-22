import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PicActionListener implements ActionListener {
    private String location,name,desc, body_part, date, file_name;

    public PicActionListener(String Location, String Name, String Description, String Body_Part, String Date, String File){
        this.location=Location;
        this.name=Name;
        this.desc=Description;
        this.body_part=Body_Part;
        this.date=Date;
        this.file_name = File;
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
        JLabel nam = new JLabel(" Patient: " + name);
        nam.setFont(nam.getFont().deriveFont(20.0f));
        JLabel descr = new JLabel(" Modality: " + desc);
        descr.setFont(descr.getFont().deriveFont(20.0f));
        JLabel body = new JLabel(" Body Part: " + body_part);
        body.setFont(body.getFont().deriveFont(20.0f));
        JLabel date_label = new JLabel(" Date: " + date);
        date_label.setFont(date_label.getFont().deriveFont(20.0f));
        JLabel file_name_label = new JLabel(" File Name: " + file_name);
        file_name_label.setFont(date_label.getFont().deriveFont(20.0f));
        JPanel big_pic = new JPanel();
        JPanel text = new JPanel();
        JButton download = new JButton("Download Image");
        download.addActionListener(new Download_img(location,name));
        JButton delete_button = new JButton("Delete Image");
        delete_button.addActionListener(new Delete_img(location, name));
        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        int image_height = image.getHeight();
        int image_width = image.getWidth();

        if(image_height>1000){
            image_height=(int)(image_height/1.5);
            image_width=(int)(image_width/1.5);
        }
        big_pic.setSize(image_width,image_height);
        pic.setSize(image_width,image_height);

        pic.setIcon(new ImageIcon(new ImageIcon(image1).getImage().getScaledInstance(image_width, image_height, Image.SCALE_DEFAULT)));
        big_pic.add(pic);
        text.add(nam);
        text.add(descr);
        text.add(body);
        text.add(date_label);
        text.add(file_name_label);
        text.add(download);
        text.add(delete_button);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        nam.setAlignmentX(Component.LEFT_ALIGNMENT);
        descr.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.setAlignmentX(Component.LEFT_ALIGNMENT);


        text.setBounds(image_width,0,400,600);


        new_frame.add(big_pic);
        new_frame.add(text);
        new_frame.setSize(image_width+400,image_height+50);
        new_frame.setLayout(null);
        new_frame.setVisible(true);
        new_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



    }
}
