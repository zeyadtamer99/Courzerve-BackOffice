package com.zth.backoffice.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Round {

    private int roundNumber;
    private String roundStatus;
    private String roundId;
    private String startDate;
    private String duration;
    private int numberOfLectures;
    @SerializedName("customersApplied")
    @Expose
    private ArrayList<Customer> customerApplied = null;
    @SerializedName("slots")
    @Expose
    private ArrayList<Slots> slots = null;


    public ArrayList<Customer> getCustomerApplied() {
        return customerApplied;
    }

    public String getDuration() {
        return duration;
    }

    public String getRoundId() {
        return roundId;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getNumberOfLectures() {
        return numberOfLectures;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public String getRoundStatus() {
        return roundStatus;
    }

    public ArrayList<Slots> getSlots() {
        return slots;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setRoundStatus(String roundStatus) {
        this.roundStatus = roundStatus;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setNumberOfLectures(int numberOfLectures) {
        this.numberOfLectures = numberOfLectures;
    }

    public void setCustomerApplied(ArrayList<Customer> customerApplied) {
        this.customerApplied = customerApplied;
    }

    public void setSlots(ArrayList<Slots> slots) {
        this.slots = slots;
    }
}
