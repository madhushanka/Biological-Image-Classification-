package com.bolzano.Task02;

import com.bolzano.dataframe;

/**
 * Logistic regression classifier.
 * Supported solvers :
 *  - "gd" (Gradient Descent)
 *  - "sag" (Stochastic Average Gradient descent)
 *  @author Vinay Bharadhwa - 3190098
*/

public class Logistic_Regression {
    boolean multinomial = false;
    fit_params[] results;
    LinearSolver solver;
    dataframe p;
    double[] classes;

    /**
     * Constructor that creates a solver based on the input
     * @param solver String input that can either be "gd" or "sag"
     */
    public Logistic_Regression(String solver){
        if(solver.equals("gd")){
            this.solver = new gradientDescent();
        }
        else if(solver.equals("sag")){
            this.solver = new sag();
        }
        else throw new NoClassDefFoundError("The input " + solver + " is not a valid solver");
    }

    /**
     * Method to carry out logistic regression of binary class data or pass multinomial data
     * to a different method.
     * @param X The data variable.
     * @param y The labels for the data.
     * @param theta The weights for the data.
     * @param max_epochs The maximum number of iterations for logistic regression.
     * @param lambda The regularization ratio.
     * @param learning_rate The learning rate for gradient descent or the "C" value for SAG solver.
     * @param threshold The threshold for prediction for gradient descent.
     */

    public void fit(
            dataframe X,
            dataframe y,
            dataframe theta,
            int max_epochs,
            double lambda,
            double learning_rate,
            double threshold
    ){
        // Convert the 0 values in the labels into -1 as 0's causes
        // division by 0 error during prediction.
        y = y.replace_all(0, -1);
        this.classes = dataframe.unique(y);

        // Check if the data is multinomial in nature.
        if(dataframe.unique(y).length > 2){
            this.multinomial = true;
            multinomialFit(X, y, theta, max_epochs, lambda, learning_rate, threshold);
        }
        else{
            this.solver.setParams(X, y, theta, max_epochs, lambda, learning_rate, threshold);
            this.solver.fit();
        }
    }

    /**
     * Method to carry out prediction of the data for both binary and multinomial data.
     * @return A dataframe consisting of the prediction values.
     */

    public dataframe predict(){
        if(this.multinomial){
            p = this.solver.predict(results, classes);
        }
        else {
            p = this.solver.predict();
        }
        return p.replace_all(-1, 0);
    }

    /**
     * Method to carry out logistic regression for multinomial class data.
     * @param X The data variable.
     * @param y The labels for the data.
     * @param theta The weights for the data.
     * @param max_epochs The maximum number of iterations for logistic regression.
     * @param lambda The regularization ratio.
     * @param learning_rate The "C" value for SAG solver.
     * @param threshold The threshold for prediction for gradient descent.
     */

    private void multinomialFit(
            dataframe X,
            dataframe y,
            dataframe theta,
            int max_epochs,
            double lambda,
            double learning_rate,
            double threshold
    ){
        int idx = 0;
        p = new dataframe(y.getRowDim(), y.getColDim());
        results = new fit_params[dataframe.unique(y).length];

        // Convert the multinomial data into a OneVsRest situation and deal with it
        // as a binary case which is then passed to the solver as a binary data.
        for (double unique_label : dataframe.unique(y)) {
            dataframe y_binary = new dataframe(y);
            for (int i = 0; i < y_binary.getRowDim(); i++) {
                for (int j = 0; j < y_binary.getColDim(); j++) {
                    if (y.getValue(i, j) == unique_label) y_binary.setValue(i, j, 1);
                    else if (y.getValue(i, j) != unique_label) y_binary.setValue(i, j, -1);
                }
            }

            this.solver.setParams(
                    X,
                    y_binary,
                    theta,
                    max_epochs,
                    lambda,
                    learning_rate,
                    threshold
            );

            // Save the results in a array of fit_param which consists of the
            // tuned weights and the intercept values.
            results[idx] = this.solver.fit(true);

            idx++;
        }
    }

}
