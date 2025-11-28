package com.github.jimorc.flexishowbuilder;

/**
 * Person class represents the name of a person. It converts between a person's
 * firstName and lastName and their fullName (<firstName> <lastName>) as well as
 * (<firstName> <lastNameInitial>.).
 */
public class Person {
    private String firstName;
    private String lastName;

    /**
     * Constructor for a Person object.
     * @param first the person's first name
     * @param last the person's last name
     */
    public Person(String first, String last) {
        firstName = first;
        lastName = last;
    }

    /**
     * Retrieve the person's first name.
     * @return a string containing the person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieve the person's full name.
     * @return the person's full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Retrieve the person's last name.
     * @return the person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retrieve the person's first name plus the initial of the last name.
     * @return the person's first name plus initial
     */
    public String getFirstPlusInitial() {
        return firstName + " " + lastName.substring(0, 1) + ".";
    }

    /**
     * toString method returns the person's full name.
     * @return the person's full name
     */
    @Override
    public String toString() {
        return getFullName();
    }
}
