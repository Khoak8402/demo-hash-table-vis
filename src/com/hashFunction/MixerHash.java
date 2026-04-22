package com.hashFunction;

public class MixerHash implements HashFunction{
    @Override
    public int hash(int key, int tableSize){
        key = ((key >>> 16) ^ key) * 0x45d9f3b;
        key = ((key >>> 16) ^ key) * 0x45d9f3b;
        key = (key >>> 16) ^ key;
        return Math.abs(key % tableSize);
    }
    @Override
    public String getName(){
        return "Mixer Hash";
    }
}

