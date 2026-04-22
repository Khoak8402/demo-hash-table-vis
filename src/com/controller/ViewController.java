package com.controller;

import com.table.HashTableModel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.List;


public class ViewController {
    public VBox visualTable;
    public static String DEFAULT_TABLE_STYLE= "-fx-border-color: black; -fx-border-width: 2; -fx-padding: 5;";
    public static String DEFAULT_ROW_STYLE= "-fx-border-color: black; -fx-border-width: 2; -fx-padding: 5;";
    public static String DEFAULT_CELL_STYLE= "-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-padding: 2;";
    public static String GLOW_ROW_STYLE= "-fx-border-color: red; -fx-border-width: 2; -fx-padding: 5;";
    public static String GLOW_CELL_STYLE= "-fx-background-color: yellow; -fx-border-color: darkblue; -fx-padding: 2;";;

    private HashTableModel tabelModel; 

    @FXML
    private AnchorPane viewPane;

    public void displayTable(VBox table){
        clearViewPane();
        viewPane.getChildren().add(table);
    }

    public void clearViewPane(){
        viewPane.getChildren().clear();
    }


    // Create and display visual table
    public void createAndVisualizeTable(int size){

        // Create visual table
        VBox table= new VBox(10);
        table.setStyle(DEFAULT_TABLE_STYLE);
        
        for(int i=0; i<size; i++){
            HBox column= new HBox(10);
            column.setAlignment(Pos.CENTER_LEFT);
            column.setStyle(DEFAULT_ROW_STYLE);
            column.setMinWidth(Region.USE_PREF_SIZE);

            Label header = new Label(Integer.valueOf(i).toString() + " | ");
            column.getChildren().add(header);

            VBox.setVgrow(column, Priority.ALWAYS);
            table.getChildren().add(column);
        }
        this.visualTable= table; 

        //Display
        displayTable(table);
    }
    
    public Label newCell(int data){
        Label cell= new Label();
        cell.setText(String.valueOf(data));
        cell.setStyle(DEFAULT_CELL_STYLE);
        return cell;
    }

    public void insertCell(int key, int row){
        HBox hBox= (HBox)visualTable.getChildren().get(row);
        hBox.getChildren().add(newCell(key));
    }

    public void deleteCell(int row, int idx){
        HBox hBox= (HBox)visualTable.getChildren().get(row);
        hBox.getChildren().remove(idx+1);
    }

    public void foundCellAnimation(int row, int idx){
        HBox hBox= (HBox)visualTable.getChildren().get(row);
        Label toHightlight= (Label)hBox.getChildren().get(idx+1);
        
        Timeline blinkingAnimation= new Timeline(
            new KeyFrame(Duration.ZERO, event -> toHightlight.setBackground(Background.fill(Color.YELLOW))),
            new KeyFrame(Duration.seconds(0.5), event -> toHightlight.setBackground(Background.fill(Color.LIGHTBLUE))),
            new KeyFrame(Duration.seconds(1.0))
        );

        blinkingAnimation.setCycleCount(3);
        blinkingAnimation.play();
    }

    public void glowRow(int row){
        HBox glowHBox= (HBox)visualTable.getChildren().get(row);
        glowHBox.setStyle(GLOW_ROW_STYLE);
    }

    public void deglowRow(int row){
        HBox glowHBox= (HBox)visualTable.getChildren().get(row);
        glowHBox.setStyle(DEFAULT_ROW_STYLE);
    }

    public void glowCell(int row, int idx){
        HBox glowHBox= (HBox)visualTable.getChildren().get(row);
        Label glowLabel= (Label)glowHBox.getChildren().get(idx);
        glowLabel.setStyle(GLOW_CELL_STYLE);
    }

    public void deglowCell(int row, int idx){
        HBox glowHBox= (HBox)visualTable.getChildren().get(row);
        Label glowLabel= (Label)glowHBox.getChildren().get(idx);
        glowLabel.setStyle(DEFAULT_CELL_STYLE);
    }

    public void resetGlowEffect(){
        // Deglow all cell and row
        for(int i=0; i< visualTable.getChildren().size(); i++){
            deglowRow(i);
            HBox hBox= (HBox)visualTable.getChildren().get(i);
            for(int j=1; j< hBox.getChildren().size(); j++){
                deglowCell(i, j);
            }
        }
    }


    public void initTableModel(HashTableModel model){
        this.tabelModel= model;

        //Creation event: display the visual table
        model.addOnCreationEvent((tableSize) -> createAndVisualizeTable(tableSize));

        //Check event:
        model.addOnCheckRowOccupied((row) -> glowRow(row));
        model.addOnCheckCellMatched((row, idx) -> glowCell(row, idx+1));

        //Insert event:
        model.addOnInsertionEvent((key, row) -> insertCell(key, row));
    
        //Delete event:
        model.addOnDeletionEvent((row, idx) -> deleteCell(row, idx));

        //Search event:
        model.addOnFoundEvent((key, row, idx) -> foundCellAnimation(row, idx));

        //Refresh start:
        model.addOnStartOperationEvent(() -> resetGlowEffect());
    }


}
