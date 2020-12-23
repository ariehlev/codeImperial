import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Upload implements ActionListener {

    boolean Dicomchecker = false;

    @Override
    public void actionPerformed(ActionEvent e) {
        Dicomchecker = false;
        JFileChooser file_select = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        file_select.setDialogTitle("Choose a file to upload: ");
        file_select.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file_select.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("DICOM, JPG, PNG, JFIF, GIF Files","dcm","dicom","jpg","png","jfif","gif");
        file_select.addChoosableFileFilter(filter);

        int val = file_select.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file = file_select.getSelectedFile();
            String file_type = (FilenameUtils.getExtension(file.toString()));

            Img img = new Img();

            //JTextField id_field = new JTextField();

            if("dcm".equalsIgnoreCase(file_type) || "dicom".equalsIgnoreCase(file_type)){
                Dicomchecker = true;
                //DicomConvert conv = new DicomConvert();
                img = DicomConvert.getTagByFile(file.getPath());
                StringBuilder date_dashes = new StringBuilder(img.getDate());
                date_dashes.insert(4,'-');
                date_dashes.insert(7, '-');
                img.setDate(date_dashes.toString());

                System.out.println(img.getDate());
                System.out.println(img.getFileName());
                System.out.println(img.getModality());
                System.out.println(img.getPatientID());
                try {
                    file = DicomConvert.jpgconvert(file.getPath());
                    file_type = (FilenameUtils.getExtension(file.toString()));

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
            img.setFileName(file.getName());
            //if("jpg".equalsIgnoreCase(file_type)){
            JFrame new_frame = new JFrame("Enlarged Picture");
            ImageIcon image2 = new ImageIcon(file.getPath());

            JLabel pic = new JLabel();

            JLabel name_label = new JLabel("File Name: ");
            JLabel modality_label = new JLabel("Modality: ");
            JLabel body_label = new JLabel("Body Part: ");
            JLabel date_label = new JLabel("Date: ");
            JLabel id_label = new JLabel("ID: ");

            JTextField name_field = new JTextField();
            JLabel jpg_label = new JLabel();
            JTextField modality_field = new JTextField();
            JTextField body_field = new JTextField();
            JTextField date_field = new JTextField();
            JTextField id_field = new JTextField();

            if("jpg".equalsIgnoreCase(file_type)) {
                jpg_label.setText(".jpg");
                name_field.setText(img.getFileName().substring(0,file.getName().length() - 4));
            }
            if("png".equalsIgnoreCase(file_type)) {
                jpg_label.setText(".png");
                name_field.setText(img.getFileName().substring(0,file.getName().length() - 4));
            }
            if("jfif".equalsIgnoreCase(file_type)) {
                jpg_label.setText(".jfif");
                name_field.setText(img.getFileName().substring(0,file.getName().length() - 5));
            }
            if("gif".equalsIgnoreCase(file_type)) {
                jpg_label.setText(".gif");
                name_field.setText(img.getFileName().substring(0,file.getName().length() - 4));
            }

            modality_field.setText(img.getModality());
            body_field.setText(img.getBodyPart());
            if (img.getDate() == null){
                //https://www.scribd.com/doc/3846457/GET-DATE-TIME-in-java
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date_field.setText(sdf.format(cal.getTime()));
            }
            else date_field.setText(img.getDate());
            id_field.setText(img.getPatientID());

            int image_height = image2.getIconHeight();
            int image_width = image2.getIconWidth();

            if(image_height>1500){
                image_height=(int)(image_height/1.5);
                image_width=(int)(image_width/1.5);
            }
            if(image_height>1100){
                image_height=(int)(image_height/1.1);
                image_width=(int)(image_width/1.1);
            }

            JPanel big_pic = new JPanel();
            JPanel text = new JPanel();

            JButton upload = new JButton("Upload");


            big_pic.setSize(image_width, image_height);
            pic.setSize(image_width, image_height);

            pic.setIcon(new ImageIcon(image2.getImage().getScaledInstance(image_width, image_height, Image.SCALE_DEFAULT)));
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
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jpg_label)
                    )
            );
            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(name_label)
                            .addComponent(name_field)
                            .addComponent(jpg_label)
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
            File finalFile = file;

            String finalFile_type = file_type;
            upload.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Img img = new Img();
                    String name_input = name_field.getText();
                    String modality_input = modality_field.getText();
                    String body_part_input = body_field.getText();
                    String date_input = date_field.getText();
                    String id_input = id_field.getText();
                    if (name_input.equals("") || modality_input.equals("") || body_part_input.equals("") || date_input.equals("") || id_input.equals("")){
                        JOptionPane.showMessageDialog(null, "Please input something in all fields");
                        return;
                    }

                    //ArrayList<String> data_upload = new ArrayList<String>();

                    img.setFileName(name_input + "." + finalFile_type);
                    img.setModality(modality_input);
                    img.setBodyPart(body_part_input);
                    img.setDate(date_input);
                    img.setPatientID(id_input);
                    System.out.println(img.getFileName());
                    try {
                        img.setImageURL(ServerComm.makeUploadImagePOSTRequest(finalFile, img.getFileName()));
                        ServerComm.makeUploadPostRequest(img);
                        //img.setImageURL();
                        if (Dicomchecker){
                            Files.deleteIfExists(finalFile.toPath());
                            System.out.println("jpg deleted from computer");
                        }
                        JOptionPane.showMessageDialog(null, "The upload was successful");
                        new_frame.dispose();
                    }
                    catch (InvalidObjectException | ConnectException o){
                        o.printStackTrace();
                        JOptionPane.showMessageDialog(null, o.getMessage());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        JOptionPane.showMessageDialog(null, "The upload was unsuccessful");
                    }
                }
            });


            text.setBounds(image_width, 0, 400, 600);

            new_frame.add(big_pic);
            new_frame.add(text);
            new_frame.setSize(image_width+420, image_height+50);
            new_frame.setLayout(null);
            new_frame.setVisible(true);
            new_frame.addWindowListener(new WindowAdapter() {
                                            @Override
                                            public void windowClosing(WindowEvent e) {
                                                try {
                                                    if (Dicomchecker){
                                                        System.out.println("Attempting to delete image");
                                                        Files.deleteIfExists(finalFile.toPath());
                                                    }
                                                } catch (IOException ioException) {
                                                    ioException.printStackTrace();
                                                }
                                            }
                                        });
            new_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    //@Override
    //public void windowClosing(WindowEvent e) {
//
   // }
}