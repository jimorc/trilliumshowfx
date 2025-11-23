package com.github.jimorc.flexishowbuilder;

import com.opencsv.bean.CsvBindByName;

/**
 * FlexiBean represents a row in the CSV input file.
 */
public class FlexiBean {
    @CsvBindByName(column = "Filename", required = true)
    private String filename;

    @CsvBindByName(column = "Title", required = false)
    private String title;

    @CsvBindByName(column = "Full Name", required = false)
    private String fullName;

    @CsvBindByName(column = "First Name", required = false)
    private String firstName;

    @CsvBindByName(column = "Last Name", required = false)
    private String lastName;

    /**
     * Get filename.
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get title.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get full name.
     * @return full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Get first name.
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get last name.
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set filename.
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Set title.
     * @param title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set full name.
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Set first name.
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set last name.
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * toString method.
     * @return string representation of FlexiBean.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(filename);
        sb.append(",");
        sb.append(title);
        sb.append(",");
        sb.append(fullName);
        sb.append(",");
        sb.append(firstName);
        sb.append(",");
        sb.append(lastName);
        return sb.toString();
    }
}
