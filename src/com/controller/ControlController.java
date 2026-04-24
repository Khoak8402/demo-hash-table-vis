package com.controller;

import java.util.List;

import com.collisionResolving.CollisionResolver;
import com.collisionResolving.DoubleHashing;
import com.hashFunction.HashFunction;
import com.hashFunction.KnuthHash;
import com.hashFunction.MixerHash;
import com.hashFunction.ModuloHash;
import com.table.HashTable;
import com.collisionResolving.LinearProbling;
import com.collisionResolving.QuadraticProbling;
import com.collisionResolving.SeparateChaining;
import com.external.StatusMessage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.table.HashTableModel;

public class ControlController {
    @FXML
    private ComboBox<String> operationComboBox;
    @FXML
    private ComboBox<CollisionResolver> collisionHandlingComboBox;
    @FXML
    private ComboBox<HashFunction> hashFunction1ComboBox;
    @FXML
    private ComboBox<HashFunction> hashFunction2ComboBox;
    @FXML
    private ComboBox<String> dataTypeComboBox;
    @FXML
    private Button createBtn;
    @FXML
    private Button executeBtn;
    @FXML
    private TextField sizeInputField;
    @FXML
    private TextField dataInputField;
    @FXML
    private Text statusField;

    private HashTableModel tabelModel; 


    public void initModel(HashTableModel model){
        this.tabelModel= model;
        
    }

    public void alertStatus(StatusMessage msg){
        statusField.setText(msg.getMessage());
        
        String msgStyle= msg.isError()? "-fx-fill: red;" : "-fx-fill: green;";
        statusField.setStyle(msgStyle);
    }


    public int getInputSize(TextField sizeInputField){
        String txt= sizeInputField.getText();

        //Validate input text
        if(txt==null || txt.equals("")){
            alertStatus(StatusMessage.ERROR_INVALID_INPUT);
            return -1;
        }

        try{
            int size= Integer.parseInt(txt);
            if(size<=0)
                alertStatus(StatusMessage.ERROR_INVALID_INPUT);
            else if(size>HashTable.MAX_SIZE){
                alertStatus(StatusMessage.ERROR_TABLE_SIZE_LIMIT_EXCEED);
            }
            else
                return size;
        }catch(NumberFormatException e){
            alertStatus(StatusMessage.ERROR_INVALID_INPUT);
        }

        return -1;
    }

    public int getInputData(TextField dataInputField){
        String txt= dataInputField.getText();

        //Validate input text
        if(txt==null || txt.equals("")){
            alertStatus(StatusMessage.ERROR_INVALID_INPUT);
            return -1;
        }

        try{
            int data= Integer.parseInt(txt);
            if(data<=0)
                alertStatus(StatusMessage.ERROR_INVALID_INPUT);
            else
                return data;
        }catch(NumberFormatException e){
            alertStatus(StatusMessage.ERROR_INVALID_INPUT);
        }

        return -1;
    }


    public void createTable(){
        // Get data from user input
        int size= getInputSize(sizeInputField);
        CollisionResolver collisionResolver= collisionHandlingComboBox.getValue();
        HashFunction hashFunction1= hashFunction1ComboBox.getValue();
        HashFunction hashFunction2= hashFunction2ComboBox.isDisable()? null : hashFunction2ComboBox.getValue();

        if(collisionResolver==null || hashFunction1==null){
            alertStatus(StatusMessage.ERROR_EMPTY_FIELD);
            return;
        }
            
        if(hashFunction2==null && collisionResolver instanceof DoubleHashing){
            alertStatus(StatusMessage.ERROR_EMPTY_FIELD);
            return;
        }
            

        HashTable hashTable= new HashTable(size, collisionResolver, hashFunction1, hashFunction2);
        tabelModel.setNewTable(hashTable);
        alertStatus(StatusMessage.SUCCESS_MSG);
    }

    public void executeOperation(){
        int data= getInputData(dataInputField);
        if(data==-1)
            return; //Invalid data, stop process

        //Get operation type
        switch (operationComboBox.getValue()) {
            case "Insert":
                boolean successInsertOp= tabelModel.insert(data);
                if(successInsertOp)
                    alertStatus(StatusMessage.SUCCESS_MSG);
                else
                    alertStatus(StatusMessage.FAILED_MSG);
                break;
            
            case "Delete":
                boolean successDeleteOp= tabelModel.delete(data);
                if(successDeleteOp)
                    alertStatus(StatusMessage.SUCCESS_MSG);
                else
                    alertStatus(StatusMessage.FAILED_MSG);
                break;
        
            case "Search":
                tabelModel.search(data);
                alertStatus(StatusMessage.SUCCESS_MSG);
                break;
            
            case null:
                alertStatus(StatusMessage.ERROR_EMPTY_FIELD);
                break;

            default:
                break;
        }
        playVisualizeAnimation();
    }

    public void playVisualizeAnimation(){
        List<Runnable> script= tabelModel.getMainScript();

        Timeline animation= new Timeline();

        for(int i=0; i < script.size(); i++){
            Runnable command= script.get(i);
            KeyFrame keyFrame= new KeyFrame(Duration.millis(i*1000), event -> {
                command.run();
            });
            animation.getKeyFrames().add(keyFrame);
        }

        animation.play();
    }

    
    @FXML
    public void initialize(){

        //Initialize ComboBox

        // List of operation
        String[] operationChoice= {"Insert", "Delete", "Search"};
        operationComboBox.getItems().addAll(operationChoice);


        // Set up list of choice for collision handling strategy
        CollisionResolver[] collisionHandlingChoice= {new LinearProbling(), new QuadraticProbling(), new SeparateChaining(), new DoubleHashing()};
        collisionHandlingComboBox.getItems().addAll(collisionHandlingChoice);
        collisionHandlingComboBox.setConverter(new StringConverter<CollisionResolver>() {
           @Override
           public String toString(CollisionResolver collisionResolver){
            return collisionResolver.getName();
           } 
           @Override
           public CollisionResolver fromString(String str){
            return null;
           }
        });


        // Set up list of choice for hash function
        HashFunction[] hashFunctionChoice= {new ModuloHash(), new KnuthHash(), new MixerHash()};
        StringConverter<HashFunction> hashFunctionStrConverter= new StringConverter<HashFunction>() {
            @Override
            public String toString(HashFunction hashFunction){
                return hashFunction.getName();
            } 
            @Override
            public HashFunction fromString(String str){
                return null;
            }
        };
        hashFunction1ComboBox.getItems().addAll(hashFunctionChoice);
        hashFunction2ComboBox.getItems().addAll(hashFunctionChoice);
        hashFunction1ComboBox.setConverter(hashFunctionStrConverter);
        hashFunction2ComboBox.setConverter(hashFunctionStrConverter);


        String[] dataTypeChoice= {"Integer"};
        dataTypeComboBox.getItems().addAll(dataTypeChoice);


        // Setup create button
        createBtn.setOnAction(event -> createTable());

        // Setup execute button
        executeBtn.setOnAction(event -> executeOperation());
        
    }
}
