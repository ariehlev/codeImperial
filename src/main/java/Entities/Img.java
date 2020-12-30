package Entities;

//Class containing individual image data received from the server
public class Img {
    protected int ID;
    protected String FileName;
    protected String PatientID;
    protected String Modality;
    protected String BodyPart;
    protected String Date;
    protected String ImageURL;

    //Getters and setters for variables
    public String getFileName() { return FileName; }

    public void setFileName(String fileName) { FileName = fileName;}

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getModality() { return Modality; }

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
