import Entities.*;
import org.junit.Assert;
import org.junit.Test;
public class TestImg {

    @Test
    public void testFilenameAssign(){
        Img img=new Img();
        String file="file";
        img.setFileName(file);
        Assert.assertEquals(img.getFileName(),file);
    }
    @Test
    public void testPatientIdAssign(){
        Img img=new Img();
        String id="123";
        img.setPatientID(id);
        Assert.assertEquals(img.getPatientID(),id);
    }
    @Test
    public void testModalityAssign(){
        Img img=new Img();
        String mod="ct";
        img.setModality(mod);
        Assert.assertEquals(img.getModality(),mod);
    }
    @Test
    public void testBodyPartAssign(){
        Img img=new Img();
        String body="feet";
        img.setBodyPart(body);
        Assert.assertEquals(img.getBodyPart(),body);
    }
    @Test
    public void testDateAssign(){
        Img img=new Img();
        String date="2008";
        img.setDate(date);
        Assert.assertEquals(img.getDate(),date);
    }
    @Test
    public void testURLAssign(){
        Img img=new Img();
        String url="http";
        img.setImageURL(url);
        Assert.assertEquals(img.getImageURL(),url);
    }
}
