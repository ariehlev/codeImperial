import java.awt.*;
import javax.swing.*;


public class Interface extends Search{
    public Interface(JPanel images,JFrame frame,JCheckBox ch1,JCheckBox ch2,JCheckBox ch3,JCheckBox ch4,JCheckBox ch5,JCheckBox ch6) {
        super(images,frame,ch1,ch2,ch3,ch4,ch5,ch6);

    }
    public static void inter() {
        JFrame frame = new JFrame("Image Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JCheckBox checkBox1 = new JCheckBox("MRI");
        JCheckBox checkBox2 = new JCheckBox("CT");
        JCheckBox checkBox3 = new JCheckBox("Microscope");
        JCheckBox checkBox4 = new JCheckBox("Lungs");
        JCheckBox checkBox5 = new JCheckBox("Brain");
        JCheckBox checkBox6 = new JCheckBox("Spine");

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
        scrollPane.setBounds(250, 60, 1150, 740);

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
        search_stuff.setSize(100, 15);

        search.addActionListener(new Search(images,frame,checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6));

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
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(checkBox4)
                                    .addComponent(checkBox5)
                                    .addComponent(checkBox6)
                                )
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
                                        .addComponent(checkBox5)
                                        .addComponent(checkBox6)
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
