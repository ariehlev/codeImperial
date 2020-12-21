import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

public class Upload implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser file_select = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        file_select.setDialogTitle("Choose a directory to save your file: ");
        file_select.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file_select.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("DICOM and JPG Files","dcm","dicom","jpg");
        file_select.addChoosableFileFilter(filter);

        int val = file_select.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file_path = file_select.getSelectedFile();
            System.out.println(file_path.getAbsolutePath());
            System.out.println(FilenameUtils.getExtension(String.valueOf(file_path)));
            if(FilenameUtils.getExtension(String.valueOf(file_path))=="jpg"){
                JFrame new_frame = new JFrame("Enlarged Picture");
                Image image1 = null;
                try {
                    image1= ImageIO.read(file_path);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                JLabel pic = new JLabel();

                JLabel nam = new JLabel("Name: ");
                JLabel descr = new JLabel("Description: ");

                JTextField name = new JTextField();
                JTextField desc = new JTextField();

                JPanel big_pic = new JPanel();
                JPanel text = new JPanel();

                JButton upload = new JButton("Upload");


                big_pic.setSize(750,600);
                pic.setSize(750,600);

                pic.setIcon(new ImageIcon(new ImageIcon(image1).getImage().getScaledInstance(750, 600, Image.SCALE_DEFAULT)));
                big_pic.add(pic);

                GroupLayout layout = new GroupLayout(text);
                text.setLayout(layout);
                layout.setAutoCreateGaps(true);
                layout.setAutoCreateContainerGaps(true);
                layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nam)
                                .addComponent(descr)
                                .addComponent(upload)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(name)
                                .addComponent(desc)
                        )

                );
                layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nam)
                                .addComponent(name)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(descr)
                                .addComponent(desc)
                        )
                        .addComponent(upload)
                );
                upload.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name_input=name.getText();
                        String description_input=desc.getText();
                        ArrayList<String> data_upload = new ArrayList<String>();
                        data_upload.add(name_input);
                        data_upload.add(description_input);

                    }
                });


                text.setBounds(750,0,400,600);

                new_frame.add(big_pic);
                new_frame.add(text);
                new_frame.setSize(1200,600);
                new_frame.setLayout(null);
                new_frame.setVisible(true);
                new_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            }


        }
    }
}
