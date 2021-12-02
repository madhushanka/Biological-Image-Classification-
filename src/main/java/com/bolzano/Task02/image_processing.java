package com.bolzano.Task02;

import com.bolzano.dataframe;
import ij.ImagePlus;
import ij.io.FileInfo;
import ij.plugin.ContrastEnhancer;
import ij.plugin.ImageInfo;
import ij.plugin.filter.BackgroundSubtracter;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.nio.file.Path;
import java.util.*;

/**
 * Pre-Processing of image and meta-information extraction of given image.
 * @author Yojana Gadiya - 3210282
 */

public class image_processing {

    /**
     * Method to pre-process the image.
     * @param path A path of file
     * @return A dataframe containing pixel intensity values of the pre-processed image.
     */

    public dataframe Pre_Processing (Path path){
        dataframe final_output = new dataframe(1,256);

        try {
                ImagePlus imgPlus = new ImagePlus(path.toString());

                // Converting image to 8-bit.
                ImageConverter grayImg = new ImageConverter(imgPlus);
                grayImg.convertToGray8();

                // Contrast enhancement.
                ContrastEnhancer contrastEnhancer = new ContrastEnhancer();
                contrastEnhancer.equalize(imgPlus);

                // Background subtraction.
                ImageProcessor imageProcessor = imgPlus.getProcessor();
                BackgroundSubtracter backgroundSubtracter = new BackgroundSubtracter();
                backgroundSubtracter.subtractBackround(imageProcessor, 50);


//                This loop will iterate over each pixel in the image and get its respective
//                value and create a value matrix for further use.
//                Input - Image
//                Output - N-dimensional matrix with pixel value of the image


                // Image value dataframe to get value for each pixel
                dataframe value_dataframe = new dataframe(imgPlus.getDimensions()[0], imgPlus.getDimensions()[1]);
                for (int x = 0; x < imgPlus.getDimensions()[0]; x++) {
                    for (int y = 0; y < imgPlus.getDimensions()[1]; y++) {
                        value_dataframe.setValue(x, y, imageProcessor.getPixelValue(x, y));
                    }
                }

//                This loop will iterate over each pixel in the value matrix and at value found at that
//                pixel will be used as an index for the image matrix and take the corresponding value
//                from the image matrix (initialised with 0's) and add 1 to it every time it occurs in
//                the value matrix.
//                Input - N-dimensional matrix of image with pixel values
//                Output - 1D Matrix of the image (equivalent to histogram)


                // Dataframe with non-binary value
                for (int width = 0; width < value_dataframe.getRowDim(); width++) {
                    for (int height = 0; height < value_dataframe.getColDim(); height++) {
                        int value_matrixValue = (int) value_dataframe.getValue(width, height);
                        double img_matrixValue = final_output.getValue(0, value_matrixValue);
                        double add = 1d;
                        img_matrixValue += add;
                        final_output.setValue(0, value_matrixValue, img_matrixValue);
                    }
                }
        } catch (NullPointerException e) {
            System.out.println("The image is bigger than expected.");
        }
        return final_output;
    }

    /**
     * Method to extract the meta-information of original image.
     * @param path A path of file
     * @return A hash table with title/header-value of each meta-information as key,value pair.
     */

    public Map<String, String> Metadata_Extraction(Path path) {

        String filename = path.getFileName().toString();
        String extension = filename.substring(filename.lastIndexOf(".") + 1);

        ImagePlus imgPlus = new ImagePlus(path.toString());

        // Creating hashtable to store value as header, value pair.
        Map<String,String> metadata = new HashMap<>();

        // Abstracting meta-data information.
        ImageInfo img = new ImageInfo();
        String imgInfo = img.getImageInfo(imgPlus);
        String[] basic_info = imgInfo.split("\\R");

        FileInfo fileInfo = new FileInfo();
        fileInfo = imgPlus.getFileInfo();
        String info = fileInfo.toString();

        String[] addInfo = info.split(",\\s");

        // Modifications of value for efficient data analysis in database.
        for (String i : basic_info) {
            if (i.contentEquals("Uncalibrated")) { i = "Calibration: False"; }

            // For values like No threshold, No ROI etc.
            else if (i.contains("No")) {
                String title = i.split("\\s")[1];
                String answer = i.split("\\s")[0];

                if (answer.contentEquals("No")) { i = title.concat(": False"); }
                else { i = title.concat(": True"); }
            }

            String header = i.split(":\\s+")[0];
            String value = i.split(":\\s+")[1];

            if (header.toLowerCase().contentEquals("path")) { value = value.replace(value,filename); }
            if (header.contentEquals("Pixel size") || header.contentEquals("Height") || header.contentEquals("Width") ||
                    header.contentEquals("Display range") || header.contentEquals("Title")) { continue; }
            else {
                if (header.contains(" ")) {
                    String new_header = header.replace(" ", "_");
                    metadata.put(new_header.toLowerCase(), value);
                    }
                else { metadata.put(header.toLowerCase(), value); }
            }

        }

        // Modifications of value for efficient data analysis in database.
        for (String j : addInfo) {
            String head;
            String val;
            try {
                head = j.split("=")[0];
                val = j.split("=")[1];

                // whiteIsZero has boolean value : t/f
                if (head.contentEquals("whiteIsZero")) {
                    if (val.contentEquals("f")) { val = "False"; }
                    else { val = "True"; }
                }
                else if (head.contentEquals("format")) { val = extension; }
                else if (head.contentEquals("ranges") & val.contentEquals("null")) { val = "NaN"; }
                else if (head.toLowerCase().contentEquals("path")) { val = val.replace(val,filename); }

                if (head.contains(" ")) { head = head.replace(" ", "_"); }

                metadata.put(head.toLowerCase(), val);
            }
            catch (ArrayIndexOutOfBoundsException ignored) { }
        }
       return metadata;
    }
}

