
import java.io.Serializable;

public class Img implements Serializable {
    protected String PatientID;
    protected String Modality;
    protected String BodyPart;
    protected String Date;
    protected String ImageURL;

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getModality() {
        return Modality;
    }

    public void setModality(String modality) {
        Modality = modality;
    }

    public String getBodyPart() {
        return BodyPart;
    }

    public void setBodyPart(String bodyPart) {
        BodyPart = bodyPart;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}