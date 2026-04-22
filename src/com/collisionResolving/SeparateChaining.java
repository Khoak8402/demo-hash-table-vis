package com.collisionResolving;

import com.table.HashTable;

public class SeparateChaining implements CollisionResolver{
    @Override
    public int resolve(int key, int step, HashTable table){
        //Separate chaining does not require any resolve
        return table.h1(key);
    }

    @Override
    public String getName(){
        return "Separate Chaining";
    }

}
