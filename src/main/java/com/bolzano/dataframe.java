package com.bolzano;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A custom datatype to store the data as a 2D double array and carry out various functions on them.
 * @author Vinay Bharadhwa - 3190098
 */

final public class dataframe {
    private final int row;             // number of rows
    private final int col;             // number of columns
    private final double[][] data;   // row-by-col array

    /**
     * Constructor to create row-by-col dataframe filled with 0's
     * @param row Number of rows
     * @param col Number of columns
     */

    public dataframe(int row, int col) {
        this.row = row;
        this.col = col;
        data = new double[row][col];
    }

    /**
     * Constructor to create dataframe based on a 2d array
     * @param data 2D Double array containing the data to populate the dataframe
     */

    public dataframe(double[][] data) {
        row = data.length;
        col = data[0].length;
        this.data = new double[row][col];
        for (int i = 0; i < row; i++)
            System.arraycopy(data[i], 0, this.data[i], 0, col);
    }

    /**
     * Copy constructor
     * @param A Dataframe that needs to be copied to a new dataframe
     */

    public dataframe(dataframe A) {
        this.row = A.row;
        this.col = A.col;
        this.data = A.data;
    }

    /**
     * Getter for the number of rows
     * @return number of rows
     */

    public int getRowDim() {
        return row;
    }

    /**
     * Getter for the number of columns
     * @return number of columns
     */

    public int getColDim() {
        return col;
    }

    /**
     * Getter for the data
     * @return 2D array of data
     */

    public double[][] getData() {
        return data;
    }

    /**
     * Setter method to set value of a given row and column
     * @param row Index of the row
     * @param col Index of the column
     * @param value Value to be set at the row x col position
     */

    public void setValue(int row, int col, double value){
        data[row][col] = value;
    }

    /**
     * Getter method to get value of a given row and column
     * @param row Index of the row
     * @param col Index of the column
     * @return value at row x col
     */

    public double getValue(int row, int col){
        return data[row][col];
    }

    /**
     * Getter method to get a column as a dataframe
     * @param col_ind Index of the column to be retrieved
     * @return column at col_ind as a dataframe
     */

    public dataframe getCol(int col_ind){
        dataframe A = this;
        dataframe B = new dataframe(A.row, 1);
        if (col_ind > 0) {
            col_ind = col_ind - 1;
        }
        else if (col_ind < 0) {
            col_ind = A.col - (Math.abs(col_ind));
        }
        for (int i = 0; i < A.row; i++) {
            B.data[i][0] = A.data[i][col_ind];
        }
        return B;
    }

    /**
     * Setter method to set a column in a dataframe
     * @param col_ind Index of the column to be set
     * @param B dataframe to be put at the col_ind
     */

    public void setCol(int col_ind, dataframe B){
        col_ind = col_ind - 1;
        if(B.col > 1) throw new RuntimeException("The matrix has more than 1 column");
        dataframe A = this;
        for (int i = 0; i < A.row; i++){
            A.data[i][col_ind] = B.data[i][0];
        }
    }

    /**
     * Get a flattened version of a column, i.e., 2D dataframe is returned as a 1D double array
     * @param col_ind The index of the column that needs to be flattened
     * @return Retruns the column as a 1D double array
     */

    public double[] getFlatCol(int col_ind){
        col_ind = col_ind - 1;
        dataframe A = this;
        double[] B = new double[A.row];
        for (int i = 0; i < A.row; i++){
            B[i] = A.data[i][col_ind];
        }
        return B;
    }

    /**
     * Getter method to get a row as a dataframe
     * @param row_ind Index of the row to be retrieved
     * @return row at row_ind as a dataframe
     */

    public dataframe getRow(int row_ind){
        row_ind = row_ind - 1;
        dataframe A = this;
        dataframe B = new dataframe(1, A.col);
        if (A.row >= 0) System.arraycopy(A.data[row_ind], 0, B.data[0], 0, A.col);
        return B;
    }

    /**
     * Setter method to set a row in a dataframe
     * @param row_ind Index of the row to be set
     * @param B dataframe to be put at the row_ind
     */

    public void setRow(int row_ind, dataframe B){
        row_ind = row_ind - 1;
        if(B.row > 1) throw new RuntimeException("The matrix has more than 1 row");
        dataframe A = this;
        if (B.col >= 0) System.arraycopy(B.data[0], 0, A.data[row_ind], 0, B.col);
    }

    /**
     * Get a flattened version of a row, i.e., 2D dataframe is returned as a 1D double array
     * @param row_ind The index of the row that needs to be flattened
     * @return Retruns the row as a 1D double array
     */

    public double[] getFlatRow(int row_ind){
        row_ind = row_ind - 1;
        dataframe A = this;
        double[] B = new double[A.col];
        if (A.row >= 0) System.arraycopy(A.data[row_ind], 0, B, 0, A.row);
        return B;
    }

    /**
     * Static method to create and return a random row-by-col dataframe with values between 0 and 1
     * @param row Number of rows
     * @param col Number of columns
     * @return A dataframe of size row-by-col 
     */

    public static dataframe random(int row, int col) {
        dataframe A = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                A.data[i][j] = Math.random();
        return A;
    }

    /**
     * Static method to create and return an identity dataframe
     * @param col The size of the square Identity dataframe
     * @return A dataframe of size col-by-col 
     */

    public static dataframe eye(int col) {
        dataframe A = new dataframe(col, col);
        for (int i = 0; i < col; i++)
            A.data[i][i] = 1;
        return A;
    }

    /**
     * Static method to create and return a row-by-col dataframe filled with 0's
     * @param row Number of rows
     * @param col Number of columns
     * @return A dataframe of size row-by-col
     */

    public static dataframe zeros(int row, int col) {
        dataframe A = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                A.data[i][j] = 0;
        return A;
    }

    /**
     * Static method to create and return a 1-by-col dataframe filled with 0's
     * @param col Number of columns
     * @return A dataframe of size 1-by-col
     */

    public static dataframe zeros(int col) {
        dataframe A = new dataframe(1, col);
        for (int i = 0; i < col; i++)
            A.data[0][i] = 0;
        return A;
    }

    /**
     * Static method to create and return a row-by-col dataframe filled with 1's
     * @param row Number of rows
     * @param col Number of columns
     * @return A dataframe of size row-by-col
     */

    public static dataframe ones(int row, int col) {
        dataframe A = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                A.data[i][j] = 1;
        return A;
    }

    /**
     * Static method to create and return a 1-by-col dataframe filled with 1's
     * @param col Number of columns
     * @return A dataframe of size 1-by-col
     */

    public static dataframe ones(int col) {
        dataframe A = new dataframe(1, col);
        for (int i = 0; i < col; i++)
            A.data[0][i] = 1;
        return A;
    }

    /**
     * Static method to calculate the sum of a given dataframe
     * @param A Dataframe whose sum
     * @param axis Axis along which the sum has to be calculated (0 - row, 1 - column)
     * @return A dataframe with the sum of either rows or columns
     */

    public static dataframe sum(dataframe A, int axis){
        if (axis < 0 || axis > 1) throw new RuntimeException("Illegal axis value");
        dataframe B;
        if (axis == 1){
            B = new dataframe(1, A.row);
            for (int i = 0; i < A.row; i++) {
                double temp = 0;
                for (int j = 0; j < A.col; j++) {
                    temp += A.data[i][j];
                }
                B.data[0][i] = temp;
            }
        }
        else {
            B = new dataframe(1, A.col);
            for (int i = 0; i < A.col; i++) {
                double temp = 0;
                for (int j = 0; j < A.row; j++) {
                    temp += A.data[i][j];
                }
                B.data[0][i] = temp;
            }
        }
        return B;
    }

    /**
     * Static method to find the max of the passed dataframe
     * @param A dataframe whose max needs to be calculated
     * @return The max value
     */

    public static double max(dataframe A) {
        double val = A.data[0][0];
        for (int i = 0; i < A.row; i++) {
            for (int j = 0; j < A.col; j++) {
                if (A.data[i][j] > val) {
                    val = A.data[i][j];
                }
            }
        }
        return val;
    }

    /**
     * Static method to find the min of the passed dataframe
     * @param A dataframe whose min needs to be calculated
     * @return The min value
     */

    public static double min(dataframe A) {
        double val = A.data[0][0];
        for (int i = 0; i < A.row; i++) {
            for (int j = 0; j < A.col; j++) {
                if (A.data[i][j] < val) {
                    val = A.data[i][j];
                }
            }
        }
        return val;
    }

    /**
     * Static method to find the unique values of the passed dataframe
     * @param A dataframe whose unique values needs to be calculated
     * @return The unique values as a 1D array
     */

    public static double[] unique(dataframe A){
        double[] unique_arr;
        if(A.getColDim() == 1)
            unique_arr = Arrays.stream(A.getFlatCol(1)).distinct().toArray();
        else if(A.getRowDim() == 1)
            unique_arr = Arrays.stream(A.getFlatRow(1)).distinct().toArray();
        else throw new RuntimeException("Invalid Matrix");

        return unique_arr;
    }

    /**
     * Static method to load a file as a dataframe
     * @param fileName Name of the file to be loaded
     * @param sep separators used to separate the columns
     * @return A dataframe containing the file data
     * @throws IOException if the file is not available
     */

    public static dataframe readFile(String fileName, String sep) throws IOException {
        double[][] data_vals = new double[0][0];
        int rows = 0;
        for(int loop_num = 0; loop_num < 2; loop_num++) {
            Scanner inputStream= new Scanner(new File(fileName));
            data_vals = new double[rows][];
            int i = 0;
            while (inputStream.hasNextLine()) {
                String line = inputStream.nextLine();
                if(loop_num == 1){
                    data_vals[i] = Arrays.stream(line.split(sep)).mapToDouble(Double::parseDouble).toArray();
                }
                i += 1;
            }
            rows = i;
            inputStream.close();
        }
        return new dataframe(data_vals);
    }

    /**
     * Static method to load a CSV file as a dataframe
     * @param fileName Name of the file to be loaded
     * @return A dataframe containing the file data
     * @throws IOException if the file is not available
     */

    public static dataframe readFile(String fileName) throws IOException {
        double[][] data_vals = new double[0][0];
        int rows = 0;
        for(int loop_num = 0; loop_num < 2; loop_num++) {
            Scanner inputStream= new Scanner(new File(fileName));
            data_vals = new double[rows][];
            int i = 0;
            while (inputStream.hasNextLine()) {
                String line = inputStream.nextLine();
                if(loop_num == 1){
                    data_vals[i] = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
                }
                i += 1;
            }
            rows = i;
            inputStream.close();
        }
        return new dataframe(data_vals);
    }

    /**
     * Static method to split the data into 2 dataframes (Training and Testing)
     * based on the train size
     * @param X Dataframe to be split
     * @param train_size Size of the train dataframe (0 - 1)
     * @return Array of dataframes containing training and testing data
     */

    public static dataframe[] trainTestSplit(dataframe X, double train_size){
        Random randomGenerator = new Random();
        Set<Integer> nums = new HashSet<Integer>();
        int train_size_val = (int)(train_size * X.getRowDim());

        dataframe train_data = new dataframe(train_size_val, X.getColDim());
        dataframe test_data = new dataframe(X.getRowDim() - train_size_val, X.getColDim());

        while (nums.size() < train_size_val) {
            int randomInt = randomGenerator.nextInt(X.getRowDim());
            nums.add(randomInt);
        }

        int train_ind = 1;
        int test_ind = 1;

        for (int i = 1; i <= X.getRowDim(); i++) {
            if(nums.contains(i - 1)){
                train_data.setRow(train_ind, X.getRow(i));
                train_ind++;
            }
            else{
                test_data.setRow(test_ind, X.getRow(i));
                test_ind++;
            }
        }
        return new dataframe[] {train_data, test_data};
    }

    /**
     * Remove either a row or a column based on the index and axis passed
     * @param ind index of the row or column
     * @param axis Axis to determine row or columns (0 - row, 1 - column)
     * @return dataframe with the row or column dropped
     */

    public dataframe drop(int ind, int axis){
        if (axis < 0 || axis > 1) throw new RuntimeException("Illegal axis value");
        if (ind == 0) throw new RuntimeException("Invalid index");
        dataframe A = this;
        dataframe B;

        if (ind > 0) {
            ind = ind - 1;
            if (axis == 0) {
                B = new dataframe(A.row - 1, A.col);
                for (int i = 0; i < A.col; i++) {
                    int k = 0;
                    for (int j = 0; j < A.row; j++) {
                        if (j == ind) continue;
                        B.data[k][i] = A.data[j][i];
                        k++;
                    }
                }
            } else {
                B = new dataframe(A.row, A.col-1);
                for (int i = 0; i < A.row; i++) {
                    int k = 0;
                    for (int j = 0; j < A.col; j++) {
                        if (j == ind) continue;
                        B.data[i][k] = A.data[i][j];
                        k++;
                    }
                }
            }
        }
        else {
            if (axis == 0) {
                B = new dataframe(A.row - 1, A.col);
                ind = A.row - ((Math.abs(ind)));
                for (int i = 0; i < A.col; i++) {
                    int k = 0;
                    for (int j = 0; j < A.row; j++) {
                        if (j == ind) continue;
                        B.data[k][i] = A.data[j][i];
                        k++;
                    }
                }
            } else {
                B = new dataframe(A.row, A.col-1);
                ind = A.col + ind;
                for (int i = 0; i < A.row; i++) {
                    int k = 0;
                    for (int j = 0; j < A.col; j++) {
                        if (j == ind) continue;
                        B.data[i][k] = A.data[i][j];
                        k++;
                    }
                }
            }
        }
        return B;
    }

    /**
     * Finds the index of max value in a given row
     * @param row_ind Index of the row
     * @return Index of the max value in the row
     */

    public int argmax(int row_ind) {
        dataframe A = this;
        double val = A.data[0][0];
        int index = 0;
        for (int j = 0; j < A.col; j++) {
            if (A.data[row_ind][j] > val) {
                val = A.data[row_ind][j];
                index = j;
            }
        }
        return index;
    }

    /**
     * Returns the transpose of the invoking dataframe
     * @return Transpose of the invoking dataframe
     */

    public dataframe transpose() {
        dataframe A = new dataframe(col, row);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }

    /**
     * Calculates the sum of the invoking dataframe and the passed dataframe
     * @param B The 2nd dataframe
     * @return Sum of the 2 dataframes as a dataframe
     */

    public dataframe plus(dataframe B) {
        dataframe A = this;
        if (A.row == 1 && A.col == 1) return B.plus(A.data[0][0]);
        else if (B.row == 1 && B.col == 1) return A.plus(B.data[0][0]);
        if (B.row != A.row || B.col != A.col) throw new RuntimeException("Illegal matrix dimensions.");
        dataframe C = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                C.data[i][j] = A.data[i][j] + B.data[i][j];
        return C;
    }

    /**
     * Calculates the sum of the invoking dataframe and the passed scalar value
     * @param lambda The scalar value to be added
     * @return Sum of the scalar value and the dataframe as a dataframe
     */

    public dataframe plus(double lambda) {
        dataframe A = this;
        dataframe C = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                C.data[i][j] = A.data[i][j] + lambda;
        return C;
    }

    /**
     * Calculates the difference between of the invoking dataframe and the passed dataframe
     * @param B The 2nd dataframe
     * @return Difference between of the 2 dataframes as a dataframe
     */

    public dataframe minus(dataframe B) {
        dataframe A = this;
        if (A.row == 1 && A.col == 1) return B.minus(A.data[0][0]);
        else if (B.row == 1 && B.col == 1) return A.minus(B.data[0][0]);
        if (B.row != A.row || B.col != A.col) throw new RuntimeException("Illegal matrix dimensions.");
        dataframe C = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                C.data[i][j] = A.data[i][j] - B.data[i][j];
        return C;
    }

    /**
     * Calculates the difference between of the invoking dataframe and the passed scalar value
     * @param lambda The scalar value to be subtracted
     * @return Difference between of the scalar value and the dataframe as a dataframe
     */

    public dataframe minus(double lambda) {
        dataframe A = this;
        dataframe C = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                C.data[i][j] = A.data[i][j] - lambda;
        return C;
    }

    /**
     * Calculates the dot product of the invoking dataframe and the passed dataframe
     * @param B The 2nd dataframe
     * @return Dot product of the 2 dataframes as a dataframe
     */

    public dataframe dot(dataframe B) {
        dataframe A = this;
        if (A.col != B.row) throw new RuntimeException("Illegal matrix dimensions.");
        dataframe C = new dataframe(A.row, B.col);
        for (int i = 0; i < C.row; i++)
            for (int j = 0; j < C.col; j++)
                for (int k = 0; k < A.col; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }

    /**
     * Calculates the elemental product of the invoking dataframe and the passed dataframe
     * @param B The 2nd dataframe
     * @return Product of the 2 dataframes as a dataframe
     */

    public dataframe el_mul(dataframe B) {
        dataframe A = this;
        if (A.row == 1 && A.col == 1) return B.el_mul(A.data[0][0]);
        else if (B.row == 1 && B.col == 1) return A.el_mul(B.data[0][0]);
        if (B.row != A.row || B.col != A.col) throw new RuntimeException("Illegal matrix dimensions.");
        dataframe C = new dataframe(A.row, B.col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                C.data[i][j] = A.data[i][j] * B.data[i][j];
        return C;
    }

    /**
     * Calculates the product between of the invoking dataframe and the passed scalar value
     * @param lambda The scalar value to be subtracted
     * @return Product between of the scalar value and the dataframe as a dataframe
     */

    public dataframe el_mul(double lambda) {
        dataframe A = this;
        dataframe C = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                C.data[i][j] = A.data[i][j] * lambda;
        return C;
    }

    /**
     * Calculates the elemental division of the invoking dataframe and the passed dataframe
     * @param B The 2nd dataframe
     * @return Division of the 2 dataframes as a dataframe
     */

    public dataframe el_div(dataframe B){
        dataframe A = this;
        if (A.row == 1 && A.col == 1) return B.el_div(A.data[0][0]);
        else if (B.row == 1 && B.col == 1) return A.el_div(B.data[0][0]);
        if (B.row != A.row || B.col != A.col) throw new RuntimeException("Illegal matrix dimensions.");
        dataframe C = new dataframe(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                C.data[i][j] = A.data[i][j] / B.data[i][j];
            }
        }
        return C;
    }

    /**
     * Calculates the division between of the invoking dataframe and the passed scalar value
     * @param lambda The scalar value to be subtracted
     * @return Division between of the scalar value and the dataframe as a dataframe
     */

    public dataframe el_div(double lambda) {
        dataframe A = this;
        dataframe C = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                C.data[i][j] = A.data[i][j] / lambda;
        return C;
    }

    /**
     * Calculates the exponent of the values of the invoking dataframe raised by passed scalar value
     * @param pow The scalar value to be raised
     * @return Exponent of the values of the invoking dataframe raised by passed scalar value as a dataframe
     */

    public dataframe pow(float pow){
        dataframe A = this;
        dataframe C = new dataframe(row, col);
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                C.data[i][j] = Math.pow(A.data[i][j], pow);
        return C;
    }

    /**
     * Method joins two dataframes vertically one after the another.
     * @param B A dataframe that needs to be stacked.
     * @return A new dataframe with the two dataframes vertically stacked.
     */

    public dataframe vstack(dataframe B){
        dataframe A = this;
        if (B.col != A.col) throw new RuntimeException("The 2 matrices are not of the same horizontal size.");
        dataframe C = new dataframe(row + B.row, col);
        for (int i = 0; i < row; i++) {
            try {
                System.arraycopy(A.data[i], 0, C.data[i], 0, col);
            }
            catch (ArrayIndexOutOfBoundsException ignored){
            }
            try {
                System.arraycopy(B.data[i], 0, C.data[i + row], 0, col);
            }
            catch (ArrayIndexOutOfBoundsException ignored){
            }
        }
        return C;
    }

    /** Method joins two dataframes horizontally one after the another.
     * @param B A dataframe that needs to be stacked.
     * @return A new dataframe with the two dataframes horizontally stacked.
     */

    public dataframe hstack(dataframe B){
        dataframe A = this;
        if (B.row != A.row) throw new RuntimeException("The 2 matrices are not of the same vertical size.");
        dataframe C = new dataframe(row, col + B.col);
        for (int i = 0; i < row; i++) {
            try {
                System.arraycopy(A.data[i], 0, C.data[i], 0, col);
            }
            catch (ArrayIndexOutOfBoundsException ignored){
            }
            try {
                if (B.col >= 0) System.arraycopy(B.getData()[i], 0, C.data[i], A.col, B.col);
            }
            catch (ArrayIndexOutOfBoundsException ignored){
            }
        }
        return C;
    }

    /**
     * Method to check all values present in dataframe are greater than the given value.
     * @param value A double value
     * @return A boolean value
     */

    public boolean greater_than(double value){
        dataframe A = this;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (A.data[i][j] < value) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method to check all values present in dataframe are lower than the given value.
     * @param value A double value
     * @return A boolean value
     */
    public boolean lesser_than(double value){
        dataframe A = this;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (A.data[i][j] > value) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method replaces all given value present in databse to the new value.
     * @param from The value needs to be replaced.
     * @param to The replaced or new value.
     * @return A dataframe with the replaced values.
     */

    public dataframe replace_all(double from, double to) {
        dataframe A = this;
        dataframe B = new dataframe(A);
        for (int i = 0; i < A.getRowDim(); i++)
            for (int j = 0; j < A.getColDim(); j++)
                if (A.getValue(i, j) == from) B.setValue(i, j, to);
        return B;
    }

    /**
     * Method to write the dataframe to a file.
     * @param sep The separator for the file.
     * @param fileName The name of existing file you want to write to.
     * @throws IOException If the file is not found.
     */

    public void toFile(String sep, String fileName) throws IOException {
        double[][] data = this.data;
        BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
        StringBuilder sb = new StringBuilder();

        for (double[] arr : data) {
            String line =  Arrays.stream(arr).mapToObj(String::valueOf).collect(Collectors.joining(sep));
            sb.append(line);
            sb.append("\n");
        }

        br.write(sb.toString());
        br.close();
    }

    // Write to CSV file

    /**
     * Method to write the dataframe in a comma separated (csv) file
     * @param fileName The name of existing file you want to write to.
     * @throws IOException If the file is not found.
     */

    public void toFile(String fileName) throws IOException {
        double[][] data = this.data;
        BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
        StringBuilder sb = new StringBuilder();

        for (double[] arr : data) {
            String line =  Arrays.stream(arr).mapToObj(String::valueOf).collect(Collectors.joining(","));
            sb.append(line);
            sb.append("\n");
        }

        br.write(sb.toString());
        br.close();
    }

    /**
     * Prints matrix to standard output
     */

    public void show() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++)
                System.out.printf("%9.4f ", data[i][j]);
            System.out.println();
        }
    }
}
