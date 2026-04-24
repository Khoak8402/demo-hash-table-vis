package com.table;

import java.util.function.Consumer;
import java.util.function.BiConsumer;
import com.external.TriConsumer;
import java.util.ArrayList;
import java.util.List;


public class HashTableModel {
    private HashTable hashTable;

    private final List<Consumer<Integer>> onHashCodeCalculated= new ArrayList<>(); //Event: Compute hash code
    private final List<Consumer<Integer>> onCheckRowOccupied= new ArrayList<>(); // Event: Check a crow
    private final List<BiConsumer<Integer, Integer>> onCheckCellMatched= new ArrayList<>(); // Event: Check a cell
    private final List<Consumer<Integer>> onCollisionDetected= new ArrayList<>(); //Event: Collision detected
    private final List<Consumer<Integer>> onAvailableSlotFound= new ArrayList<>(); //Event: Found available slot
    private final List<Consumer<Integer>> onNoAvailableSlot= new ArrayList<>(); //Event: No slot left
    private final List<BiConsumer<Integer, Integer>> onInsertionEvent= new ArrayList<>(); //Event: Insert element
    private final List<BiConsumer<Integer, Integer>> onDeletionEvent= new ArrayList<>(); //Event: Delete element
    private final List<TriConsumer<Integer, Integer, Integer>> onFoundEvent= new ArrayList<>(); //Event: Found element
    private final List<Consumer<Integer>> onNotFoundEvent= new ArrayList<>(); //Event: Not found element
    private final List<Consumer<Integer>> onAlreadyExistedElement= new ArrayList<>(); //Event: Insert an existed element
    private final List<Consumer<Integer>> onCreationEvent= new ArrayList<>();
    private final List<Runnable> onStartOperationEvent= new ArrayList<>();

    // This will hold the script for the visualization
    private final List<Runnable> mainScript= new ArrayList<>();

    public List<Runnable> getMainScript(){
        return mainScript;
    }

    public void addOnHashCodeCalculated(Consumer<Integer> callback){
        this.onHashCodeCalculated.add(callback);
    }

    public void addOnCheckCellMatched(BiConsumer<Integer, Integer> callback){
        this.onCheckCellMatched.add(callback);
    }

    public void addOnCheckRowOccupied(Consumer<Integer> callback){
        this.onCheckRowOccupied.add(callback);
    }

    public void addOnCollisionDetected(Consumer<Integer> callback){
        this.onCollisionDetected.add(callback);
    }

    public void addOnAvailableSlotFound(Consumer<Integer> callback){
        this.onAvailableSlotFound.add(callback);
    }

    public void addNoAvailableSlot(Consumer<Integer> callback){
        this.onNoAvailableSlot.add(callback);
    }

    public void addOnInsertionEvent(BiConsumer<Integer, Integer> callback){
        this.onInsertionEvent.add(callback);
    }

    public void addOnDeletionEvent(BiConsumer<Integer, Integer> callback){
        this.onDeletionEvent.add(callback);
    }

    public void addOnFoundEvent(TriConsumer<Integer, Integer, Integer> callback){
        this.onFoundEvent.add(callback);
    }

    public void addOnNotFoundEvent(Consumer<Integer> callback){
        this.onNotFoundEvent.add(callback);
    }

    public void addOnCreationEvent(Consumer<Integer> callback){
        this.onCreationEvent.add(callback);
    }

    public void addOnStartOperationEvent(Runnable callback){
        this.onStartOperationEvent.add(callback);
    }

    public void addOnAlreadyExistedEvent(Consumer<Integer> callback){
        this.onAlreadyExistedElement.add(callback);
    }

    // Initialize
    public void setNewTable(HashTable table){
        this.hashTable= table;
        for(Consumer<Integer> e : onCreationEvent)
            e.accept(table.getSize());;
    }


    // Insert operation
    public boolean insert(int key){
        //Refresh table
        generateScriptStartOperationEvent();

        // Calculate hash code
        int hashCode= hashTable.h1(key);
        generateScriptOnHashCodeCalculatedEvent(hashCode);

        // Check separate chaining
        if(hashTable.utilizeSeparateChaining()){
            generateScriptOnCheckRowOccupiedEvent(hashCode);

            //Check if element already existed
            for(int i=0; i<hashTable.getRowSize(hashCode); i++){
                generateScriptOnCheckCellMatchedEvent(hashCode, i);
                
                if(hashTable.getElement(hashCode, i)==key){
                    generateScriptOnAlreadyExistedElementEvent(key);
                    return false;
                }
            }

            hashTable.addElement(key, hashCode);
            generateScriptOnAvailableSlotFound(hashCode);
            generateScriptOnInsertionEvent(key, hashCode);
            
            return true;
        }

        else{
        int n= hashTable.getSize();
        for(int i=0; i<= n; i++){
            int row= hashTable.collisionResolve(key, i);

            generateScriptOnCheckRowOccupiedEvent(row);

            if(hashTable.checkEmptyRow(row)){
                generateScriptOnAvailableSlotFound(row);
                generateScriptOnInsertionEvent(key, row);
                
                hashTable.addElement(key, row);
                
                return true;
            }

            generateScriptOnCheckCellMatchedEvent(row, 0);
            if(hashTable.getElement(row, 0)==key){
                generateScriptOnAlreadyExistedElementEvent(key);
                return false;
            }
            generateScriptOnCollisionDetectedEvent(row);
        }}

        //Not found
        generateScriptOnNoAvailableSlotEvent(key);

        return false;
    }

    private void generateScriptOnNoAvailableSlotEvent(int key) {
        final int fkey= key;
        for(Consumer<Integer> cons : onNoAvailableSlot){
            Runnable e= () -> cons.accept(fkey);
            mainScript.add(e);
        }
    }

    private void generateScriptOnCollisionDetectedEvent(int row) {
        final int frow= row;
        for(Consumer<Integer> cons : onCollisionDetected){
            Runnable e= () -> cons.accept(frow);
            mainScript.add(e);
        }
            
    }

    private void generateScriptOnInsertionEvent(int key, int hashCode) {
        final int fkey= key, fhashCode= hashCode;
        for(BiConsumer<Integer, Integer> cons : onInsertionEvent){
            Runnable e= () -> cons.accept(fkey, fhashCode);
            mainScript.add(e);
        }
    }

    private void generateScriptOnAvailableSlotFound(int hashCode) {
        final int fhashCode= hashCode;
        for(Consumer<Integer> cons : onAvailableSlotFound){
            Runnable e= () -> cons.accept(fhashCode);
            mainScript.add(e);
        }
    }

    private void generateScriptOnAlreadyExistedElementEvent(int key) {
        final int fkey= key;
        for(Consumer<Integer> cons : onAlreadyExistedElement){
            Runnable e = () -> cons.accept(fkey);
            mainScript.add(e);
        }
    }

    private void generateScriptOnCheckCellMatchedEvent(int hashCode, int i) {
        final int fhashCode= hashCode, fi= i;
        for(BiConsumer<Integer, Integer> cons : onCheckCellMatched){
            Runnable e= () -> cons.accept(fhashCode, fi);
            mainScript.add(e);
        }
    }

    private void generateScriptOnCheckRowOccupiedEvent(int hashCode) {
        final int fhashCode= hashCode;
        for(Consumer<Integer> cons : onCheckRowOccupied){
            Runnable e= () -> cons.accept(fhashCode);
            mainScript.add(e);
        }  
    }

    private void generateScriptOnHashCodeCalculatedEvent(int hashCode) {
        final int fhashCode= hashCode;
        for(Consumer<Integer> cons : onHashCodeCalculated){
            Runnable e= () -> cons.accept(fhashCode);
            mainScript.add(e);
        }   
    }

    private void generateScriptStartOperationEvent() {
        mainScript.clear();
        for(Runnable e : onStartOperationEvent)
            mainScript.add(e);
    }

    //Search operation
    public int[] search(int key){
        generateScriptStartOperationEvent();
        // Calculate hash code
        int hashCode= hashTable.h1(key);
        generateScriptOnHashCodeCalculatedEvent(hashCode);

        if(hashTable.utilizeSeparateChaining()){
            generateScriptOnCheckRowOccupiedEvent(hashCode);

            int n= hashTable.getRowSize(hashCode);
            for(int i=0; i<n; i++){
                generateScriptOnCheckCellMatchedEvent(hashCode, i);
                if(hashTable.getElement(hashCode, i)==key){
                    generateScriptOnFoundEvent(key, hashCode, i);
                    return new int[]{hashCode, i};
                }
            }
        }

        else
        for(int i=0; i<=hashTable.getSize(); i++){
            int row= hashTable.collisionResolve(key, i);
            generateScriptOnCheckRowOccupiedEvent(row);

            if(hashTable.checkEmptyRow(row)){
                generateScriptOnNotFoundEvent(key);
                return null;
            }

            generateScriptOnCheckCellMatchedEvent(row, 0);
        
            if(hashTable.getElement(row, 0)==key){
                //Found
                generateScriptOnFoundEvent(key, row, 0);
                return new int[]{row, 0};
            }
        }

        //Not found
        generateScriptOnNotFoundEvent(key);

        return null;
    }

    private void generateScriptOnNotFoundEvent(int key) {
        final int fkey= key;
        for(Consumer<Integer> cons : onNotFoundEvent){
            Runnable e= () -> cons.accept(fkey);
            mainScript.add(e);
        }
    }

    private void generateScriptOnFoundEvent(int key, int hashCode, int idx) {
        final int fkey= key, fhashCode= hashCode, fidx= idx;
        for(TriConsumer<Integer, Integer, Integer> cons : onFoundEvent){
            Runnable e= () -> cons.accept(fkey, fhashCode, fidx);
            mainScript.add(e);
        }   
    }

    // Delete operation
    public boolean delete(int key){
        generateScriptStartOperationEvent();

        int[] found= search(key); 

        // Not found
        if(found==null)
            return false;

        int row= found[0], idx= found[1];
        hashTable.removeElement(row, idx);
        
        generateScriptOnDeletionEvent(row, idx);

        return true;
    }

    private void generateScriptOnDeletionEvent(int row, int idx) {
        final int frow= row, fidx= idx;
        for(BiConsumer<Integer, Integer> cons : onDeletionEvent){
            Runnable e= () -> cons.accept(frow, fidx);
            mainScript.add(e);
        } 
    } 
    
}
