package com.external;

public class Description {
    
    public static String getCalculatingHashCodeDescription(int hashCode){
        return "Calculating hash code : hashCode = " + hashCode;
    }

    public static String getCheckRowOccupiedDescription(int row){
        return "Checking row: " + row;
    }

    public static String getCollisionDetectedDescription(int row){
        return "Row " + row + " is already full";
    }

    public static String getOnAvailableSlotFoundDescription(int row){
        return "Found available slot at bucket "+ row;
    }
    
    public static String getOnNoAvailableSlotDescription(int key){
        return "No available slot left, failed to insert element " + key;
    }

    public static String getOnInsertionEventDescription(int key, int row){
        return "Successfully inserted element " + key + " to bucket " + row;
    }

    public static String getOnDeleteEventDescription(){
        return "Successfully deleted element";
    }

    public static String getOnFoundEventDescription(int key, int row){
        return "Found element " + key + " on bucket: " + row;
    }

    public static String getOnNotFoundEventDescription(int key){
        return "Not found element " + key;
    }

    public static String getOnCreationEventDescription(int size){
        return "Created table with size " + size;
    }

    public static String getOnAlreadyExistedElementDescription(int key){
        return "Element " + key + " is already in the table!";
    }
}
