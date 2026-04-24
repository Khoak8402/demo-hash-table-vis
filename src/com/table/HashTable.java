package com.table;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.collisionResolving.CollisionResolver;
import com.collisionResolving.SeparateChaining;
import com.hashFunction.HashFunction;


public class HashTable {
    private int size;
    private HashFunction hashFunction;
    private HashFunction hashFunction2;
    private CollisionResolver collisionResolver;
    private ArrayList<ArrayList<Integer>> table;

    public static final int MAX_SIZE= 20;


    // Default constructor
    public HashTable(int size, CollisionResolver collisionResolver, HashFunction hashFunction){
        this.size= size;
        this.table= Stream.generate(ArrayList<Integer>::new).limit(size).collect(Collectors.toCollection(ArrayList::new));
        this.collisionResolver= collisionResolver;
        this.hashFunction= hashFunction;
    }

    // Constructor for Double Hashing table
    public HashTable(int size, CollisionResolver collisionResolver, HashFunction hashFunction, HashFunction hashFunction2){
        this(size, collisionResolver, hashFunction);
        this.hashFunction2= hashFunction2;
    }

    public int getSize(){
        return size;
    }

    public int getElement(int row, int idx){
        return table.get(row).get(idx).intValue();
    }

    public int getRowSize(int row){
        return table.get(row).size();
    }

    // Hash function
    public int h1(int key){
        return hashFunction.hash(key, size);
    }
    public int h2(int key){
        if(hashFunction2==null)
            return -1;
        return hashFunction2.hash(key, size);
    }

    //Collision resolve 
    public int collisionResolve(int key, int step){
        return collisionResolver.resolve(key, step, this);
    }
    public boolean utilizeSeparateChaining(){
        return collisionResolver instanceof SeparateChaining;
    }

    // Check occupied cell
    public boolean checkEmptyRow(int row){
        return table.get(row).isEmpty();
    }

    // Add an element to specific row
    public void addElement(int key, int row){
        table.get(row).add(key);
    }

    public void removeElement(int row, int idx){
        table.get(row).remove(idx);
    }


    // Search operation 
    /**
     * Search the element key in the hash table 
     * @param key the element to be searched
     * @return {cell,index} indicating the position of key, return null if not found
     */
    public int[] search(int key){
        int hashCode= h1(key);

        if(utilizeSeparateChaining()){
            int n= getRowSize(hashCode);
            for(int i=0; i<n; i++){
                if(getElement(hashCode, i)==key){
                    return new int[]{hashCode, i};
                }
            }
        }

        for(int i=0; i<=size; i++){
            int row= collisionResolve(key, i);
            if(checkEmptyRow(row))
                return null;
            if(getElement(row, i)==key){
                //Found
                return new int[]{row, 0};
            }
        }

        return null;
    }


    // Insert operation
    public boolean insert(int key){
        int hashCode= h1(key);

        if(utilizeSeparateChaining()){
            //Check if element already existed
            for(int i=0; i<getRowSize(hashCode); i++)
                if(getElement(hashCode, i)==key)
                    return false;

            addElement(key, hashCode);
            return true;
        }

        for(int i=0; i<=size; i++){
            int row= collisionResolve(key, i);
            if(getElement(row, 0)==key)
                return false; //If already existed
            if(checkEmptyRow(row)){
                addElement(key, hashCode);
                return true;
            }
        }

        return false;
    }


    // Delete operation
    public boolean delete(int key){
        int[] found= search(key); 

        // Not found
        if(found==null)
            return false;

        int row= found[0], idx= found[1];
        removeElement(row, idx);

        return true;
    } 


}
