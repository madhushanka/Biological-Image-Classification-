package com.bolzano.Task02;

import com.bolzano.dataframe;

import java.util.*;

/**
 * Stochastic average gradient (SAG) method for optimizing the sum of a finite number of
 * smooth convex functions.  Like stochastic gradient (SG) methods, the SAG method’s
 * iteration cost is independent of the number of terms in the sum.
 *
 *  @author Mark Schmidt, Nicolas Le Roux, and Francis Bach
 * Minimizing Finite Sums with the Stochastic Average Gradient
 * https://hal.inria.fr/hal-00860051/document
 * @author Vinay Bharadhwa - 3190098
*/

public class sag implements LinearSolver {
    dataframe X;
    dataframe y;
    dataframe theta; // Weights
    int max_epochs;
    double lambda; // Regression parameter
    double C;
    double threshold;
    double intercept;

    /**
     * Setter method for setting the following parameters.
     * @param X The data variable.
     * @param y The labels for the data.
     * @param theta The weights for the data.
     * @param max_epochs The maximum number of iterations for logistic regression.
     * @param lambda The regularization ratio.
     * @param learning_rate The learning rate for gradient descent or the "C" value for SAG solver.
     * @param threshold The threshold for prediction for gradient descent.
     */

    @Override
    public void setParams(
            dataframe X,
            dataframe y,
            dataframe theta,
            int max_epochs,
            double lambda,
            double learning_rate,
            double threshold
    ) {
        this.X = X;
        this.y = y;
        this.theta = theta;
        this.max_epochs = max_epochs;
        this.lambda = lambda;
        this.C = learning_rate;
        this.threshold = threshold;
    }

    /**
     * Method to calculate log for every element in the dataframe.
     * @param A The dataframe on which exponent is applied on.
     * @return A dataframe whose exponent has been calculated.
     */

    private dataframe exp(dataframe A){
        for (int i = 0; i < A.getRowDim(); i++) {
            for (int j = 0; j < A.getColDim(); j++) {
                A.setValue(i, j, Math.exp(A.getValue(i, j)));
            }
        }
        return A;
    }

    /**
     * Method to carry out the log loss function.
     * @param p The dataframe consisting of the dot product of the
     *          batch entry and the corresponding weights.
     * @param y The label dataframe.
     * @return A dataframe which has been processed by the sigmoid function.
     */

    private dataframe log_dloss(dataframe p, dataframe y){
        dataframe z = p.el_mul(y);
        // Approximately equal to taking log but saves the time of calculation of the logarithm.
        if(z.greater_than(18.0)){
            z = z.el_mul(-1);
            return exp(z).el_mul(y.el_mul(-1));
        }
        if(z.lesser_than(-18.0)){
            return y.el_mul(-1);
        }
        return exp(z).plus(1.0).el_div(y.el_mul(-1));
    }

    /**
     * Method to carry out SAG to minimize logistic regression.
     */

    @Override
    public void fit() {
        // Initializes the values
        int n_samples = this.X.getRowDim();
        int n_features = this.X.getColDim();
        double tol = 1e-11;

        dataframe sum_grad = dataframe.zeros(n_features);
        dataframe grad_memory = dataframe.zeros(n_samples, n_features);

        dataframe prev_theta = new dataframe(theta.getRowDim(), theta.getColDim());

        this.intercept = 0.0;
        double intercept_sum_grad = 0.0;
        dataframe intercept_grad_memory = dataframe.zeros(n_samples);

        double decay = 1.0; // Rate at which learning rate decays.
        Set<Integer> seen = new HashSet<>();

        // Finding the step size based on "C".
        double add_val = 1.0 + (4.0 * C);
        double step_size = (4.0 / (dataframe.max(dataframe.sum(X.el_mul(X), 1).plus(add_val))));

        // Core calculation.
        for(int epoch=0; epoch < 1; epoch++){
            for(int k=0; k < 1; k++){
                int idx = (int)(Math.random() * (n_samples));
                // Single entry is drawn from the dataset.
                dataframe entry = this.X.getRow(idx);
                seen.add(idx);

                // The p value is calculated by carrying out the dot
                // product of the entry and the corresponding weights (theta).
                dataframe p = entry.dot(this.theta.transpose()).plus(intercept);
                dataframe grad = log_dloss(p, this.y.getRow(idx));

                // New theta values are calculated.
                dataframe update_left = entry.el_mul(grad);
                dataframe update_right = theta.el_mul(step_size);
                dataframe update = update_left.plus(update_right);

                // Gradient value is calculated using the update value.
                dataframe grad_correction = update.minus(grad_memory.getRow(idx));
                sum_grad = sum_grad.plus(grad_correction);
                grad_memory.setRow(idx, update);

                // Gradient is corrected.
                double grad_correction_val  = grad.getData()[0][0] - intercept_grad_memory.getValue(0, idx);

                intercept_grad_memory.setValue(0, idx, grad.getData()[0][0]);
                intercept_sum_grad += grad_correction_val;

                // Intercept value is calculated using the learning rate and the decay.
                this.intercept -= (((step_size * intercept_sum_grad) / seen.size()) * decay);

                // Theta values are updated using the gradient and the step size.
                theta = theta.minus(sum_grad.el_mul(step_size).el_div(seen.size()));
            }

            // Check convergence.
            double max_change = 0.0;
            double max_weight = 0.0;
            for (int i = 0; i < theta.getRowDim(); i++) {
                for (int j = 0; j < theta.getColDim(); j++) {
                    max_weight = Math.max(max_weight, Math.abs(theta.getData()[i][j]));
                    max_change = Math.max(max_change, Math.abs(theta.getData()[i][j] - prev_theta.getData()[i][j]));

                    prev_theta = new dataframe(theta.getData());
                }
            }
            if ((max_weight != 0 && max_change / max_weight <= tol) || max_weight == 0 && max_change == 0){
                System.out.printf("Convergence occurred after %d epochs %n", epoch);
                break;
            }
        }
    }

    /**
     * Method to carry out SAG to minimize logistic regression.
     * @param ret_fit Boolean value to indicate if the theta and intercept should be returned
     * @return A fit_params object containing theta and intercept values.
     */

    @Override
    public fit_params fit(boolean ret_fit) {
        // Initializes the values.
        int n_samples = this.X.getRowDim();
        int n_features = this.X.getColDim();
        double tol = 1e-11;

        dataframe sum_grad = dataframe.zeros(n_features);
        dataframe grad_memory = dataframe.zeros(n_samples, n_features);

        dataframe prev_theta = new dataframe(theta.getRowDim(), theta.getColDim());

        this.intercept = 0.0;
        double intercept_sum_grad = 0.0;
        dataframe intercept_grad_memory = dataframe.zeros(n_samples);

        double decay = 1.0; // Rate at which learning rate decays.
        Set<Integer> seen = new HashSet<>();

        // Finding the step size based on "C".
        double add_val = 1.0 + (4.0 * C);
        double step_size = (4.0 / (dataframe.max(dataframe.sum(X.el_mul(X), 1).plus(add_val))));

        // Core calculation.
        for(int epoch=0; epoch < 1; epoch++){
            for(int k=0; k < n_samples; k++){
                int idx = (int)(Math.random() * (n_samples));
                // Single entry is drawn from the dataset.
                dataframe entry = this.X.getRow(idx);
                seen.add(idx);

                // The p value is calculated by carrying out the dot
                // product of the entry and the corresponding weights (theta).
                dataframe p = entry.dot(this.theta.transpose()).plus(intercept);
                dataframe grad = log_dloss(p, this.y.getRow(idx));
                dataframe update_left = entry.el_mul(grad);
                dataframe update_right = theta.el_mul(step_size);
                dataframe update = update_left.plus(update_right);

                // New theta values are calculated.
                dataframe grad_correction = update.minus(grad_memory.getRow(idx));
                sum_grad = sum_grad.plus(grad_correction);
                grad_memory.setRow(idx, update);

                // Gradient value is calculated using the update value.
                double grad_correction_val = grad.getData()[0][0] - intercept_grad_memory.getValue(0, idx);

                intercept_grad_memory.setValue(0, idx, grad.getData()[0][0]);
                intercept_sum_grad += grad_correction_val;

                // Intercept value is calculated using the learning rate and the decay.
                this.intercept -= (((step_size * intercept_sum_grad) / seen.size()) * decay);

                // Theta values are updated using the gradient and the step size.
                theta = theta.minus(sum_grad.el_mul(step_size).el_div(seen.size()));
            }

            // Check convergence.
            double max_change = 0.0;
            double max_weight = 0.0;
            for (int i = 0; i < theta.getRowDim(); i++) {
                for (int j = 0; j < theta.getColDim(); j++) {
                    max_weight = Math.max(max_weight, Math.abs(theta.getData()[i][j]));
                    max_change = Math.max(max_change, Math.abs(theta.getData()[i][j] - prev_theta.getData()[i][j]));

                    prev_theta = new dataframe(theta.getData());
                }
            }
            if ((max_weight != 0 && max_change / max_weight <= tol) || max_weight == 0 && max_change == 0){
                System.out.printf("Convergence occurred after %d epochs %n", epoch);
                break;
            }
        }
        return new fit_params(theta, intercept);
    }

    /**
     * Calculates the prediction based on the tuned weights and the intercept values.
     * @return A dataframe of the predictions.
     */

    @Override
    public dataframe predict() {
        int m = this.X.getRowDim();
        dataframe p = new dataframe(m, 1);

        // Calculates the scores using using the tuned theta and intercept values.
        dataframe scores = this.X.dot(this.theta.transpose()).plus(this.intercept);

        // Prediction is 1 if the score in positive.
        for(int i = 0; i < scores.getRowDim(); i++){
            if(scores.getValue(i, 0) > 0) {
                p.setValue(i, 0, 1);
            }
        }
        return p;
    }

    /**
     * Calculates the prediction based on the tuned weights and the intercept values.
     * @param vals A fit_params array containing the trained theta values
     * @param classes A 1D double array containing the unique classes available.
     * @return A dataframe of the prediction.
     */

    @Override
    public dataframe predict(fit_params[] vals, double[] classes) {
        dataframe p = new dataframe(X.getRowDim(), 1);
        dataframe scores = new dataframe(X.getRowDim(), classes.length);
        for (int i = 0; i < classes.length; i++) {
            scores.setCol(i+1, this.X.dot(vals[i].getTheta().transpose()).plus(vals[i].getIntercept()));
        }
        for(int i = 0; i < scores.getRowDim(); i++){
            p.setValue(i, 0, classes[scores.argmax(i)]);
        }
        return p;
    }
}
