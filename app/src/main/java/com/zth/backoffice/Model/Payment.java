package com.zth.backoffice.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("paymentAmount")
    @Expose
    private String paymentAmount;

    @SerializedName("paymentDate")
    @Expose
    private String paymentDate;

    @SerializedName("collectionMethod")
    @Expose
    private String collectionMethod;

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getcollectionMethod() {
        return collectionMethod;
    }

    public Payment(String paymentAmount, String paymentDate, String collectionMethod) {
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.collectionMethod = collectionMethod;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setcollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }
}
