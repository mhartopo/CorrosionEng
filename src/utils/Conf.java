package utils;

import java.io.File;

import javafx.stage.FileChooser;

public class Conf {
	public static void configureSelectFile(final FileChooser fileChooser){                           
        fileChooser.setTitle("Select File");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        ); 
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("LCS File", "*.lcs"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
            );
    }
}
