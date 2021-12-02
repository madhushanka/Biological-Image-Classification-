package com.bolzano;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Database connection assistant.
 * It functions like an Object Relation Mapper (ORM)
 * @author Vinay Bharadhwa - 3190098
 */

public class database {
    private String url;
    private String[] cols;

    // Constructor
    public database(String url) {
        this.url = "jdbc:sqlite:" + url;
    }

    // Getter method.
    public String getUrl() {
        return url;
    }

    /**
     * Creates a connection to the database.
     * @return A connection object
     */

    private Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Method to create a table in the dataframe.
     * @param table_name The desired table name.
     */

    public void createTable(String table_name) {
        // Initialized columns.
        this.cols = new String[]{
                "nimages", "comp", "overlay", "offset", "ranges", "format", "threshold", "type", "lutsize",
                "bits_per_pixel", "samples", "size", "selection", "whiteiszero", "width", "byteorder",
                "id", "calibration", "height", "path", "classification", "image_base46"
        };

        // Creates an SQL Query to create a table with "cols" columns.
        StringBuilder sql_builder = new StringBuilder(String.format(
                "CREATE TABLE IF NOT EXISTS %s (image_id INTEGER PRIMARY KEY AUTOINCREMENT",
                table_name
        ));
        for (String entry : this.cols) {
            if (entry.equals("path")){
                sql_builder.append(String.format(", %s text UNIQUE", entry));
            }
            else {
                sql_builder.append(String.format(", %s text", entry));
            }
        }
        sql_builder.append(");");

        String sql = sql_builder.toString();

        // Execution.
        try{
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Insertion of values to the database table.
     * @param table_name An existing table name.
     * @param row A Map of the title along with value.
     */

    public void insertTo(String table_name, Map<String, String> row){
        StringBuilder sql_top_bulder = new StringBuilder(String.format("INSERT INTO %s (", table_name));
        StringBuilder sql_bottom_bulder = new StringBuilder("\nVALUES (");

        // Loops through the columns and gets the meta-information based on column name.
        for (String entry: this.cols) {
            sql_top_bulder.append(entry).append(", ");
            sql_bottom_bulder.append('"').append(row.get(entry)).append('"').append(", ");
        }

        // Compilation into SQL query.
        String sql_top = sql_top_bulder.toString().substring(0, sql_top_bulder.toString().length() - 2);
        String sql_bottom = sql_bottom_bulder.substring(0, sql_bottom_bulder.length() - 2);

        String sql = sql_top + ")" + sql_bottom + ");";

        // Execution.
        try{
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
