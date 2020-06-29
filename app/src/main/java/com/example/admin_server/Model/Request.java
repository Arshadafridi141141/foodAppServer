package com.example.admin_server.Model;

import android.util.Log;

import java.util.List;

public class Request {
    public String name;
    public String phone;
    public String total;
    public String address;
    public String status;
    List<Order> foods;
    public Request(){}

    public Request(String name, String phone, String total, String address) {
        this.name = name;
        this.phone = phone;
        this.total = total;
        this.address = address;
        this.status="0";

    }

    public Request(String name, String phone, String total, String address, List<Order> foods) {
        this.name = name;
        this.phone = phone;
        this.total = total;
        this.address = address;
        this.foods = foods;
        this.status="0";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
