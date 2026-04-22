package com.collisionResolving;

import com.table.HashTable;

public class QuadraticProbling implements CollisionResolver{
    @Override
    public int resolve(int key, int step, HashTable table){
        int hashCode= table.h1(key);
        return (hashCode + step*step)%table.getSize();
    }

    @Override
    public String getName(){
        return "Quadratic Probling";
    }
}
