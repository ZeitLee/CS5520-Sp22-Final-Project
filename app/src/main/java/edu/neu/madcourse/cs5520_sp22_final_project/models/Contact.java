package edu.neu.madcourse.cs5520_sp22_final_project.models;

/**
 * This class represents contact person information.
 *
 */
public class Contact {
    private String name;
    private String phoneNum;
    private String emailAddress;

    /**
     * Constructor.
     */
    public Contact(String name, String phoneName, String emailAddress) {
        this.name = name;
        this.phoneNum = phoneName;
        this.emailAddress = emailAddress;
    }

    /**
     * Getter to get name.
     */
    public String getName() {
        return this.name;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }



}
