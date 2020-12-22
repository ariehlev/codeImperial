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
            File file = file_select.getSelectedFile();
            String file_type = (FilenameUtils.getExtension(file.toString())).toString();

            Img img = new Img();

            //JTextField id_field = new JTextField();

            if("dcm".equalsIgnoreCase(file_type) || "dicom".equalsIgnoreCase(file_type)){
                DicomConvert conv = new DicomConvert();
                img = conv.getTagByFile(file.getPath());
                img.setFileName(file.getName());
                StringBuffer date_dashes = new StringBuffer(img.getDate());
                date_dashes.insert(4,'-');
                date_dashes.insert(7, '-');
                img.setDate(date_dashes.toString());

                System.out.println(img.getDate());
                System.out.println(img.getFileName());
                System.out.println(img.getModality());
                System.out.println(img.getPatientID());
                try {
                    file = conv.jpgconvert(file.getPath());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
            //if("jpg".equalsIgnoreCase(file_type)){
                JFrame new_frame = new JFrame("Enlarged Picture");
                Image image1 = null;
                try {
                    image1 = ImageIO.read(file);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                JLabel pic = new JLabel();

                JLabel name_label = new JLabel("File Name: ");
                JLabel modality_label = new JLabel("Modality: ");
                JLabel body_label = new JLabel("Body Part: ");
                JLabel date_label = new JLabel("Date: ");
                JLabel id_label = new JLabel("ID: ");


                JTextField name_field = new JTextField();
                JTextField modality_field = new JTextField();
                JTextField body_field = new JTextField();
                JTextField date_field = new JTextField();
                JTextField id_field = new JTextField();


                name_field.setText(img.getFileName());
                modality_field.setText(img.getModality());
                body_field.setText(img.getBodyPart());
                date_field.setText(img.getDate());
                id_field.setText(img.getPatientID());


                JPanel big_pic = new JPanel();
                JPanel text = new JPanel();

                JButton upload = new JButton("Upload");


                big_pic.setSize(750, 600);
                pic.setSize(750, 600);

                pic.setIcon(new ImageIcon(new ImageIcon(image1).getImage().getScaledInstance(750, 600, Image.SCALE_DEFAULT)));
                big_pic.add(pic);

                GroupLayout layout = new GroupLayout(text);
                text.setLayout(layout);
                layout.setAutoCreateGaps(true);
                layout.setAutoCreateContainerGaps(true);
                layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(name_label)
                                .addComponent(modality_label)
                                .addComponent(body_label)
                                .addComponent(date_label)
                                .addComponent(id_label)
                                .addComponent(upload)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(name_field)
                                .addComponent(modality_field)
                                .addComponent(body_field)
                                .addComponent(date_field)
                                .addComponent(id_field)
                        )

                );
                layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(name_label)
                                .addComponent(name_field)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(modality_label)
                                .addComponent(modality_field)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(body_label)
                                .addComponent(body_field)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(date_label)
                                .addComponent(date_field)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(id_label)
                                .addComponent(id_field)
                        )
                        .addComponent(upload)
                );
                upload.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Img img = new Img();
                        String name_input = name_field.getText();
                        String modality_input = modality_field.getText();
                        String body_part_input = body_field.getText();
                        String date_input = date_field.getText();
                        String id_input = id_field.getText();

                        //ArrayList<String> data_upload = new ArrayList<String>();

                        img.setFileName(name_input);
                        img.setModality(modality_input);
                        img.setBodyPart(body_part_input);
                        img.setDate(date_input);
                        img.setPatientID(id_input);
                    }
                });


                text.setBounds(750, 0, 400, 600);

                new_frame.add(big_pic);
                new_frame.add(text);
                new_frame.setSize(1200, 600);
                new_frame.setLayout(null);
                new_frame.setVisible(true);
                new_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            //}
            /*
            else{
                DicomConvert conv = new DicomConvert();
                String filePath = "C:\\Programming3\\Dicom\\us heart 1.dcm";
                try {
                    conv.jpgconvert(file.getPath());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Img img = new Img();
                img = conv.getTagByFile(file.getPath());
            }

             */

        }
    }
}
