package Entities;

import java.util.ArrayList;

//Class storing all images received from server in an array list
public class MedicalImageLibrary {
    protected ArrayList<MedImage> Library = new ArrayList<>();

    //Methods to retrieve information from image library
    public boolean isEmpty(){
        return Library.isEmpty();
    }

    public MedImage getImg(int i){
        return Library.get(i);
    }

    public ArrayList<String> getNames (){
        ArrayList<String> array = new ArrayList<>();
        for (MedImage img : Library) {
            array.add(img.getFileName());
        }
        return array;
    }

    public ArrayList<String> getURLs (){
        ArrayList<String> array = new ArrayList<>();
        for (MedImage img : Library) {
            array.add(img.getImageURL());
        }
        return array;
    }
    public ArrayList<String> getBodyParts (){
        ArrayList<String> array = new ArrayList<>();
        for (MedImage img : Library) {
            array.add(img.getBodyPart());
        }
        return array;
    }

    public ArrayList<String> getPatientIDs (){
        ArrayList<String> array = new ArrayList<>();
        for (MedImage img : Library) {
            array.add(img.getPatientID());
        }

        return array;
    }
    public ArrayList<String> getModalities () {
        ArrayList<String> array = new ArrayList<>();
        for (MedImage img : Library) {
            array.add(img.getModality());
        }
        return array;
    }

    public ArrayList<String> getDates() {
        ArrayList<String> array = new ArrayList<>();
        for (MedImage img : Library) {
            array.add(img.getDate());
        }
        return array;
    }

    public ArrayList<MedImage> getLibrary() {
        return Library;
    }

    public void setLibrary(ArrayList<MedImage> library) {
        Library = library;
    }
}