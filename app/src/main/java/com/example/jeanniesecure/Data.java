package com.example.jeanniesecure;


/* Data class creates object of data that would store the SMS message that would be used
 * for uploading to the server. */
public class Data {
    String message;
    int id;

    public Data(String message, int id){
        this.message = message;
        this.id = id;
    }


    /* Getter and Setter functions */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
