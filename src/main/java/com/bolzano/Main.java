package com.bolzano;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
import com.bolzano.Task02.ClassifyMain;
import com.bolzano.Task02.PreProcessMain;
import com.bolzano.Task02.preprocess_ret;
import com.bolzano.Task03.ProductrestApplication;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Classification of images from biomedical literature along with service implementation.
 */

public class Main {
    // JCommander to create command line argument
    @Parameter(names={"--input", "-i"})
    private static String input = "";
    @Parameter(names = "-train", description = "Training mode")
    private static boolean train_mode = false;
    @Parameter(names = "--csv", description = "CSV File for training that describes the image file name and label for the image")
    private static String csv_file =  "";
    @Parameter(names = "-webstart", description = "Start the website and the rest_api")
    private static boolean webstart = false;

    private static Map<Integer, String> mapping_label = new HashMap<>();

    /**
     * Main method that runs the jcommander
     * @param argv Input arguments from command-line
     * @throws IOException If the input or the csv file were not found.
     * @throws InterruptedException If the thread has been interuppted
     */

    public static void main(String ... argv) throws IOException, InterruptedException {
        // Classification types map.
        mapping_label.put(0, "Anatomical_Scans");
        mapping_label.put(1, "Biological_Cartoons");
        mapping_label.put(2, "Chemical_Structures");
        mapping_label.put(3, "Graphical_Representations");
        mapping_label.put(4, "Staining_Blotting_Tests");
        mapping_label.put(5, "Textual_Information_Representations");

        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        Main.run();

        Thread t = new Thread(new timer());
        t.start(); // Kick off timer

        if(webstart) {
            System.out.println("Starting RestAPI...");
            ProductrestApplication.RestApiMain();
            System.out.println("RestAPI & the website is now working.");
            if (!train_mode) {
                if (!input.equals("")) {
                    System.out.printf("Visit http://localhost:8080/get/%s to access the website.%n", Paths.get(input).getFileName().toString());
                }
            }
            System.out.println("Visit http://localhost:8080 to access the website.");
        }
    }

    /**
     * Runs all the tasks (Pre-Processing, Classification, & RestAPI)
     * @throws IOException If the input file was not found
     */

    private static void run() throws IOException {
        database model_database;
        if (train_mode && new File(input).isFile()) {
            System.out.println("The input is not a directory, please pass a directory");
        }

        if(! input.equals("")) {
            // Training mode processes.
            if (train_mode) {
                File db = new File("model_database.db");
                if (db.exists()) {
                    db.delete();
                }

                model_database = initialize_database();
                PrintWriter out = new PrintWriter(new File("database_obj.xml"));
                new XStream().toXML(model_database, out);

                System.out.println("Database initialized.");

                // Pre-processing the image.
                System.out.println("Pre-processing data for training the logistic regression model...");
                preprocess_ret preprocessed_data = PreProcessMain.preprocess_train(input, csv_file);
                System.out.println("Pre-processing is done.");

                // Classification of image.
                System.out.println("Starting to train the model...");
                ClassifyMain.classifyTrain(preprocessed_data.getImage(), true);
                System.out.println("Model training completed.");

                // Filling the database with model predicted labels.
                int idx = 0;
                for (double[] sample : preprocessed_data.getImage().drop(-1, 1).getData()) {
                    int[] pred = ClassifyMain.classifyTest(sample, true);
                    fill_database(model_database, preprocessed_data.getMetainfo().get(idx), pred[0]);
                    idx += 1;
                }
                System.out.println("Database populated with training data.");
            }
            // Testing mode processes.
            else {
                File xmlFile = new File("database_obj.xml");
                model_database = (database) new XStream().fromXML(new FileInputStream(xmlFile));

                // Pre-processing the image.
                preprocess_ret preprocessed_data = PreProcessMain.preprocess_test(input);
                System.out.println("Input image has been pre-processed.");

                // Classification of image.
                int[] pred = ClassifyMain.classifyTest(preprocessed_data.getImage().getData()[0], true);
                System.out.println("Image has been classified.");

                // Filling the database with model predicted label.
                fill_database(model_database, preprocessed_data.getMetainfo().get(0), pred[0]);
                System.out.println("Image is now in the database.");
            }
        }
    }

    /**
     * A method to create new database.
     * @return Created database
     */

    private static database initialize_database(){
        database db = new database("model_database.db");
        db.createTable("Model_Table");

        return db;
    }

    /**
     * Method to fill classification labels to created database.
     * @param db Database object
     * @param row Hash-map to fill into the database as a row
     * @param predicted_label The classification of the image as an integer
     */

    private static void fill_database(database db, Map<String, String> row, int predicted_label){
        // Filling the data
        row.put("classification", mapping_label.get(predicted_label));
        db.insertTo("Model_Table", row);
    }
}
