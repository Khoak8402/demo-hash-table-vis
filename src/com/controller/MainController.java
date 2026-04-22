package com.controller;

import com.table.HashTableModel;

import javafx.fxml.FXML;

public class MainController {
    @FXML
    private ControlController controlPaneController;
    @FXML
    private ViewController viewPaneController;
    @FXML
    private DetailController detailPaneController;

    private HashTableModel tableModel;

    @FXML
    public void initModel(HashTableModel tableModel){
        this.tableModel= tableModel;
        detailPaneController.initModel(tableModel);
        viewPaneController.initTableModel(tableModel);
        controlPaneController.initModel(tableModel);
        
    }
}
