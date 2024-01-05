package com.farmerfirst.growagric.firebase.vo;

public class NotificationVO{
    private String title;
    private String message;
    private String imageURL;
    private String action;
    private String actionDestination;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getImageURL(){
        return imageURL;
    }

    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }

    public String getAction(){
        return action;
    }

    public void setAction(String action){
        this.action = action;
    }

    public String getActionDestination(){
        return actionDestination;
    }

    public void setActionDestination(String actionDestination){
        this.actionDestination = actionDestination;
    }
}
