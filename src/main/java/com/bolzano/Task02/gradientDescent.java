package com.bolzano.Task02;

import com.bolzano.dataframe;

/**
 * Gradient Descent.
 * @author Vinay Bharadhwa - 3190098
 */

class gradientDescent implements LinearSolver {
    dataframe X;
    dataframe y;
    dataframe theta;
    int max_epochs;
    double lambda;
    double learning_rate;
    double threshold;

    /**
     * Setter method to set the parameters of the logistic regression model.
     * @param X The data variable.
     * @param y The label for the samples in the data.
     * @param theta The weights for gradient descent.
     * @param max_epochs Number of times the gradient descent run if it does not converge.
     * @param lambda Regularization ratio.
     * @param learning_rate Learning rate.
     * @param threshold threshold for the class assignment.
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
    ){
        this.X = X;
        this.y = y;
        this.theta = theta;
        this.max_epochs = max_epochs;
        this.lambda = lambda;
        this.learning_rate = learning_rate;
        this.threshold = threshold;
    }

    /**
     * Method to carry out sigmoid function.
     * @param a The dataframe on which sigmoid function is applied on.
     * @return A dataframe which has been processed by the sigmoid function.
     */

    private dataframe sigmoid(dataframe a){
        a = a.el_mul(-1);
        for (int i = 0; i < a.getRowDim(); i++) {
            for (int j = 0; j < a.getColDim(); j++) {
                a.setValue(i, j, 1 + Math.exp(a.getValue(i, j)));
            }
        }
        return (a.pow(-1));
    }

    /**
     * Method to calculate the cost during gradient descent.
     * @param X The data variable.
     * @param y The labels for the data.
     * @param theta The weights for gradient descent.
     * @param lambda Regularization ratio.
     * @return A dataframe array consisting of the cost and the gradient for the given values.
     */

    private dataframe[] cost(dataframe X, dataframe y, dataframe theta, double lambda){
        float m = X.getRowDim();

        // Calculate the sigmoid of the dot product of X & the corresponding weights (theta).
        dataframe h = sigmoid(X.dot(theta));
        dataframe logh = doLog(h);

        // Calculate the negative value of the sigmoid of the dot product of X & theta.
        dataframe neg_h = h.el_mul(-1).plus(1);
        dataframe neg_logh = doLog(neg_h);

        dataframe left = y.transpose().el_mul(-1).dot(logh);
        dataframe right = y.transpose().el_mul(-1).plus(1).dot(neg_logh);

        // Set the 1st input's weights to 0.
        theta.setValue(0, 0, 0);

        // Calculate regularized cost.
        dataframe jreg = theta.transpose().dot(theta).el_mul(lambda / (2 * m));
        dataframe J = left.minus(right).el_div(m).plus(jreg);

        // Calculate the error and the gradient.
        dataframe error = h.minus(y);
        dataframe gradreg = theta.el_mul(lambda/m);
        dataframe grad = X.transpose().dot(error).el_mul(1/m).plus(gradreg);

        return new dataframe[] {grad, J};
    }

    /**
     * Method to calculate log for every element in the dataframe.
     * @param mat The dataframe on which log is applied on.
     * @return A dataframe whose log has been calculated.
     */

    private dataframe doLog(dataframe mat) {
        dataframe mat_log = new dataframe(mat.getRowDim(), mat.getColDim());
        for (int i = 0; i < mat.getRowDim(); i++) {
            for (int j = 0; j < mat.getColDim(); j++) {
                mat_log.setValue(i, j, Math.log(mat.getValue(i, j)));
            }
        }
        return mat_log;
    }

    /**
     * Method to predict based on the tuned weights from gradient descent and threshold value.
     * @return A dataframe of the prediction.
     */

    @Override
    public dataframe predict(){
        int m = this.X.getRowDim();
        dataframe p = new dataframe(m, 1);

        dataframe h = sigmoid(this.X.dot(this.theta));

        for(int i = 1; i < h.getRowDim(); i++){
            if(h.getValue(i, 0) >= this.threshold) {
                p.setValue(i, 0, 1);
            }
        }
        return p;
    }

    /**
     * Method to predict based on the tuned weights from gradient descent and threshold value.
     * @param vals A fit_params array containing the trained theta values
     * @param classes A 1D double array containing the unique classes available.
     * @return A dataframe of the prediction.
     */

    @Override
    public dataframe predict(fit_params[] vals, double[] classes){
        int m = this.X.getRowDim();
        dataframe p = new dataframe(m, 1);

        dataframe h = sigmoid(this.X.dot(theta));

        for(int i = 1; i < h.getRowDim(); i++){
            if(h.getValue(i, 0) >= this.threshold) {
                p.setValue(i, 0, 1);
            }
        }
        return p;
    }

    /**
     * Method to carry out gradient decent of n epochs.
     */

    @Override
    public void fit(){
        for(int epoch=0; epoch < this.max_epochs; epoch++){
            float m = this.X.getRowDim();
            dataframe h = sigmoid(this.X.dot(this.theta));

            dataframe error = h.minus(this.y);
            dataframe gradreg = this.theta.el_mul(this.lambda/m);
            dataframe grad = this.X.transpose().dot(error).el_mul(1/m).plus(gradreg);
            this.theta = this.theta.minus(grad.el_mul(this.learning_rate));
        }
    }

    /**
     * Method to carry out gradient decent of n epochs.
     * @param ret_fit Boolean value to indicate if the tuned weights and
     *                the intercept must be returned.
     * @return A fit_params variable containing the theta and intercept values.
     */

    @Override
    public fit_params fit(boolean ret_fit){
        for(int epoch=0; epoch < this.max_epochs; epoch++){
            float m = this.X.getRowDim();
            dataframe h = sigmoid(this.X.dot(this.theta));

            dataframe error = h.minus(this.y);
            dataframe gradreg = this.theta.el_mul(this.lambda/m);
            dataframe grad = this.X.transpose().dot(error).el_mul(1/m).plus(gradreg);
            this.theta = this.theta.minus(grad.el_mul(this.learning_rate));
        }
        return new fit_params(theta, 0);
    }
}
