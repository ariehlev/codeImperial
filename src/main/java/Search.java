import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Search  implements ActionListener {

    private JPanel images;
    private JFrame frame;
    private JCheckBox ch1,ch2,ch3,ch4,ch5,ch6;

    public Search(JPanel imagee, JFrame framee, JCheckBox checkBox1,JCheckBox checkBox2,JCheckBox checkBox3,JCheckBox checkBox4, JCheckBox checkBox5, JCheckBox checkBox6){
        this.images=imagee;
        this.frame=framee;
        this.ch1=checkBox1;
        this.ch2=checkBox2;
        this.ch3=checkBox3;
        this.ch4=checkBox4;
        this.ch5=checkBox5;
        this.ch6=checkBox6;
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        ArrayList<String> filter_select = new ArrayList<String>();

        if(ch1.isSelected()){
            filter_select.add("MRI");
        }
        if(ch2.isSelected()){
            filter_select.add("CT");
        }
        if(ch3.isSelected()){
            filter_select.add("Microscope");
        }
        if(ch4.isSelected()){
            filter_select.add("Lungs");
        }
        if(ch5.isSelected()){
            filter_select.add("Brain");
        }
        if(ch6.isSelected()){
            filter_select.add("Spine");
        }
        ArrayList<String> file_location = new ArrayList<String>();
        file_location.add("https://codeimperial-mib.s3.eu-west-2.amazonaws.com/testImage.jpeg");

        ArrayList<String> file_name = new ArrayList<String>();
        file_name.add("Cat with mask");

        ArrayList<String> file_description = new ArrayList<String>();
        file_description.add("Healthy cat");
        images.removeAll();
        int n_of_rows = (int) Math.ceil((file_location.size())/4.0);
        JPanel[] big_result = new JPanel[n_of_rows];
        int k=0;
        big_result[k]= new JPanel();
        big_result[k].setPreferredSize(new Dimension(1100,360));
        for(int i=0; i< file_location.size();i++){

            JPanel img_panel = new JPanel();
            img_panel.setPreferredSize(new Dimension(250,200));

            JPanel info_panel = new JPanel();
            info_panel.setPreferredSize(new Dimension(250,140));

            JPanel buff_panel1 = new JPanel();
            buff_panel1.setPreferredSize(new Dimension(270,10));

            JPanel buff_panel2 = new JPanel();
            buff_panel2.setPreferredSize(new Dimension(10,350));

            JPanel result = new JPanel();
            result.setPreferredSize(new Dimension(270,350));

            JLabel name = new JLabel("Name:");
            name.setPreferredSize(new Dimension(100,15));
            JLabel name_i = new JLabel(file_name.get(i));
            name_i.setPreferredSize(new Dimension(100,15));
            URL url = null;

            try {
                url = new URL(file_location.get(i));
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }

            Image image = null;
            try {
                image = ImageIO.read(url);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            JButton button = new JButton();
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setVerticalTextPosition(JButton.CENTER);
            button.setMargin(new Insets(0,0,0,0));
            button.setPreferredSize(new Dimension(250,200));
            button.setIcon(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(250, 200, Image.SCALE_DEFAULT)));
            button.setVisible(true);
            button.addActionListener(new PicActionListener(file_location.get(i),file_name.get(i),file_description.get(i)));

            img_panel.add(button);
            info_panel.add(name);
            info_panel.add(name_i);

            result.add(img_panel);
            result.add(info_panel);


            big_result[k].add(result);
            if((i+1)%4==0){
                images.add(big_result[k]);
                frame.getContentPane().validate();
                frame.getContentPane().repaint();
                k++;
                big_result[k]= new JPanel();
            }


        }
        images.add(big_result[k]);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();

        //making thumbnails and outputting them

    }

}


