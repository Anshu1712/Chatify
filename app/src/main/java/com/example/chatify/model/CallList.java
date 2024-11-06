package com.example.chatify.model;  // This defines the package where this class is located (com.example.chatify.model).

public class CallList {  // Declaring the class CallList which represents a call entry with various details.

    // Declaring private instance variables (fields) to store the call-related data.
    private String userID;       // Stores the unique ID of the user associated with the call.
    private String userName;     // Stores the name of the user who made or received the call.
    private String date;         // Stores the date and time of the call.
    private String urlProfile;   // Stores the URL of the profile image of the user.
    private String callType;     // Stores the type of call (e.g., "missed", "incoming", "outgoing").

    // Default constructor - an empty constructor required for certain frameworks (e.g., Firebase) or when no parameters are passed during object creation.
    public CallList() {
    }

    // Parameterized constructor - Allows creating an instance of CallList by providing values for all fields.
    public CallList(String userID, String userName, String date, String urlProfile, String callType) {
        this.userID = userID;          // Initialize the userID with the provided userID.
        this.userName = userName;      // Initialize the userName with the provided userName.
        this.date = date;              // Initialize the date with the provided date.
        this.urlProfile = urlProfile;  // Initialize the urlProfile with the provided URL of the profile image.
        this.callType = callType;      // Initialize the callType with the provided call type (e.g., "missed", "incoming").
    }

    // Getter method for userID - Retrieves the userID value.
    public String getUserID() {
        return userID;  // Returns the userID associated with the call.
    }

    // Setter method for userID - Allows setting the userID value.
    public void setUserID(String userID) {
        this.userID = userID;  // Sets the userID field to the provided value.
    }

    // Getter method for userName - Retrieves the userName value.
    public String getUserName() {
        return userName;  // Returns the name of the user.
    }

    // Setter method for userName - Allows setting the userName value.
    public void setUserName(String userName) {
        this.userName = userName;  // Sets the userName field to the provided value.
    }

    // Getter method for date - Retrieves the date value.
    public String getDate() {
        return date;  // Returns the date of the call.
    }

    // Setter method for date - Allows setting the date value.
    public void setDate(String date) {
        this.date = date;  // Sets the date field to the provided value.
    }

    // Getter method for urlProfile - Retrieves the profile image URL.
    public String getUrlProfile() {
        return urlProfile;  // Returns the URL of the user's profile image.
    }

    // Setter method for urlProfile - Allows setting the profile image URL.
    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;  // Sets the urlProfile field to the provided value.
    }

    // Getter method for callType - Retrieves the type of the call.
    public String getCallType() {
        return callType;  // Returns the type of the call (e.g., "missed", "incoming").
    }

    // Setter method for callType - Allows setting the callType value.
    public void setCallType(String callType) {
        this.callType = callType;  // Sets the callType field to the provided value.
    }
}
