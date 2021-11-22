package com.zth.backoffice.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class isContactInfo {
    @SerializedName("contactedBy")
    @Expose
    private String contactedBy;
    @SerializedName("customerConcerns")
    @Expose
    private String customerConcerns;
    @SerializedName("adminComments")
    @Expose
    private String adminComments;

    public String getContactedBy() {
        return contactedBy;
    }

    public String getCustomerConcerns() {
        return customerConcerns;
    }

    public String getAdminComments() {
        return adminComments;
    }

    public isContactInfo(String contactedBy, String customerConcerns, String adminComments) {
        this.contactedBy = contactedBy;
        this.customerConcerns = customerConcerns;
        this.adminComments = adminComments;
    }

    public void setContactedBy(String contactedBy) {
        this.contactedBy = contactedBy;
    }

    public void setCustomerConcerns(String customerConcerns) {
        this.customerConcerns = customerConcerns;
    }

    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }
}
