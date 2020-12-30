package ImageUI;

import Entities.*;
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

    boolean Dicomchecker = false;//Marker for checking if the file uploaded is a Dicom

    @Override
    public void actionPerformed(ActionEvent e) { //the following code asks the user to select the image or dicom file to upload to the database. After that, the user can input the necessary details about image - patient id, date, modality and etc.
        Dicomchecker = false;
        JFileChooser file_select = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        file_select.setDialogTitle("Choose a file to upload: "); //promts a user to select an image to upload.
        file_select.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file_select.setAcceptAllFileFilterUsed(false);
        //Accepts only Dicom,jpg,png,jfif and gif
        FileNameExtensionFilter filter = new FileNameExtensionFilter("DICOM, JPG, PNG, JFIF, GIF Files","dcm","dicom","jpg","png","jfif","gif");
        file_select.addChoosableFileFilter(filter);

        int val = file_select.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file = file_select.getSelectedFile();
            String file_type = (FilenameUtils.getExtension(file.toString()));

            MedImage img = new MedImage();

            if("dcm".equalsIgnoreCase(file_type) || "dicom".equalsIgnoreCase(file_type)){ //checks if file to upload is Dicom as more processing required
                Dicomchecker = true;
                try {
                    img = getTagByFile(file.getPath());//Gets image metadata and stores them in a MedImage object
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Dicom file not supported");
                    return;
                }
                StringBuilder date_dashes = new StringBuilder(img.getDate()); //Adds dashes to the date to match date format supported by the database
                date_dashes.insert(4,'-');
                date_dashes.insert(7, '-');
                img.setDate(date_dashes.toString());

                try {
                    file = dcm2jpgConvert(file.getPath()); //Converts dicom to a jpg image for storage and to display
                    file_type = (FilenameUtils.getExtension(file.toString())); //Updates file type to jpg

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Dicom file not supported");
                    return;
                }

            }
            img.setFileName(file.getName());//Sets image name

            //Preview window made with input fields for image data to be filled in by user before upload
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

            //Add the file format to the end of the file name so that the user can't change file format and cause issues
            //Fills in text fields with data from the image metadata (only works for dicom images)
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
            if (img.getDate() == null){ //If the date field is empty, puts today's date by default in supported date format to aid the user
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
                public void actionPerformed(ActionEvent e) { //Action listener for the upload button, saves input fields in a MedImage object and sends image for upload
                    MedImage img = new MedImage();
                    String name_input = name_field.getText();
                    String modality_input = modality_field.getText();
                    String body_part_input = body_field.getText();
                    String date_input = date_field.getText();
                    String id_input = id_field.getText();
                    //Checks to make sure user has filled in all fields
                    if (name_input.equals("") || modality_input.equals("") || body_part_input.equals("") || date_input.equals("") || id_input.equals("")){
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Please input something in all fields");
                        return;
                    }
                    //Saves data into an image object to be sent to server
                    img.setFileName(name_input + "." + finalFile_type); //Adds respective ending to file name (i.e. .jpg)
                    img.setModality(modality_input);
                    img.setBodyPart(body_part_input);
                    img.setDate(date_input);
                    img.setPatientID(id_input);
                    try {
                        img.setImageURL(makeUploadImagePOSTRequest(finalFile, img.getFileName()));//Sends image to server for uploading to s3, receives the url where it's stored which is saved in MedImage object
                        makeUploadPostRequest(img);//sends image object with all information to the server to be uploaded to postgresSQL database

                        if (Dicomchecker){ //Deletes temporary Dicom converted jpg file in temporary folder
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
                        System.out.println(o.getMessage());
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
                                            public void windowClosing(WindowEvent e) { //Makes sure temporary dicom gets deleted from local system
                                                try {
                                                    if (Dicomchecker){//Deletes temporary Dicom converted jpg file in temporary folder
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

    public static File dcm2jpgConvert (String filePath) throws IOException { //Converts Dicom file to jpg (dcm4che Dcm2Jpg would not work, this is a workaround)
        System.out.println("Opening Dicom File");
        //Reference 1 - taken from https://www.programmersought.com/article/21235554230/
        File src = new File(filePath);
        String destPath = System.getProperty("java.io.tmpdir")+ src.getName().substring(0,src.getName().length() - 4) + "temporary.dcm";
        File dest = new File(destPath);
        String imagePath = System.getProperty("java.io.tmpdir")+ src.getName().substring(0,src.getName().length() - 4) + ".jpg";
        File destjpg = new File(imagePath);
        try (Transcoder transcoder = new Transcoder(src)) { //Begins by decompressing the image
            System.out.println("Decompressing Image");
            transcoder.setDestinationTransferSyntax(UID.ExplicitVRLittleEndian);
            transcoder.transcode(new Transcoder.Handler(){
                @Override
                public OutputStream newOutputStream(Transcoder transcoder, Attributes dataset) throws IOException {
                    return new FileOutputStream(dest); //Stores decompressed image in a temporary Dicom file
                }
            });
            System.out.println("Image Decompressed");
            System.out.println("Temporary Dicom saved in local temporary folder");
            //End of Reference 1
            //Reference 2 - taken from https://www.programmersought.com/article/51681500761/
            DICOM dicom = new DICOM(); //Converts decompressed Dicom into jpg
            dicom.run(String.valueOf(dest));
            BufferedImage bi = (BufferedImage) dicom.getImage();
            ImageIO.write(bi, "jpg", destjpg);
            System.out.println("Image converted to jpg");
            System.out.println("Temporary jpg saved in local temporary folder");
            //End of Reference 2
        } catch (Exception e) {
            System.out.println("Could not decompress image");
            Files.deleteIfExists(dest.toPath()); //Deletes temporary Dicom
            System.out.println("Temporary Dicom deleted from local temporary folder");
            throw e;
        }
        Files.deleteIfExists(dest.toPath()); //Deletes temporary Dicom
        System.out.println("Temporary Dicom deleted from local temporary folder");
        return destjpg;
    }

    //Reference 3 - taken from https://www.programmersought.com/article/73222342075/
    public static MedImage getTagByFile(String pathFile) throws IOException { //Retrieves metadata from Dicom and stores it into a MedImage object
        System.out.println("Retrieving image data");
        MedImage img = new MedImage();
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
    //End of Reference 3

    /* Reference 4 - taken from https://www.codejava.net/java-ee/servlet/upload-file-to-servlet-without-using-html-form */
    protected static String makeUploadImagePOSTRequest(File file, String name) throws IOException { //Sends image to be uploaded to s#
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
    //End of Reference 4

    protected static void makeUploadPostRequest(MedImage newImage) throws IOException {//Sends image data of image to be uploaded including url
        System.out.println("Adding image to database");
        // Set up the body data
        // Sends image data to be uploaded to database by converting a MedImage object to json
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