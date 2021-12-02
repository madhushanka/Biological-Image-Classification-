package com.bolzano.Task02;

import com.bolzano.dataframe;
import smile.classification.LogisticRegression;
import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.util.Arrays;

/**
 * Involves classification using logistic regression, along with metrics calculation for the model.
 * @author Vinay Bharadhwaj - 3190098
 */

public class ClassifyMain {
    /**
     * Carries out training of logistic regression model using either smile package or
     * self implemented logistic regression model using stochastic average gradient descent.
     * @param data The dataframe that is used for training of the model.
     * @param use_smile The boolean value to indicate if smile package must be used.
     * @throws FileNotFoundException if model.xml file is not found.
     */

    public static void classifyTrain(dataframe data, boolean use_smile) throws FileNotFoundException {
        if (use_smile) smileLRTrain(data);
        else SGDLRTrain(data);
    }

    /**
     * Carries out testing of logistic regression model using either smile package or
     * self implemented logistic regression model using stochastic average gradient descent.
     * @param data A 1D double array that is used for testing of the model.
     * @param use_smile The boolean value to indicate if smile package must be used.
     * @return predictions as an integer array.
     * @throws FileNotFoundException if model.xml file is not found.
     */

    public static int[] classifyTest(double[] data, boolean use_smile) throws FileNotFoundException {
        int[] pred = new int[0];
        if (use_smile) pred = smileLRTest(data);
        else pred = SGDLRTest();

        return pred;
    }

    /**
     * Carries out training of smile package's logistic regression model.
     * @param data The data that is used for training of the model.
     * @throws FileNotFoundException if model.xml file is not found.
     */

    public static void smileLRTrain(dataframe data) throws FileNotFoundException {
        XStream xs = new XStream();
        LogisticRegression logit;
        int[] pred;

        // "regularized" estimate of linear weights.
        double lambda = 1;

        // Split the data into testing and training dataset
        dataframe[] train_test = dataframe.trainTestSplit(data, 0.8);
        dataframe train = train_test[0];
        dataframe test = train_test[1];

        // Assign the last column of the data as the label.
        dataframe X_train = new dataframe(train.drop(-1, 1));
        dataframe y_train = new dataframe(train.getCol(-1));

        dataframe X_test = new dataframe(test.drop(-1, 1));
        dataframe y_test = new dataframe(test.getCol(-1));

        // Convert the labels from dataframe to 1D array
        final int[] train_label = new int[y_train.getFlatCol(1).length];
        for (int i=0; i<train_label.length; ++i)
            train_label[i] = (int) y_train.getFlatCol(1)[i];

        final int[] test_label = new int[y_test.getFlatCol(1).length];
        for (int i=0; i<test_label.length; ++i)
            test_label[i] = (int) y_test.getFlatCol(1)[i];

        //      TODO: make it parameterized & Hyper-parameter optimization
        // Run the logistic regression for 200 epochs and check convergence
        // with tolerance/accuracy of 10^(-11).
        logit = LogisticRegression.fit(
                X_train.getData(),
                train_label,
                lambda,
                1E-11,
                200
        );

        // Save the model to a xml file.
        PrintWriter out = new PrintWriter(new File("model.xml"));
        xs.toXML(logit, out);

        // Get the predicted labels from the model.
        pred = new int[test_label.length];
        for (int i = 0; i < test_label.length; i++) {
            pred[i] = logit.predict(X_test.getData()[i]);
        }

        // Calculate the Accuracy, Precision, Recall & F1 scores.
        double[] metrics = calcMultinomalMetrics(test_label, pred);

        System.out.println("Training Metrics: ");
        System.out.println("------------------------------------------");
        System.out.printf("\tAccuracy = %.2f%%\n", 100*metrics[0]);
        System.out.printf("\tPrecision = %.2f%%\n", 100*metrics[1]);
        System.out.printf("\tRecall = %.2f%%\n", 100*metrics[2]);
        System.out.printf("\tF1 Score = %.2f%%\n", 100*metrics[3]);
        System.out.println("------------------------------------------");

    }

    /**
     * Carries out training of smile package's logistic regression model.
     * @param data The data that is used for training of the model.
     * @return predictions as an integer array
     * @throws FileNotFoundException if model.xml file is not found.
     */

    public static int[] smileLRTest(double[] data) throws FileNotFoundException {
        XStream xs = new XStream();
        LogisticRegression logit;
        int[] pred;

        // Load the model from the xml file.
        File xmlFile = new File("model.xml");
        logit = (LogisticRegression) xs.fromXML(new FileInputStream(xmlFile));

        // Calculate the prediction from the saved model.
        pred = new int[1];
        pred[0] = logit.predict(data);
        return pred;
    }

    /**
     * Carries out training of logistic regression model using
     * stochastic average gradient descent.
     * @param data The data that is used for training of the model.
     * @throws FileNotFoundException if model.xml file is not found.
     */

    public static void SGDLRTrain(dataframe data) throws FileNotFoundException {
        Logistic_Regression lr = new Logistic_Regression("sag");

        // Assign the last column of the data as the label.
        dataframe X_mat = new dataframe(data.drop(-1, 1));
        dataframe y_mat = new dataframe(data.getCol(-1));

        // Initialize the weights as random values.
        dataframe theta_t = dataframe.random(1, X_mat.getColDim());

        int max_epochs = 200;
        double lambda = 1;
        double learning_rate = 1.7;
        double threshold = 0.5;

        // Run the logistic regression for 200 epochs.
        lr.fit(X_mat, y_mat, theta_t, max_epochs, lambda, learning_rate, threshold);
        dataframe pred_mat = lr.predict();

        // Save the model to a xml file.
        PrintWriter out = new PrintWriter(new File("model.xml"));
        new XStream().toXML(lr, out);

        // Convert the labels from dataframe to 1D array.
        final int[] label = new int[y_mat.getFlatCol(1).length];
        for (int i=0; i<label.length; ++i)
            label[i] = (int) y_mat.getFlatCol(1)[i];

        final int[] pred = new int[pred_mat.getFlatCol(1).length];
        for (int i=0; i<pred.length; ++i)
            pred[i] = (int) pred_mat.getFlatCol(1)[i];

        // Calculate the Accuracy, Precision, Recall & F1 scores.
        double[] metrics = calcMultinomalMetrics(label, pred);

        System.out.println(Arrays.toString(pred));
        System.out.println();

        System.out.printf("Accuracy = %.2f%%\n", 100*metrics[0]);
        System.out.printf("Precision = %.2f%%\n", 100*metrics[1]);
        System.out.printf("Recall = %.2f%%\n", 100*metrics[2]);
        System.out.printf("F1 Score = %.2f%%\n", 100*metrics[3]);
    }

    /**
     * Carries out training of logistic regression model using
     * stochastic average gradient descent.
     * @return predictions as an integer array
     * @throws FileNotFoundException if model.xml file is not found.
     */

    public static int[] SGDLRTest() throws FileNotFoundException {
        XStream xs = new XStream();
        Logistic_Regression lr;
        int[] pred;

        // Load the model from the xml file.
        File xmlFile = new File("model.xml");
        lr = (Logistic_Regression) xs.fromXML(new FileInputStream(xmlFile));

        // Calculate the prediction from the saved model.
        dataframe pred_mat = lr.predict();

        // Convert the predicted dataframe into a 1D int array.
        pred = new int[pred_mat.getFlatCol(1).length];
        for (int i=0; i<pred.length; ++i)
            pred[i] = (int) pred_mat.getFlatCol(1)[i];

        return pred;
    }

    /**
     * Carries out calculation of scoring metrics (Accuracy, Precision, Recall & F1 scores)
     * for multinomial data.
     * @param label - True labels.
     * @param pred - Predicted labels.
     * @return A 1D double array containing values of Accuracy, Precision, Recall & F1 scores.
     */

    private static double[] calcMultinomalMetrics(int[] label, int[] pred){
        int[] unique_labels = Arrays.stream(label).distinct().toArray();
        double accuracy = 0;
        double precision = 0;
        double recall = 0;
        double f1_score = 0;

        // Convert the multinomial data into a OneVsRest situation and deal with it
        // as a binary case which is then passed to the metric calculation function.
        for (double unique_label : unique_labels) {
            int[] label_binary = new int[label.length];
            int[] pred_binary = new int[pred.length];
            for (int idx = 0; idx < label_binary.length; idx++) {
                if (label[idx] == unique_label) label_binary[idx] = 1;
                else if (label[idx] != unique_label) label_binary[idx] = 0;

                if (pred[idx] == unique_label) pred_binary[idx] = 1;
                else if (pred[idx] != unique_label) pred_binary[idx] = 0;
            }
            accuracy += Metrics.Accuracy(label_binary, pred_binary);
            precision += Metrics.Precision(label_binary, pred_binary);
            recall += Metrics.Recall(label_binary, pred_binary);
            f1_score += Metrics.F1_score(label_binary, pred_binary);
        }

        // Once the OneVsRest values are added up, it is then averaged
        // to get the multinomial metrics.
        accuracy /= unique_labels.length;
        precision /= unique_labels.length;
        recall /= unique_labels.length;
        f1_score /= unique_labels.length;

        return new double[] {accuracy, precision, recall, f1_score};
    }
}
