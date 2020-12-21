import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Search  implements ActionListener {

    private JPanel images;
    private JFrame frame;
    private JCheckBox ch1,ch2,ch3,ch4,ch5,ch6;
    private JTextField start,end,id;

    public Search(JPanel imagee, JFrame framee, JCheckBox checkBox1,JCheckBox checkBox2,JCheckBox checkBox3,JCheckBox checkBox4, JCheckBox checkBox5, JCheckBox checkBox6, JTextField start_date, JTextField end_date, JTextField patient_id_window){
        this.images=imagee;
        this.frame=framee;
        this.ch1=checkBox1;
        this.ch2=checkBox2;
        this.ch3=checkBox3;
        this.ch4=checkBox4;
        this.ch5=checkBox5;
        this.ch6=checkBox6;
        this.start=start_date;
        this.end=end_date;
        this.id=patient_id_window;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ArrayList<String> filter_body_select = new ArrayList<String>();
        ArrayList<String> filter_modality_select = new ArrayList<String>();
        ArrayList<String> filter_dates_select = new ArrayList<String>();
        String[] patient_id = new String[6];


        if(ch1.isSelected()){
            filter_modality_select.add("MRI");
        }
        if(ch2.isSelected()){
            filter_modality_select.add("CT");
        }
        if(ch3.isSelected()){
            filter_modality_select.add("Microscope");
        }
        if(ch4.isSelected()){
            filter_body_select.add("lungs");
        }
        if(ch5.isSelected()){
            filter_body_select.add("brain");
        }
        if(ch6.isSelected()){
            filter_body_select.add("spine");
        }
        filter_dates_select.add(start.getText());
        filter_dates_select.add(end.getText());
        patient_id[0]=id.getText();


        SearchParameters pars = new SearchParameters();
        pars.setModality(filter_modality_select.toArray(new String [0]));
        pars.setBodyPart(filter_body_select.toArray(new String [0]));
        pars.setDate(filter_dates_select.toArray(new String [0]));
        //pars.setDate(new String[]{"14/01/2020","20/12/2020"});
        pars.setPatientID(patient_id);
        //pars.setPatientID(new String[]{"not null"});

        String request = "SELECT * FROM MedImages WHERE modality in ('";
        String delim = "','";
        request = request.concat(String.join(delim,(pars.getModality())));
        request = request.concat("')");
        request = request.concat(" AND bodyPart in ('");
        request = request.concat(String.join(delim,(pars.getBodyPart())));
        request = request.concat("')");
        //request = request.concat(" AND date BETWEEN'");
        //request = request.concat(String.join("' AND '",(pars.getDate())));
        //request = request.concat("'");
        String part = "";
        if (String.join(delim,(pars.getPatientID()))==("not null")){
            part = part.concat(" AND patientid is not null;");
        }
        else{
            part = part.concat(" AND patientid = '");
            part = part.concat(String.join(delim,(pars.getPatientID())));
            part = part.concat("';");
        }
        request = request.concat(part);
        System.out.println(request);
        Img_lib libr = new Img_lib();
        try {
            libr = makePostRequest(pars);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        libr.Details();

        ArrayList<String> file_location = new ArrayList<String>();
        //file_location.add("https://codeimperial-mib.s3.eu-west-2.amazonaws.com/testImage.jpeg");
        file_location = libr.getURL();
        for(int i = 0; i<file_location.size(); i++) {
            System.out.println(file_location.get(i));
        }

        ArrayList<String> file_name = new ArrayList<String>();
        //file_name.add("Cat with mask");
        file_name = libr.getPatientId();
        for(int i = 0; i<file_name.size(); i++) {
            System.out.println(file_name.get(i));
        }

        ArrayList<String> file_description = new ArrayList<String>();
        //file_description.add("Healthy cat");
        file_description = libr.getModality();
        for(int i = 0; i<file_description.size(); i++) {
            System.out.println(file_description.get(i));
        }


        ArrayList<String> file_body_part = new ArrayList<String>();
        file_body_part = libr.getBodyPart();
        for(int i = 0; i<file_body_part.size(); i++) {
            System.out.println(file_body_part.get(i));
        }
        //need to make arrays for other parameters

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
            info_panel.setPreferredSize(new Dimension(250,100));

            JPanel buff_panel1 = new JPanel();
            buff_panel1.setPreferredSize(new Dimension(270,10));

            JPanel buff_panel2 = new JPanel();
            buff_panel2.setPreferredSize(new Dimension(10,350));

            JPanel result = new JPanel();
            result.setPreferredSize(new Dimension(270,350));

            JLabel name = new JLabel("ID:");
            name.setPreferredSize(new Dimension(100,15));
            JLabel name_i = new JLabel(file_name.get(i));
            name_i.setPreferredSize(new Dimension(100,15));

            JLabel modality = new JLabel("Modality:");
            modality.setPreferredSize(new Dimension(100,15));
            JLabel modality_i= new JLabel(file_description.get(i));
            modality_i.setPreferredSize(new Dimension(100,15));

            JLabel body_part = new JLabel("Body Part:");
            body_part.setPreferredSize(new Dimension(100,15));
            JLabel body_part_i= new JLabel(file_body_part.get(i));
            body_part_i.setPreferredSize(new Dimension(100,15));


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
            info_panel.add(modality);
            info_panel.add(modality_i);

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
    protected static Img_lib makePostRequest(SearchParameters pars) throws IOException {
        // Set up the body data
        Gson gson = new Gson();
        String jsonString = gson.toJson(pars);
        System.out.println(jsonString);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);

        URL myURL = null;
        try {
            myURL = new URL("http://localhost:8080/LocalServlet/main");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) myURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up the header
        try {
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
        Gson gson2 = new Gson();
        Img_lib libr = gson2.fromJson(inputLine,Img_lib.class);
        libr.Details();
        return libr;
    }

}


