package com.example.myapplication;

public class Counter {
    private int min;
    private int start;
    private int step;
    private int current;

    public Counter() {
        this.min = 0;
        this.start = 0;
        this.step = 1;
        this.current = this.start;
    }

    public Counter(int min, int max, int start, int step) {
        this.min = min;
        this.start = start;
        this.step = step;
        this.current = this.start;
    }

    public void increment(){
        current += step;
    }

    public void reset(){
        current = start;
    }

    public int getCurrent() {
        return current;
    }
}