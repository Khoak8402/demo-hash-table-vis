package com.controller;

import com.external.Description;
import com.table.HashTableModel;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class DetailController {
    private HashTableModel tabelModel; 

    @FXML
    private Text descriptionField;

    private void setDescription(String des){
        descriptionField.setText(des);
    }

    public void initModel(HashTableModel model){
        this.tabelModel= model;
        model.addNoAvailableSlot((key)->setDescription(Description.getOnNoAvailableSlotDescription(key)));
        
        model.addOnAvailableSlotFound((key)->setDescription(Description.getOnAvailableSlotFoundDescription(key)));
        
        model.addOnCheckRowOccupied((row)->setDescription(Description.getCheckRowOccupiedDescription(row)));
        
        model.addOnCollisionDetected((row)->setDescription(Description.getCollisionDetectedDescription(row)));
        
        model.addOnCreationEvent((size)->setDescription(Description.getOnCreationEventDescription(size)));
        
        model.addOnDeletionEvent((key, row)->setDescription(Description.getOnDeleteEventDescription()));
        
        model.addOnFoundEvent((key, row, idx)->setDescription(Description.getOnFoundEventDescription(key, row)));
        
        model.addOnHashCodeCalculated((hashCode)->setDescription(Description.getCalculatingHashCodeDescription(hashCode)));
        
        model.addOnNotFoundEvent((key)->setDescription(Description.getOnNotFoundEventDescription(key)));
        
        model.addOnInsertionEvent((key, row)->setDescription(Description.getOnInsertionEventDescription(key, row)));
        
        model.addOnAlreadyExistedEvent((key)->setDescription(Description.getOnAlreadyExistedElementDescription(key)));
    }

    @FXML
    public void initialize(){
        //Make description bigger
        descriptionField.setFont(Font.font(23));
    }

}
