package com.zth.backoffice.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Customer {

    @SerializedName("isContactInfo")
    @Expose
    private isContactInfo isContactInfo;

    @SerializedName("payment")
    @Expose
    private Payment payment;


    @SerializedName("email")
    @Expose
    private  String email;

    @SerializedName("name")
    @Expose
    private  String name;

    @SerializedName("appliedAt")
    @Expose
    private  String appliedAt;

    @SerializedName("paymentStatus")
    @Expose
    private  String paymentStatus;

    @SerializedName("phoneNumber")
    @Expose
    private  String phoneNumber;

    @SerializedName("age")
    @Expose
    private  String age;

    @SerializedName("attendanceStatus")
    @Expose
    private  String attendanceStatus;

    @SerializedName("isEarly")
    @Expose
    private  String isEarly;

    @SerializedName("isContacted")
    @Expose
    private  Boolean isContacted;

    @SerializedName("learningBackground")
    @Expose
    private  String learningBackground;


    public Customer(isContactInfo isContactInfo, Payment payment, String email, String name, String appliedAt, String paymentStatus, String phoneNumber, String age, String attendanceStatus, String isEarly,Boolean isContacted, String learningBackground) {
        this.isContactInfo = isContactInfo;
        this.payment = payment;
        this.email = email;
        this.name = name;
        this.appliedAt = appliedAt;
        this.paymentStatus = paymentStatus;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.attendanceStatus = attendanceStatus;
        this.isEarly = isEarly;
        this.isContacted = isContacted;
        this.learningBackground = learningBackground;
    }

    public isContactInfo getIsContactInfo() {
        return isContactInfo;
    }

    public Payment getPayment() {
        return payment;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAppliedAt() {
        return appliedAt;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAge() {
        return age;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public String getIsEarly() {
        return isEarly;
    }

    public Boolean getIsContacted() {
        return isContacted;
    }

    public String getLearningBackground() {
        return learningBackground;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setIsContactInfo(com.zth.backoffice.Model.isContactInfo isContactInfo) {
        this.isContactInfo = isContactInfo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAppliedAt(String appliedAt) {
        this.appliedAt = appliedAt;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public void setIsEarly(String isEarly) {
        this.isEarly = isEarly;
    }

    public void setIsContacted(Boolean isContacted) {
        this.isContacted = isContacted;
    }

    public void setLearningBackground(String learningBackground) {
        this.learningBackground = learningBackground;
    }
}
