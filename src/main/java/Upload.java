import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class Upload implements ActionListener{

    boolean Dicomchecker = false;

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
                    //Files.deleteIfExists(file.toPath());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
            img.setFileName(file.getName());
            //if("jpg".equalsIgnoreCase(file_type)){
            JFrame new_frame = new JFrame("Enlarged Picture");
            BufferedImage image1 = null;
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
            JLabel jpg_label = new JLabel(".jpg");
            JTextField modality_field = new JTextField();
            JTextField body_field = new JTextField();
            JTextField date_field = new JTextField();
            JTextField id_field = new JTextField();


            name_field.setText(img.getFileName());
            modality_field.setText(img.getModality());
            body_field.setText(img.getBodyPart());
            date_field.setText(img.getDate());
            id_field.setText(img.getPatientID());

            int image_height = image1.getHeight();
            int image_width = image1.getWidth();

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

            assert image1 != null;
            pic.setIcon(new ImageIcon(new ImageIcon(image1).getImage().getScaledInstance(image_width, image_height, Image.SCALE_DEFAULT)));
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
                    System.out.println(finalFile.getName());
                    try {
                        img.setImageURL(makeUploadImagePOSTRequest(finalFile));
                        makeUploadPostRequest(img);

                        if (Dicomchecker){
                            Files.deleteIfExists(finalFile.toPath());
                            System.out.println("jpg deleted from computer");
                        }
                        JOptionPane.showMessageDialog(null, "The upload was successful");
                        new_frame.dispose();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        JOptionPane.showMessageDialog(null, "The upload was unsuccessful");
                    }
                }
            });


            text.setBounds(image_width, 0, 400, 600);

            new_frame.add(big_pic);
            new_frame.add(text);
            new_frame.setSize(image_width+400, image_height+50);
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
    protected static String makeUploadImagePOSTRequest(File file) throws IOException {
        //final String UPLOAD_URL = "http://localhost:8080/LocalServlet/uploadimage";
        final String UPLOAD_URL = "https://hlabsmedimagedatabase.herokuapp.com/uploadimage";
        final int BUFFER_SIZE = 4096;

        // takes file path from first program's argument
        //String filePath = "/Users/lilmaga/Desktop/test.dcm";
        //File uploadFile = new File(filePath);
        System.out.println("File to upload: " + file.getPath());

        // creates a HTTP connection
        URL url = new URL(UPLOAD_URL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");
        // sets file name as a HTTP header
        httpConn.setRequestProperty("fileName", file.getName());

        // opens output stream of the HTTP connection for writing data
        OutputStream outputStream = httpConn.getOutputStream();

        // Opens input stream of the file for reading data
        FileInputStream inputStream = new FileInputStream(file);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

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
            return response;
        } else {
            System.out.println("Server returned non-OK code: " + responseCode);
            return "505";
        }

    }

    protected static void makeUploadPostRequest(Img newImage) throws IOException {
        // Set up the body data
        Gson gson = new Gson();
        String jsonString = gson.toJson(newImage);
        System.out.println(jsonString);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);

        URL myURL = null;
        try {
            //myURL = new URL("http://localhost:8080/LocalServlet/upload");
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
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        // Read the body of the response
        //while ((inputLine = bufferedReader.readLine()) != null) { System.out.println(inputLine);
        //System.out.println("     Breakpoint     ");
        //}
        inputLine = bufferedReader.readLine();
        bufferedReader.close();
        System.out.println(inputLine);
    }
}