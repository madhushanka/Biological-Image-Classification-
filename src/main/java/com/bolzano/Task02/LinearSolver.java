package com.bolzano.Task02;

import com.bolzano.dataframe;

/**
 * Common interface for all the current and future solvers for linear ML models.
 * @author Vinay Bharadhwa - 3190098
 */

public interface LinearSolver {
    void setParams(
            dataframe X,
            dataframe y,
            dataframe theta,
            int max_epochs,
            double lambda,
            double learning_rate,
            double threshold
    );
    void fit();
    fit_params fit(boolean ret_fit);
    dataframe predict();
    dataframe predict(fit_params[] vals, double[] classes);
}
