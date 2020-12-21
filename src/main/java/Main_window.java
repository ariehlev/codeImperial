import javax.swing.*;

public class Main_window extends Interface {

    public Main_window(JPanel images,JFrame frame,JCheckBox ch1,JCheckBox ch2,JCheckBox ch3,JCheckBox ch4,JCheckBox ch5,JCheckBox ch6,JTextField start, JTextField end,JTextField id) {
        super(images,frame,ch1,ch2,ch3,ch4,ch5,ch6,start,end,id);
    }

    public static void main(String args[]){
        inter();
    }
}