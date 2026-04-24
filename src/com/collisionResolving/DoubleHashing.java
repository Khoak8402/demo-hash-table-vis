package com.collisionResolving;

import com.table.HashTable;

public class DoubleHashing implements CollisionResolver{
    @Override
    public int resolve(int key, int step, HashTable table){
        //Find the next empty slot, but with h2 jump
        int hashCode= table.h1(key);
        int hashCode2= table.h2(key)==0? 1 : table.h2(key); //hashCode2 should not be 0
        return (hashCode + hashCode2*step)%table.getSize();
    }

    @Override
    public String getName(){
        return "Double Hashing";
    }
}
