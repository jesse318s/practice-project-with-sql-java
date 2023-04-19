/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public abstract class Person {

    public int idNumber;
    public String name;
    public String address;
    public String phoneNumber;
    public String loginName;
    public String password;

    public Person() {
        idNumber = 0;
        name = "";
        address = "";
        phoneNumber = "";
        loginName = "";
        password = "";
    }

    public Person(String name, int idNumber, String address, String phoneNumber, String loginName, String password) {
        this.idNumber = idNumber;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.loginName = loginName;
        this.password = password;
    }

    public void display() {
        System.out.println("ID: " + idNumber);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Phone number: " + phoneNumber);
        System.out.println("Login name: " + loginName);
        System.out.println("Password: " + password);
    }
}
