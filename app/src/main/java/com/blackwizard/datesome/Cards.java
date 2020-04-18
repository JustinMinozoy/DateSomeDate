package com.blackwizard.datesome;

public class Cards {
    private String userId;
    private String name;

    public Cards(String userId, String name){
        this.userId = userId;
        this.name   = name;

    }

    public String getUserId(){
        return userId;
    }

    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }
}
