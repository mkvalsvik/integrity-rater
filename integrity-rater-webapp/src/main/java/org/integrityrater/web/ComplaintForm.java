package org.integrityrater.web;

import org.integrityrater.entity.Complaint;


public class ComplaintForm {
    
    public static final String KEY = "complaintForm"; 

    private String complaintTargetName;
    
    private Complaint complaint;

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public String getComplaintTargetName() {
        return complaintTargetName;
    }

    public void setComplaintTargetName(String complaintTargetName) {
        this.complaintTargetName = complaintTargetName;
    }


}
