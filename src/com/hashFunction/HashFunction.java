package com.hashFunction;

public interface HashFunction {
    int hash(int key, int tableSize);
    String getName();
}

