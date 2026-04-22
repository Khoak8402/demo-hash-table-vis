package com.collisionResolving;

import com.table.HashTable;

public interface CollisionResolver {
    //Resolve strategy
    //This method would find the next cell to insert for key element
    int resolve(int key, int step, HashTable table); 

    String getName();
}


