package com.example.project_locate_bus;

public class User {

    public User() {
    }

    String Name,Mobile,Department,Month,Amount,Date,Dues;

    public User(String name, String mobile, String department, String month, String amount, String date, String dues) {
        Name = name;
        Mobile = mobile;
        Department = department;
        Month = month;
        Amount = amount;
        Date = date;
        Dues = dues;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDues() {
        return Dues;
    }

    public void setDues(String dues) {
        Dues = dues;
    }
}
