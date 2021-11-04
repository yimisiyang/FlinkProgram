package com.tj712.wc.entity;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/4
 * @Time: 14:55
 * @author: ThinkPad
 */
public class J_Student {
    public int id;
    public String name;
    public int age;

    public J_Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    public static J_Student of(int id, String name, int age) {
        return new J_Student(id, name, age);
    }
}
