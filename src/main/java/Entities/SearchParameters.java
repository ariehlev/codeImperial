package Entities;

//Class containing search parameter information which will be sent to server
public class SearchParameters {
    protected String PatientID;
    protected String[] Modality;
    protected String[] BodyPart;
    protected String[] Date;

    //Getters and setters for searchParameter data
    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setModality(String[] modality) {
        Modality = modality;
    }

    public String[] getModality() {
        return Modality;
    }

    public void setBodyPart(String[] bodyPart) {
        BodyPart = bodyPart;
    }

    public String[] getBodyPart() {
        return BodyPart;
    }

    public void setDate(String[] date) {
        Date = date;
    }

    public String[] getDate() {
        return Date;
    }

}