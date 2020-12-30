import Entities.*;
import org.junit.Assert;
import org.junit.Test;
public class TestImg {

    @Test
    public void testFilenameAssign(){
        MedImage img=new MedImage();
        String file="file";
        img.setFileName(file);
        Assert.assertEquals(img.getFileName(),file);
    }
    @Test
    public void testPatientIdAssign(){
        MedImage img=new MedImage();
        String id="123";
        img.setPatientID(id);
        Assert.assertEquals(img.getPatientID(),id);
    }
    @Test
    public void testModalityAssign(){
        MedImage img=new MedImage();
        String mod="ct";
        img.setModality(mod);
        Assert.assertEquals(img.getModality(),mod);
    }
    @Test
    public void testBodyPartAssign(){
        MedImage img=new MedImage();
        String body="feet";
        img.setBodyPart(body);
        Assert.assertEquals(img.getBodyPart(),body);
    }
    @Test
    public void testDateAssign(){
        MedImage img=new MedImage();
        String date="2008";
        img.setDate(date);
        Assert.assertEquals(img.getDate(),date);
    }
    @Test
    public void testURLAssign(){
        MedImage img=new MedImage();
        String url="http";
        img.setImageURL(url);
        Assert.assertEquals(img.getImageURL(),url);
    }
}
