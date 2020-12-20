import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Main_window extends PicActionListener{
    public Main_window(String Location, String Name, String Description) {
        super(Location,Name,Description);

    }

    public static void main(String args[]){
        JFrame frame = new JFrame("Image Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JCheckBox checkBox1 = new JCheckBox("MRI");
        JCheckBox checkBox2 = new JCheckBox("CT");
        JCheckBox checkBox3 = new JCheckBox("Ultra");
        JCheckBox checkBox4 = new JCheckBox("Leg");

        JPanel filter = new JPanel();
        JPanel top_bar1 = new JPanel();
        JPanel top_bar2 = new JPanel();
        JPanel top_bar3 = new JPanel();
        JPanel images = new JPanel();

        images.setAutoscrolls(true);
        images.setLayout(new BoxLayout(images, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(images);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(250,60,1150,740);

        top_bar1.setBounds(0, 0, 250, 60);
        top_bar2.setBounds(250, 0, 900, 60);
        top_bar3.setBounds(1150, 0, 250, 60);

        top_bar1.setBorder(BorderFactory.createLineBorder(Color.black));
        top_bar2.setBorder(BorderFactory.createLineBorder(Color.black));
        top_bar3.setBorder(BorderFactory.createLineBorder(Color.black));

        JLabel logo = new JLabel("Logo");
        JLabel scan = new JLabel("Scan Type");
        JLabel body = new JLabel("Body Part");


        logo.setVerticalAlignment(JLabel.CENTER);
        logo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        logo.setAlignmentY(JLabel.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Medical Image Database");
        title.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        JButton upload = new JButton("Upload");
        upload.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        JButton search = new JButton("Search");
        JTextField search_stuff = new JTextField();
        search_stuff.setSize(100,15);

        ArrayList<String> file_location = new ArrayList<String>();
        file_location.add("https://codeimperial-mib.s3.eu-west-2.amazonaws.com/testImage.jpeg");


        ArrayList<String> file_name = new ArrayList<String>();
        file_name.add("Cat with mask");
        file_name.add("Orange cat");
        file_name.add("Blue-eyed cat");
        file_name.add("Sitting cat");
        file_name.add("X-ray cat");
        file_name.add("X-ray");
        file_name.add("X-ray Legs");
        file_name.add("X-ray Skull");
        file_name.add("X-ray Hand");
        file_name.add("X-ray Skull 2");

        ArrayList<String> file_description = new ArrayList<String>();
        file_description.add("Healthy cat");
        file_description.add("Sleepy cat");
        file_description.add("Cool cat");
        file_description.add("Spying Cat");
        file_description.add("X-ray of the front of cat");
        file_description.add("X-ray of bones");
        file_description.add("X-ray of legs");
        file_description.add("X-ray of skull");
        file_description.add("X-ray of an arm");
        file_description.add("X-ray of another skull");

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
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
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Image image = null;
                        try {
                            image = ImageIO.read(url);
                        }
                        catch (IOException e){
                            e.printStackTrace();
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
            }
        });


        top_bar1.add(logo);
        top_bar2.add(title);
        top_bar3.add(upload);
        scan.setFont(scan.getFont().deriveFont(18.0f));
        body.setFont(body.getFont().deriveFont(18.0f));

        GroupLayout layout = new GroupLayout(filter);
        filter.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scan)
                        .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(body,checkBox1, LayoutStyle.ComponentPlacement.INDENT)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(checkBox1)
                                        .addComponent(checkBox2)
                                        .addComponent(checkBox3)
                                )
                        )
                        .addComponent(body)
                        .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(body,checkBox4, LayoutStyle.ComponentPlacement.INDENT)
                                .addComponent(checkBox4)
                        )
                        .addComponent(search_stuff)
                        .addComponent(search)



                )


        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(scan)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(checkBox1)
                                        .addComponent(checkBox2)
                                        .addComponent(checkBox3)
                                )
                        )
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(body)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(checkBox4)
                                )
                        )


                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(search_stuff)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(search)
                )

        );


        filter.setBounds(0,60,250,740);
        frame.add(filter);
        frame.add(top_bar1);
        frame.add(top_bar2);
        frame.add(top_bar3);
        frame.add(scrollPane);
        frame.setSize(1415,810);
        frame.setLayout(null);
        frame.setVisible(true);



    }
}