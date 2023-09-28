package com.chq.common;

public enum Type {
    SELECT(1,3),JUDGE(2,2),FILLING(3,2),COMPREHENSIVE(4,10);


    private int key;
    private int grade;

    Type(int key,int grade) {
        this.key = key;
        this.grade=grade;
    }

    public int getKey() {
        return key;
    }
    public int  getGrade() {
        return grade;
    }
}
