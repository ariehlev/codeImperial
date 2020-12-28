package ImageUI;

import Entities.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PicActionListener implements ActionListener {
    private String location,patient,modality, body_part, date, file_name;
    private Img img;

    public PicActionListener(Img img){
        this.img = img;
        this.location = img.getImageURL();
        this.patient = img.getFileName();
        this.modality = img.getModality();
        this.body_part = img.getBodyPart();
        this.date = img.getDate();
        this.file_name = img.getFileName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame new_frame = new JFrame("Enlarged Picture");

        URL url = null;
        try {
            url = new URL(location);
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        }
        ImageIcon image2 = new ImageIcon(url);

        JLabel pic = new JLabel();
        JLabel nam = new JLabel(" Patient: " + patient);
        nam.setFont(nam.getFont().deriveFont(20.0f));
        JLabel descr = new JLabel(" Modality: " + modality);
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
        download.addActionListener(new Download_img(location));
        JButton delete_button = new JButton("Delete Image");
        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        int image_height = image.getHeight();
        int image_width = image.getWidth();
        delete_button.addActionListener(new Delete_img(img, new_frame));

        if(image_height>1900){
            image_height=(int)(image_height/2.4);
            image_width=(int)(image_width/2.4);
        }
        if(image_height>1500){
            image_height=(int)(image_height/2.2);
            image_width=(int)(image_width/2.2);
        }
        if(image_height>1300){
            image_height=(int)(image_height/1.8);
            image_width=(int)(image_width/1.8);
        }
        if(image_height>1100){
            image_height=(int)(image_height/1.5);
            image_width=(int)(image_width/1.5);
        }
        if(image_height>1000){
            image_height=(int)(image_height/1.3);
            image_width=(int)(image_width/1.3);
        }
        big_pic.setSize(image_width,image_height);
        pic.setSize(image_width,image_height);

        pic.setIcon(new ImageIcon(image2.getImage().getScaledInstance(image_width, image_height, Image.SCALE_DEFAULT)));
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
