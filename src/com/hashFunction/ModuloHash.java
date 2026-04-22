package com.hashFunction;

public class ModuloHash implements HashFunction{
    @Override
    public int hash(int key, int tableSize){
        return Math.abs(key % tableSize);
    }
    @Override
    public String getName(){
        return "Modulo Hash";
    }
}
