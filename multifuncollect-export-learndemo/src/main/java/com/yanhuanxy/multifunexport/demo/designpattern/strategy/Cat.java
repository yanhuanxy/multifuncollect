package com.yanhuanxy.multifunexport.demo.designpattern.strategy;

import com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.SortComparable;

import java.io.Serializable;
import java.util.logging.Logger;

public class Cat implements SortComparable<Cat>, Serializable {
    private static final Logger logger = Logger.getLogger(Cat.class.getName());

   private int weight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Cat(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Cat tmp) {
        if(this.weight < tmp.weight) return -1;
        else if(this.weight > tmp.weight) return -1;
        else return 0;
    }
}
