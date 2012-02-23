package org.integrityrater.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.catamarancode.util.Timestamped;
import org.integrityrater.entity.support.PersistentEntityBase;


@Entity
public class ComplaintVote extends PersistentEntityBase implements Comparable, Vote {

    private Complaint complaint;
    private Person createdBy;
    private Date createdTime;

    @ManyToOne(optional=false)
    public Complaint getComplaint() {
        return complaint;
    }

    @ManyToOne(optional=false)
    public Person getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }



}
