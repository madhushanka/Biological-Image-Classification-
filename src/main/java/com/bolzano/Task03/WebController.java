package com.bolzano.Task03;


import com.bolzano.Task02.ClassifyMain;
import com.bolzano.Task02.PreProcessMain;
import com.bolzano.Task02.preprocess_ret;
import com.bolzano.database;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This is used to control and manage the website.
 */

@Controller
public class WebController implements ErrorController {

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String homePage(Model model) {
        return "index";
    }

    /**
     * Gets the image id from the input field from the website as a POST query and returns
     * redirects to the results page and injects the image and its
     * classification to the webpage.
     * @param model Springboot model
     * @param image_id image path from the input field
     * @return Returns the name of the html file to the springboot application.
     */

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getImage_id(Model model, @RequestParam("image_path") String image_id) {
        model.addAttribute("image_path", service.get(image_id).getImage_base46());
        model.addAttribute("classification", service.get(image_id).getClassification());
        return "results";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getImage_file(Model model, @RequestParam("file") MultipartFile image_file) throws IOException {
        // Get the name of the original image
        String image_id = image_file.getOriginalFilename();
        assert image_id != null;

        // Get the content of the image as a byte array
        byte[] image_data = image_file.getBytes();

        // Create a temporary folder if it does not exist
        new File("tmp/").mkdirs();

        // Convert the byte array to a file for processing and classification
        ByteArrayInputStream bis = new ByteArrayInputStream(image_data);
        BufferedImage bImage2 = ImageIO.read(bis);
        String format = image_id.substring(image_id.lastIndexOf('.') + 1);
        image_id = image_id.replace(format, "jpg");
        ImageIO.write(bImage2, "jpg", new File("tmp/" + image_id));

        ProcessImage("tmp/" + image_id);

        // Add the attributes from the database to the model and pass to the webpage
        model.addAttribute("image_path", service.get(image_id).getImage_base46());
        model.addAttribute("classification", service.get(image_id).getClassification());

        // Delete the file and the temporary folder
        new File("tmp/" + image_id).delete();
        new File("tmp/").delete();

        return "results";
    }

    @GetMapping("/error")
    public String errorPage(Model model, HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        model.addAttribute("status_code", statusCode);
        model.addAttribute("error_message", exception.getCause());
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private void ProcessImage(String image_file) throws IOException {
        File xmlFile = new File("database_obj.xml");
        database model_database = (database) new XStream().fromXML(new FileInputStream(xmlFile));

        // Pre-processing the image.
        preprocess_ret preprocessed_data = PreProcessMain.preprocess_test(image_file);

        // Classification of image.
        int[] pred = ClassifyMain.classifyTest(preprocessed_data.getImage().getData()[0], true);

        // Filling the database with model predicted label.
        fill_database(model_database, preprocessed_data.getMetainfo().get(0), pred[0]);
    }

    private static void fill_database(database db, Map<String, String> row, int predicted_label){
        // Filling the data
        Map<Integer, String> mapping_label = new HashMap<>();
        mapping_label.put(0, "Anatomical_Scans");
        mapping_label.put(1, "Biological_Cartoons");
        mapping_label.put(2, "Chemical_Structures");
        mapping_label.put(3, "Graphical_Representations");
        mapping_label.put(4, "Staining_Blotting_Tests");
        mapping_label.put(5, "Textual_Information_Representations");

        row.put("classification", mapping_label.get(predicted_label));
        db.insertTo("Model_Table", row);
    }
}
