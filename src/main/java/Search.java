import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Search extends Interface implements ActionListener {
    private Load load;

    @Override
    public void actionPerformed(ActionEvent e) {
        load = new Load();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                load.execute();

            }
        });


    }
    class Load extends SwingWorker<String, Void>{
        @Override
        public String doInBackground(){
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            progress_bar.setVisible(true);
            progress_bar.setIndeterminate(true);
            searchaction();
            return "done";
        }

        @Override
        public void done() {
            progress_bar.setVisible(false);
            progress_bar.setIndeterminate(false);
            frame.setCursor(null);
        }

    }

    public static void searchaction(){
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        ArrayList<String> filter_body_select = new ArrayList<>();
        ArrayList<String> filter_modality_select = new ArrayList<>();
        ArrayList<String> filter_dates_select = new ArrayList<>();

        if(checkBox1.isSelected()){
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

        SearchParameters pars = new SearchParameters();
        pars.setModality(filter_modality_select.toArray(new String [0]));
        pars.setBodyPart(filter_body_select.toArray(new String [0]));
        pars.setDate(filter_dates_select.toArray(new String [0]));
        pars.setPatientID(patient_id);

        Img_lib libr = new Img_lib();
        try {
            libr = ServerComm.makeSearchRequest(pars);
        }catch (InvalidObjectException o){
            JOptionPane.showMessageDialog(null, o.getMessage());

        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
        libr.Details();

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

        images.removeAll();
        int n_of_rows = ((int) Math.ceil((file_location.size())/4.0))+4;
        JPanel[] big_result = new JPanel[n_of_rows];
        int k=0;
        big_result[k]= new JPanel();
        big_result[k].setPreferredSize(new Dimension(1100,360));
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
            ImageIcon image1 = new ImageIcon(url);

            JButton button = new JButton();
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setVerticalTextPosition(JButton.CENTER);
            button.setMargin(new Insets(0,0,0,0));
            button.setPreferredSize(new Dimension(250,200));
            button.setIcon(new ImageIcon(image1.getImage().getScaledInstance(250, 200, Image.SCALE_DEFAULT)));

            button.setVisible(true);
            button.addActionListener(new PicActionListener(file_location.get(i),file_PatientID.get(i),file_Modalities.get(i), file_body_part.get(i), file_dates.get(i), file_name.get(i), libr.getimg(i)));

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


}


