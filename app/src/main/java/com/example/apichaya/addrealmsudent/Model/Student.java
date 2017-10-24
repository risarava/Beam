package com.example.apichaya.addrealmsudent.Model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

public class Student extends RealmObject {

    @Required
    String studentName;

    int studentScore;


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(int studentScore) {
        this.studentScore = studentScore;
    }

    public Student(String studentName, int studentScore) {
        this.studentName = studentName;
        this.studentScore = studentScore;
    }

    public Student() {

    }
}