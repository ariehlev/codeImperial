package Entities;

//Class containing search parameter information which will be sent to server
public class SearchParameters {
    protected String PatientID;
    protected String[] Modality;
    protected String[] BodyPart;
    protected String[] Date;

    //methods for setting searchParameter data
    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public void setModality(String[] modality) {
        Modality = modality;
    }

    public void setBodyPart(String[] bodyPart) {
        BodyPart = bodyPart;
    }

    public void setDate(String[] date) {
        Date = date;
    }

}