package com.bolzano.Task02;

import com.bolzano.dataframe;

/**
 * Custom data type to contain the weights (theta) and the intercept of the
 * logistic regression model.
 * @author Vinay Bharadhwa - 3190098
 */

final class fit_params {
    private dataframe theta;
    private double intercept;

    // Constructor.
    public fit_params(dataframe theta, double intercept){
        this.theta = theta;
        this.intercept = intercept;
    }

    // Getter method.
    public double getIntercept() {
        return intercept;
    }

    // Getter method.
    public dataframe getTheta() {
        return theta;
    }
}
