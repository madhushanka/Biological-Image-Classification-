package com.bolzano.Task03;

import org.hibernate.annotations.NaturalId;
import javax.persistence.*;

/**
 * Domain model class for the API application.
 * Entity: Used to define the class that is mapped to a table in a database
 * class name should be same as table name.
 * @author Tharindu Madhusankha - 3198602
 */

@Entity
public class Model_Table {

    /**
     * Id : indicates the member field below is the primary key of current entity.
     * GeneratedValue : used to configure the way of increment of the specified column(field)
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer image_id;
    private String nimages;
    private String comp;
    private String overlay;
    private String offset;
    private String ranges;
    private String threshold;
    private String type;
    private String lutsize;
    private String samples;
    private String size;
    private String selection;
    private String whiteiszero;
    private String width;
    private String byteorder;
    private String id;
    private String calibration;
    private String height;
    @NaturalId
    private String path;
    private String classification;
    private String image_base46;

    public Model_Table(){

    }

    public Model_Table(String id, String imagename) {
        // Constructor.
        super();
        this.image_id = image_id;
        this.nimages = nimages;
        this.comp = comp;
        this.overlay = overlay;
        this.offset = offset;
        this.ranges = ranges;
        this.threshold = threshold;
        this.type = type;
        this.lutsize = lutsize;
        this.samples = samples;
        this.size = size;
        this.selection = selection;
        this.whiteiszero = whiteiszero;
        this.width = width;
        this.byteorder = byteorder;
        this.id = id;
        this.calibration = calibration;
        this.height = height;
        this.path = path;
        this.classification = classification;
        this.image_base46 = image_base46;

    }
    // Getter method.
    public Integer getImage_id() {
        return image_id;
    }

    // Setter method.
    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }

    // Getter method.
    public String getComp() {
        return comp;
    }

    // Setter method.
    public void setComp(String comp) {
        this.comp = comp;
    }

    // Getter method.
    public String getOverlay() {
        return overlay;
    }

    // Setter method.
    public void setOverlay(String overlay) {
        this.overlay = overlay;
    }

    // Getter method.
    public String getOffset() {
        return offset;
    }

    // Setter method.
    public void setOffset(String offset) {
        this.offset = offset;
    }

    // Getter method.
    public String getRanges() {
        return ranges;
    }

    //Setter method.
    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    // Getter method.
    public String getThreshold() {
        return threshold;
    }

    // Setter method.
    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    // Getter method.
    public String getType() {
        return type;
    }

    // Setter method.
    public void setType(String type) {
        this.type = type;
    }

    // Getter method.
    public String getLutsize() {
        return lutsize;
    }

    public void setLutsize(String lutsize) {
        this.lutsize = lutsize;
    }

    // Getter method.
    public String getSamples() {
        return samples;
    }

    // Setter method.
    public void setSamples(String samples) {
        this.samples = samples;
    }

    // Getter method.
    public String getSize() {
        return size;
    }

    // Setter method.
    public void setSize(String size) {
        this.size = size;
    }

    // Getter method.
    public String getSelection() {
        return selection;
    }

    // Setter method.
    public void setSelection(String selection) {
        this.selection = selection;
    }

    // Getter method.
    public String getWhiteiszero() {
        return whiteiszero;
    }

    // Setter method.
    public void setWhiteiszero(String whiteiszero) {
        this.whiteiszero = whiteiszero;
    }

    // Getter method.
    public String getWidth() {
        return width;
    }

    // Setter method.
    public void setWidth(String width) {
        this.width = width;
    }

    // Getter method.
    public String getByteorder() {
        return byteorder;
    }

    // Setter method.
    public void setByteorder(String byteorder) {
        this.byteorder = byteorder;
    }

    // Getter method.
    public String getId(String id) {
        return id;
    }

    // Setter method.
    public void setId(String id) {
        this.id = id;
    }

    // Getter method.
    public String getCalibration() {
        return calibration;
    }

    // Setter method.
    public void setCalibration(String calibration) {
        this.calibration = calibration;
    }

    // Getter method.
    public String getHeight() {
        return height;
    }

    // Setter method.
    public void setHeight(String height) {
        this.height = height;
    }

    // Getter method.
    public String getPath() {
        return path;
    }

    // Setter method.
    public void setPath(String path) {
        this.path = path;
    }

    // Getter method.
    public String getClassification() {
        return classification;
    }

    // Setter method.
    public void setClassification(String classification) {
        this.classification = classification;
    }

    // Getter method.
    public String getNimages() {
        return nimages;
    }

    // Setter method.
    public void setNimages(String nimages) {
        this.nimages = nimages;
    }

    // Getter method.
    public String getImage_base46() {
        return image_base46;
    }

    // Setter method.
    public void setImage_base46(String image_base46) {
        this.image_base46 = image_base46;
    }
}
