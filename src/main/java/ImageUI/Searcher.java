package ImageUI;

import Entities.*;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//Searcher class searches the database by reading input fields from Interface and searching the database through the server
//Since the checkboxes are made in interface, search inherits from it
public class Searcher extends Interface implements ActionListener {
    private Load load;
    @Override
    public void actionPerformed(ActionEvent e) { //creates two parallel running threads - the load bar and the searching method
        load = new Load();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                load.execute();
            }
        });
    }

    static class Load extends SwingWorker<String, Void>{
        @Override
        public String doInBackground(){
            load_bar();
            searchAction();
            return "done";
        }

        @Override
        public void done() {
            progress_bar.setVisible(false);
            progress_bar.setIndeterminate(false);
            frame.setCursor(null);
        }

    }

    public static void searchAction(){ //sends the filter parameters to the server and receives images. It then outputs them into the JPanel
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY); //gives the search method the maximum priority between the load barand itself
        ArrayList<String> filter_body_select = new ArrayList<>();
        ArrayList<String> filter_modality_select = new ArrayList<>();
        ArrayList<String> filter_dates_select = new ArrayList<>();

        if(checkBox1.isSelected()){//checks which checkboxes with filter options were selected and what was written in input fields
            filter_modality_select.add("MRI");
        }
        if(checkBox2.isSelected()){
            filter_modality_select.add("CT");
        }
        if(checkBox3.isSelected()){
            filter_modality_select.add("ECG");
        }
        if(checkBox4.isSelected()){
            filter_modality_select.add("US");
        }
        if(checkBox5.isSelected()){
            filter_modality_select.add("XRAY");
        }
        if(checkBox6.isSelected()){
            filter_body_select.add("Leg");
        }
        if(checkBox7.isSelected()){
            filter_body_select.add("Head");
        }
        if(checkBox8.isSelected()){
            filter_body_select.add("Arm");
        }
        if(checkBox9.isSelected()){
            filter_body_select.add("Body");
        }
        if(checkBox10.isSelected()){
            filter_body_select.add("Heart");
        }
        if(filter_modality_select.isEmpty() && other_scan_field.getText().isEmpty()) {
            filter_modality_select.add("");
        }
        if(filter_body_select.isEmpty() && other_body_field.getText().isEmpty()){
            filter_body_select.add("");
        }
        filter_dates_select.add(start_date.getText());
        filter_dates_select.add(end_date.getText());
        if(!other_scan_field.getText().isEmpty()){
            filter_modality_select.add(other_scan_field.getText());
        }
        if(!other_body_field.getText().isEmpty()){
            filter_body_select.add(other_body_field.getText());
        }
        String patient_id=patient_id_window.getText();

        //Saves search parameters in a SearchParameters object
        SearchParameters pars = new SearchParameters();
        pars.setModality(filter_modality_select.toArray(new String [0]));
        pars.setBodyPart(filter_body_select.toArray(new String [0]));
        pars.setDate(filter_dates_select.toArray(new String [0]));
        pars.setPatientID(patient_id);

        MedicalImageLibrary libr = new MedicalImageLibrary();//makes the request to the server and receives back the needed images
        try {
            libr = makeSearchRequest(pars);
        }catch (InvalidObjectException o){ //Exception for no images found in database
            Toolkit.getDefaultToolkit().beep();
            System.out.println(o.getMessage());
            JOptionPane.showMessageDialog(null, o.getMessage());
            return;
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("Could not connect to database");
            JOptionPane.showMessageDialog(null, "Could not connect to database, please check your internet connection");
            return;
        }
        System.out.println("Search results received, displaying results");

        ArrayList<String> file_location;
        file_location = libr.getURLs();

        ArrayList<String> file_name;
        file_name = libr.getNames();

        ArrayList<String> file_PatientID;
        file_PatientID = libr.getPatientIds();

        ArrayList<String> file_Modalities;
        file_Modalities = libr.getModalities();

        ArrayList<String> file_body_part;
        file_body_part = libr.getBodyParts();

        ArrayList<String> file_dates;
        file_dates = libr.getDates();

        images.removeAll(); //outputs the images into JPanels and attaches an actionlistener to them, the one that allows to enlarge the image
        int n_of_rows = ((int) Math.ceil((file_location.size())/4.0))+4; // also outputs the necessary information about the files - names, modality, body part and date
        JPanel[] result_panel = new JPanel[n_of_rows];
        int k=0;
        result_panel[k]= new JPanel();
        result_panel[k].setPreferredSize(new Dimension(1100,360));
        for(int i=0; i< file_location.size();i++){
            JPanel img_panel = new JPanel();
            img_panel.setPreferredSize(new Dimension(250,200));

            JPanel info_panel = new JPanel();
            info_panel.setPreferredSize(new Dimension(250,100));

            JPanel result = new JPanel();
            result.setPreferredSize(new Dimension(270,350));

            JLabel name = new JLabel("Patient:");
            name.setPreferredSize(new Dimension(100,15));
            JLabel name_i = new JLabel(file_PatientID.get(i));
            name_i.setPreferredSize(new Dimension(100,15));

            JLabel modality = new JLabel("Modality:");
            modality.setPreferredSize(new Dimension(100,15));
            JLabel modality_i= new JLabel(file_Modalities.get(i));
            modality_i.setPreferredSize(new Dimension(100,15));

            JLabel body_part = new JLabel("Body Part:");
            body_part.setPreferredSize(new Dimension(100,15));
            JLabel body_part_i= new JLabel(file_body_part.get(i));
            body_part_i.setPreferredSize(new Dimension(100,15));

            JLabel date_label = new JLabel("Date:");
            date_label.setPreferredSize(new Dimension(100,15));
            JLabel date_label_i= new JLabel(file_dates.get(i));
            date_label_i.setPreferredSize(new Dimension(100,15));

            JLabel file_name_label = new JLabel("File Name:");
            file_name_label.setPreferredSize(new Dimension(100,15));
            JLabel file_name_label_i= new JLabel(file_name.get(i));
            file_name_label_i.setPreferredSize(new Dimension(100,15));


            URL url = null;
            try {
                url = new URL(file_location.get(i));
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            ImageIcon image = new ImageIcon(url);

            JButton button = new JButton();
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setVerticalTextPosition(JButton.CENTER);
            button.setMargin(new Insets(0,0,0,0));
            button.setPreferredSize(new Dimension(250,200));
            button.setIcon(new ImageIcon(image.getImage().getScaledInstance(250, 200, Image.SCALE_DEFAULT)));

            button.setVisible(true);
            button.addActionListener(new ImageEnlarger(libr.getImg(i)));

            img_panel.add(button);
            info_panel.add(name);
            info_panel.add(name_i);
            info_panel.add(modality);
            info_panel.add(modality_i);
            info_panel.add(body_part);
            info_panel.add(body_part_i);
            info_panel.add(date_label);
            info_panel.add(date_label_i);
            info_panel.add(file_name_label);
            info_panel.add(file_name_label_i);

            result.add(img_panel);
            result.add(info_panel);


            result_panel[k].add(result);
            if((i+1)%4==0){
                images.add(result_panel[k]);
                frame.getContentPane().validate();
                frame.getContentPane().repaint();
                k++;
                result_panel[k]= new JPanel();
            }


        }

        images.add(result_panel[k]);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();

        //making thumbnails and outputting them

    }
    public static void load_bar() {//method for the load bar - creates and keeps it running until the search is over
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        progress_bar.setVisible(true);
        progress_bar.setIndeterminate(true);
    }


    public static MedicalImageLibrary makeSearchRequest(SearchParameters pars) throws IOException {
        // Set up the body data
        System.out.println("Sending search to servlet");
        //Convert search parameters to json and send as body of request
        Gson gson = new Gson();
        String jsonString = gson.toJson(pars);
        System.out.println(jsonString);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);

        URL myURL = null;
        try {
            myURL = new URL("https://hlabsmedimagedatabase.herokuapp.com/search");
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
        System.out.println("Search sent");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        inputLine = bufferedReader.readLine();
        bufferedReader.close();
        Gson gson2 = new Gson();
        MedicalImageLibrary libr = gson2.fromJson(inputLine, MedicalImageLibrary.class);
        if (libr.isEmpty()){
            throw new InvalidObjectException("No images found in database");
        }
        return libr;
    }

}


