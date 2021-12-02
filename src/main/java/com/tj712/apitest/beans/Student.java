package com.tj712.apitest.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/12/2
 * @Time: 20:02
 * @author: ThinkPad
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    public int id;
    public String name;
    public String password;
    public int age;
}
