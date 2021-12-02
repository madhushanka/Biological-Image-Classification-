package com.bolzano.Task02;

import com.bolzano.dataframe;
import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Involves pre-processing of image along with meta-information storage.
 * @author Yojana Gadiya
 */

public class PreProcessMain {

    /**
     * Method created for training purposes.
     * @param main_folder_path Path of the folder containing the images.
     * @param csv_file Path of csv file containing the label of each image.
     * @return A pre-processed image for classification and meta-information of the original image for database storage.
     * @throws IOException if input file or folder not found.
     */

    public static preprocess_ret preprocess_train(String main_folder_path, String csv_file) throws IOException {
        // Initializing objects.
        ArrayList<Map<String,String>> metainfo = new ArrayList<>();
        image_processing Image_Processing = new image_processing();

        File folder = new File(main_folder_path);
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;

        String[] image_types = {".png",".jpg",".gif", ".jpeg", ".JPEG", ".PNG", ".GIF", ".JPG"};

        dataframe image_preprocessed = new dataframe(1, 257);

        // Loading the 1st 2 columns from csv to a hash-map.
        CSVReader reader = new CSVReader(new FileReader(csv_file));
        String[] line;
        Map<String, Double> csv_data = new HashMap<>();
        while ((line = reader.readNext()) != null) {
            csv_data.put(line[0], Double.valueOf(line[1]));
        }

        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(! Arrays.asList(image_types).contains(file.getName().substring(file.getName().lastIndexOf('.'))))
                {
                    System.out.printf("The file '%s' is not a valid image type, and hence is skipped %n", file.getName());
                    continue;
                }

                // Conversion of image to Base64 String
                byte[] fileContent = FileUtils.readFileToByteArray(new File(main_folder_path, file.getName()));
                String image_base64 = Base64.getEncoder().encodeToString(fileContent);

                Path path = Paths.get(main_folder_path, file.getName());

                // Pre-processing of image.
                dataframe img = Image_Processing.Pre_Processing(path);
                dataframe label = new dataframe(new double[][] {{ csv_data.get(file.getName()) }});

                img = img.hstack(label);
                image_preprocessed = image_preprocessed.vstack(img);

                // Extraction of metadata of original image.
                Map<String,String> info = Image_Processing.Metadata_Extraction(path);
                info.put("image_base46", image_base64);
                metainfo.add(info);
            }
        }
        return new preprocess_ret(image_preprocessed.drop(1, 0), metainfo);
    }



    /**
     * Method created for testing purposes.
     * @param main_file Path of the image.
     * @return A pre-processed image for classification and meta-information of the original image for database storage.
     */

    public static preprocess_ret preprocess_test(String main_file) throws IOException {
        // Initializing objects.
        ArrayList<Map<String,String>> metainfo = new ArrayList<>();
        image_processing Image_Processing = new image_processing();

        dataframe image_preprocessed = new dataframe(1, 256);

        byte[] fileContent = FileUtils.readFileToByteArray(new File(main_file));
        String image_base64 = Base64.getEncoder().encodeToString(fileContent);

        // Pre-processing of image.
        dataframe img = Image_Processing.Pre_Processing(Paths.get(main_file));
        image_preprocessed = image_preprocessed.vstack(img);

        // Extraction of metadata of original image.
        Map<String,String> info = Image_Processing.Metadata_Extraction(Paths.get(main_file));
        info.put("image_base46", image_base64);
        metainfo.add(info);

        return new preprocess_ret(image_preprocessed.drop(1, 0), metainfo);
    }


}
