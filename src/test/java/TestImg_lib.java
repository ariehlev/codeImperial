import Entities.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
public class TestImg_lib {
    @Test
    public void testAllMethods(){
        //Create a simple 3 image library and set all fields
        MedImage image1=new MedImage();
        MedImage image2=new MedImage();
        MedImage image3=new MedImage();
        MedicalImageLibrary testlibrary=new MedicalImageLibrary();
        ArrayList<MedImage> library=new ArrayList<MedImage>();
        image1.setPatientID("1");
        image1.setBodyPart("leg");
        image1.setFileName("legFile");
        image1.setModality("ct");
        image1.setDate("today");
        image1.setImageURL("abc.heroku.com");
        image2.setPatientID("1");
        image2.setBodyPart("arm");
        image2.setFileName("armFile");
        image2.setModality("mri");
        image2.setDate("yesterday");
        image2.setImageURL("def.heroku.com");
        image3.setPatientID("1");
        image3.setBodyPart("head");
        image3.setFileName("headFile");
        image3.setModality("ct");
        image3.setDate("twodaysago");
        image3.setImageURL("ghi.heroku.com");
        library.add(image1); //Add the 3 created images into an arraylist of arrays called library
        library.add(image2);
        library.add(image3);
        testlibrary.setLibrary(library); // pass that library into our Img_lib object called testlibrary
        //Test Img_lib getLibrary() method
        Assert.assertEquals(testlibrary.getLibrary(), library);
        //Test Img_lib getimg() method
        Assert.assertEquals(testlibrary.getImg(2), image3);
        //Test Img_lib getNames() method
        ArrayList<String> testarray =new ArrayList<String>();
        testarray.add("legFile"); testarray.add("armFile"); testarray.add("headFile");
        Assert.assertEquals(testlibrary.getNames(),testarray);
        //Test Img_lib getURLs() method
        ArrayList<String> testarray2 =new ArrayList<String>();
        testarray2.add("abc.heroku.com"); testarray2.add("def.heroku.com"); testarray2.add("ghi.heroku.com");
        Assert.assertEquals(testlibrary.getURLs(),testarray2);
        //Test Img_lib getBodyParts() method
        ArrayList<String> testarray3 =new ArrayList<String>();
        testarray3.add("leg"); testarray3.add("arm"); testarray3.add("head");
        Assert.assertEquals(testlibrary.getBodyParts(),testarray3);
        //Test Img_lib getPatientIds() method
        ArrayList<String> testarray4 =new ArrayList<String>();
        testarray4.add("1"); testarray4.add("1"); testarray4.add("1");
        Assert.assertEquals(testlibrary.getPatientIds(),testarray4);
        //Test Img_lib getModalities() method
        ArrayList<String> testarray5 =new ArrayList<String>();
        testarray5.add("ct"); testarray5.add("mri"); testarray5.add("ct");
        Assert.assertEquals(testlibrary.getModalities(),testarray5);
        //Test Img_lib getDates() method
        ArrayList<String> testarray6 =new ArrayList<String>();
        testarray6.add("today"); testarray6.add("yesterday"); testarray6.add("twodaysago");
        Assert.assertEquals(testlibrary.getDates(),testarray6);
    }
}