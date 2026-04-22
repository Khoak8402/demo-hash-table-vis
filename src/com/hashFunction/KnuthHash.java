package com.hashFunction;

public class KnuthHash implements HashFunction{ 
    @Override
    public int hash(int key, int tableSize){
        int scrambled= key * 0x9e3779b9;
        int shift= 32 - (int)(Math.floor(Math.log(tableSize)));
        return Math.abs((scrambled >> shift) % tableSize);
    }
    @Override
    public String getName(){
        return "Knuth Hash";
    }
}
