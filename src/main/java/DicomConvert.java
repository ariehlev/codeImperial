import ij.plugin.DICOM;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.imageio.codec.Transcoder;
import org.dcm4che3.io.DicomInputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;


public class DicomConvert {
    public static File jpgconvert (String filePath) throws IOException {
        File src = new File(filePath);
        String destPath = filePath.substring(0,filePath.length() - 4) + "temporary.dcm";
        File dest = new File(destPath);
        String imagePath = filePath.substring(0,filePath.length() - 4) + ".jpg";
        File destjpg = new File(imagePath);
        try (Transcoder transcoder = new Transcoder(src)) {
            transcoder.setDestinationTransferSyntax(UID.ExplicitVRLittleEndian);
            transcoder.transcode(new Transcoder.Handler(){
                @Override
                public OutputStream newOutputStream(Transcoder transcoder, Attributes dataset) throws IOException {
                    return new FileOutputStream(dest);
                }
            });
            DICOM dicom = new DICOM();
            dicom.run(String.valueOf(dest));
            BufferedImage bi = (BufferedImage) dicom.getImage();
            int width = bi.getWidth();
            int height = dicom.getHeight();
            System.out.println("width: " + width + "\n" + "height: " + height);
            ImageIO.write(bi, "jpg", destjpg);
            System.out.println("Hehe,Game over!!!");
        } catch (Exception e) {
            Files.deleteIfExists(dest.toPath());
            throw e;
        }
        Files.deleteIfExists(dest.toPath());
        return destjpg;
    }

    public static Img getTagByFile(String pathFile) {
        Img img = new Img();
        try {
            File file = new File(pathFile);
            DicomInputStream dis = new DicomInputStream(file);
            Attributes fmi = dis.readFileMetaInformation();
            Attributes attrs = dis.readDataset(-1, -1);
            img.setPatientID(attrs.getString(Tag.PatientID));
            img.setModality(attrs.getString(Tag.Modality));
            img.setDate(attrs.getString(Tag.StudyDate));
            img.setBodyPart(attrs.getString(Tag.BodyPartExamined));
            img.setImageURL("");
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return img;
        }
    }

    public static void main(String[] args) throws IOException {
        //File src = new File("C:\\Programming3\\test2.dcm"); //Dicom original file with compression protocol
        //File dest = new File("C:\\Programming3\\temporaryfile.dcm"); //Decompress into an uncompressed dicom target file
        String filePath = "C:\\Programming3\\Dicom\\us heart 1.dcm";
        jpgconvert(filePath);
        Img img = new Img();
        img = getTagByFile(filePath);
        System.out.println(img.getDate());
    }
}

