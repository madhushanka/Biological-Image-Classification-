package com.bolzano.Task02;

import com.bolzano.dataframe;

import java.util.ArrayList;
import java.util.Map;

/**
 * Datatype for containing image dataframe and meta-information values
 * for storage of the pre-processed image and the image's meta-information.
 * @author Yojana Gadiya - 3210282
 */

public class preprocess_ret {
    private dataframe image;
    private ArrayList<Map<String, String>> metainfo;

    // Constructor.
    public preprocess_ret(dataframe image, ArrayList<Map<String, String>> metainfo){
        this.image = image;
        this.metainfo = metainfo;
    }

    // Getter method.
    public dataframe getImage() {
        return image;
    }

    // Getter method.
    public ArrayList<Map<String, String>> getMetainfo() {
        return metainfo;
    }
}
