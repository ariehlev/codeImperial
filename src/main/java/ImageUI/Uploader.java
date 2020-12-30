package ImageUI;

import Entities.Img;
import com.google.gson.Gson;
import ij.plugin.DICOM;
import org.apache.commons.io.FilenameUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.imageio.codec.Transcoder;
import org.dcm4che3.io.DicomInputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Uploader implements ActionListener {

    boolean Dicomchecker = false;

    @Override
    public void actionPerformed(ActionEvent e) { //the following code asks the user to select the image or dicom file to upload to the database. After that, the user can input the necessary details about image - patient id, date, modality and etc.
        Dicomchecker = false;
        JFileChooser file_select = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        file_select.setDialogTitle("Choose a file to upload: "); //promts a user to select an image to upload.
        file_select.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file_select.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("DICOM, JPG, PNG, JFIF, GIF Files","dcm","dicom","jpg","png","jfif","gif");
        file_select.addChoosableFileFilter(filter);

        int val = file_select.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file = file_select.getSelectedFile();
            String file_type = (FilenameUtils.getExtension(file.toString()));

            Img img = new Img();

            if("dcm".equalsIgnoreCase(file_type) || "dicom".equalsIgnoreCase(file_type)){ //checks the file format to be uploaded
                Dicomchecker = true;
                try {
                    img = getTagByFile(file.getPath());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Dicom file not supported");
                    return;
                }
                StringBuilder date_dashes = new StringBuilder(img.getDate());
                date_dashes.insert(4,'-');
                date_dashes.insert(7, '-');
                img.setDate(date_dashes.toString());

                try {
                    file = jpgconvert(file.getPath());
                    file_type = (FilenameUtils.getExtension(file.toString()));

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Dicom file not supported");
                    return;
                }

            }
            img.setFileName(file.getName());
            JFrame new_frame = new JFrame("Enlarged Picture");
            ImageIcon image = new ImageIcon(file.getPath());

            JLabel pic = new JLabel();
            //lists the input parameters
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
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date_field.setText(sdf.format(cal.getTime()));
            }
            else date_field.setText(img.getDate());
            id_field.setText(img.getPatientID());

            int image_height = image.getIconHeight();
            int image_width = image.getIconWidth();

            if(image_height>1900){ //checks if the original image on the server is larger than the screen. If it is - resizes it to be able to see it better
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

            JPanel big_pic = new JPanel();
            JPanel text = new JPanel();

            JButton upload = new JButton("Upload");

            big_pic.setSize(image_width, image_height);
            pic.setSize(image_width, image_height);

            pic.setIcon(new ImageIcon(image.getImage().getScaledInstance(image_width, image_height, Image.SCALE_DEFAULT)));
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
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Please input something in all fields");
                        return;
                    }

                    img.setFileName(name_input + "." + finalFile_type);
                    img.setModality(modality_input);
                    img.setBodyPart(body_part_input);
                    img.setDate(date_input);
                    img.setPatientID(id_input);
                    try {
                        img.setImageURL(makeUploadImagePOSTRequest(finalFile, img.getFileName()));
                        makeUploadPostRequest(img);

                        if (Dicomchecker){
                            Files.deleteIfExists(finalFile.toPath());
                            System.out.println("Temporary jpg deleted from local temporary folder");
                        }
                        System.out.println("Upload successful");
                        JOptionPane.showMessageDialog(null, "The upload was successful");
                        new_frame.dispose();
                    }
                    catch (InvalidObjectException | ConnectException o){
                        o.printStackTrace();
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, o.getMessage());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        Toolkit.getDefaultToolkit().beep();
                        System.out.println("Upload failed");
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
                                                        Files.deleteIfExists(finalFile.toPath());
                                                        System.out.println("Temporary jpg deleted from local temporary folder");
                                                    }
                                                } catch (IOException ioException) {
                                                    ioException.printStackTrace();
                                                }
                                            }
                                        });
            new_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    public static File jpgconvert (String filePath) throws IOException {
        System.out.println("Opening Dicom File");
        //Reference X - taken from https://www.programmersought.com/article/21235554230/
        File src = new File(filePath);
        String destPath = System.getProperty("java.io.tmpdir")+ src.getName().substring(0,src.getName().length() - 4) + "temporary.dcm";
        File dest = new File(destPath);
        String imagePath = System.getProperty("java.io.tmpdir")+ src.getName().substring(0,src.getName().length() - 4) + ".jpg";
        File destjpg = new File(imagePath);
        try (Transcoder transcoder = new Transcoder(src)) {
            System.out.println("Decompressing Image");
            transcoder.setDestinationTransferSyntax(UID.ExplicitVRLittleEndian);
            transcoder.transcode(new Transcoder.Handler(){
                @Override
                public OutputStream newOutputStream(Transcoder transcoder, Attributes dataset) throws IOException {
                    return new FileOutputStream(dest);
                }
            });
            System.out.println("Image Decompressed");
            System.out.println("Temporary Dicom saved in local temporary folder");
            //End of Reference X
            //Reference X - taken from https://www.programmersought.com/article/51681500761/
            DICOM dicom = new DICOM();
            dicom.run(String.valueOf(dest));
            BufferedImage bi = (BufferedImage) dicom.getImage();
            ImageIO.write(bi, "jpg", destjpg);
            System.out.println("Image converted to jpg");
            System.out.println("Temporary jpg saved in local temporary folder");
            //End of Reference X
        } catch (Exception e) {
            System.out.println("Could not decompress image");
            Files.deleteIfExists(dest.toPath());
            System.out.println("Temporary Dicom deleted from local temporary folder");
            throw e;
        }
        Files.deleteIfExists(dest.toPath());
        System.out.println("Temporary Dicom deleted from local temporary folder");
        return destjpg;
    }


    //Reference X - taken from https://www.programmersought.com/article/73222342075/
    public static Img getTagByFile(String pathFile) throws IOException {
        System.out.println("Retrieving image data");
        Img img = new Img();
        File file = new File(pathFile);
        DicomInputStream dis = new DicomInputStream(file);
        dis.readFileMetaInformation();
        Attributes attrs = dis.readDataset(-1, -1);
        img.setPatientID(attrs.getString(Tag.PatientID));
        img.setModality(attrs.getString(Tag.Modality));
        img.setDate(attrs.getString(Tag.StudyDate));
        img.setBodyPart(attrs.getString(Tag.BodyPartExamined));
        img.setImageURL("");
        System.out.println("Image data successfully retrieved");
        return img;
    }
    //End of Reference X

    /* Reference X - taken from https://www.codejava.net/java-ee/servlet/upload-file-to-servlet-without-using-html-form */
    protected static String makeUploadImagePOSTRequest(File file, String name) throws IOException {
        final String UPLOAD_URL = "https://hlabsmedimagedatabase.herokuapp.com/uploadimage";
        final int BUFFER_SIZE = 4096;

        System.out.println("Uploading image to s3");
        System.out.println("File to upload: " + name);

        // creates a HTTP connection
        URL url = new URL(UPLOAD_URL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");
        // sets file name as a HTTP header
        httpConn.setRequestProperty("fileName", name);

        // opens output stream of the HTTP connection for writing data
        OutputStream outputStream = httpConn.getOutputStream();

        // Opens input stream of the file for reading data
        FileInputStream inputStream = new FileInputStream(file);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        System.out.println("Start writing data...");

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        System.out.println("Data was written.");
        outputStream.close();
        inputStream.close();

        // always check HTTP response code from server
        int responseCode = httpConn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // reads server's response
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String response = reader.readLine();
            System.out.println("Server's response: " + response);
            if (response.equals("Duplicate file")) {
                System.out.println("Duplicate file found in database");
                throw new InvalidObjectException("Another file on the database already has that name, please choose a unique name");
            }
            else{
                System.out.println("Image successfully saved to s3");
                return response;
            }
        } else {
            System.out.println("Server returned non-OK code: " + responseCode);
            throw new ConnectException("Failed to connect to server");
        }

    }
    //End of Reference X

    protected static void makeUploadPostRequest(Img newImage) throws IOException {
        System.out.println("Adding image to database");
        // Set up the body data
        Gson gson = new Gson();
        String jsonString = gson.toJson(newImage);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);

        URL myURL = null;
        try {
            myURL = new URL("https://hlabsmedimagedatabase.herokuapp.com/upload");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;

        try {
            assert myURL != null;
            conn = (HttpURLConnection) myURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up the header
        try {
            assert conn != null;
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(body.length));
        conn.setDoOutput(true);
        // Write the body of the request
        try (OutputStream outputStream = conn.getOutputStream()) {
            try {
                outputStream.write(body, 0, body.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        inputLine = bufferedReader.readLine();
        bufferedReader.close();
        System.out.println(inputLine);
    }
}