import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Upload implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser file_select = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        file_select.setDialogTitle("Choose a directory to save your file: ");
        file_select.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file_select.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("DICOM Files","dcm","dicom");
        file_select.addChoosableFileFilter(filter);

        int val = file_select.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            if (file_select.getSelectedFile().isDirectory()) {
                File file_path = file_select.getSelectedFile();
                System.out.println(file_path.getAbsolutePath());
            }
        }
    }
}
