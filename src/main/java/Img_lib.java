import java.io.Serializable;
import java.util.ArrayList;

public class Img_lib implements Serializable {
    protected ArrayList<Img> Library = new ArrayList<>();

    public ArrayList<Img> getLibrary() {
        return Library;
    }

    public void setLibrary(ArrayList<Img> library) {
        Library = library;
    }

    public void Details(){
        for(int i = 0; i<Library.size(); i++) {
            System.out.println(Library.get(i).getPatientID());
        }
    }
    public Integer getSize(){
        return Library.size();
    }
    public ArrayList<String> getURL (){
        ArrayList<String> array = new ArrayList<String>();
        for (int i=0; i < Library.size(); i++){
            array.add(Library.get(i).getImageURL());
        }
        return array;
    }

    public ArrayList<String> getPatientId (){
        ArrayList<String> array = new ArrayList<String>();
        for (int i=0; i < Library.size(); i++){
            array.add(Library.get(i).getPatientID());
        }

        return array;
    }
    public ArrayList<String> getModality () {
        ArrayList<String> array = new ArrayList<String>();
        for (int i = 0; i < Library.size(); i++) {
            array.add(Library.get(i).getModality());
        }
        return array;
    }
}