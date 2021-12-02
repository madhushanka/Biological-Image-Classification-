package com.bolzano.Task02;

/**
 * Evaluation of model predictions.
 * @author Yojana Gadiya - 3210282
 */

class Metrics {
    private static double false_negative = 0.0;
    private static double true_negative = 0.0;
    private static double false_positive = 0.0;
    private static double true_positive = 0.0;

    /**
     * Calculates the TP, FP, FN, TN values.
     * @param actual Integer array of true labels.
     * @param predicted Integer array of predicted labels.
     */

    private static void calcCnfMat(int[] actual, int[] predicted) {
        /*
          Creation of confusion matrix.
                                    Predicted
                            |  True (1) |  False(0) |
          Actual   True(1)  |   TP      |   FP      |
                   False(0) |   FN      |   TN      |
        */
            for (int idx = 0; idx < actual.length; idx++) {
                if (actual[idx] == 1.0 && predicted[idx] == 1.0) {
                    true_positive += 1;
                } else if (actual[idx] == 1.0 && predicted[idx] == 0.0) {
                    false_positive += 1;
                } else if (actual[idx] == 0.0 && predicted[idx] == 1.0) {
                    false_negative += 1;
                }
                if (actual[idx] == 0.0 && predicted[idx] == 0.0) {
                    true_negative += 1;
                }
            }
        }

    /**
     * Calculates the accuracy value.
     * @param actual Integer array of true labels.
     * @param predicted Integer array of predicted labels.
     * @return Accuracy value as a double variable.
     */

    public static double Accuracy(int[] actual, int[] predicted) {
        calcCnfMat(actual, predicted);
        return (true_negative + true_positive) / (true_positive + true_negative + false_positive + false_negative);
    }

    /**
     * Calculates the precision/specificity value.
     * @param actual Integer array of true labels.
     * @param predicted Integer array of predicted labels.
     * @return Precision value as a double variable.
     */

    public static double Precision(int[] actual, int[] predicted) {
        calcCnfMat(actual, predicted);
        return true_positive / (true_positive + false_positive);
    }

    /**
     * Calculates the recall/sensitivity value.
     * @param actual Integer array of true labels.
     * @param predicted Integer array of predicted labels.
     * @return Recall value as a double variable.
     */

    public static double Recall(int[] actual, int[] predicted) {
        calcCnfMat(actual, predicted);
        return true_positive / (true_positive + false_negative);
    }

    /**
     * Calculates the F1 score.
     * @param actual Integer array of true labels.
     * @param predicted Integer array of predicted labels.
     * @return F1 score as a double variable.
     */

    public static double F1_score(int[] actual, int[] predicted) {
        calcCnfMat(actual, predicted);
        double precision = Precision(actual,predicted);
        double recall = Recall(actual,predicted);
        return 2 * ((precision * recall) / (precision + recall));

    }
}
