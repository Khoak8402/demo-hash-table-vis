package com.external;

public class StatusMessage {
    private String message;
    private boolean isError;

    public StatusMessage(String message, boolean isError){
        this.message= message;
        this.isError= isError;
    }

    public String getMessage(){
        return message;
    }

    public boolean isError(){
        return isError;
    }

    public static StatusMessage SUCCESS_MSG= new StatusMessage("Success", false);
    public static StatusMessage ERROR_INVALID_INPUT= new StatusMessage("Error: Invalid input field!", true);
    public static StatusMessage ERROR_TABLE_SIZE_LIMIT_EXCEED= new StatusMessage("Error: Maximum size is 20!", true);
    public static StatusMessage ERROR_EMPTY_FIELD= new StatusMessage("Error: Please select all fields", true);
    public static StatusMessage FAILED_MSG= new StatusMessage("Failed", true);
}
