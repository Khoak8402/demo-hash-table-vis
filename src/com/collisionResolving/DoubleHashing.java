package com.collisionResolving;

import com.table.HashTable;

public class DoubleHashing implements CollisionResolver{
    @Override
    public int resolve(int key, int step, HashTable table){
        //Find the next empty slot, but with h2 jump
        int hashCode= table.h1(key);
        return (hashCode + table.h2(key)*step)%table.getSize();
    }

    @Override
    public String getName(){
        return "Double Hashing";
    }
}
