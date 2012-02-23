package org.integrityrater.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.integrityrater.entity.support.PersistentEntityBase;


@Entity
public class ChallengeVote extends PersistentEntityBase implements Comparable, Vote {

    private Challenge challenge;
    private Person createdBy;
    private Date createdTime;

    @ManyToOne(optional=false)
    public Challenge getChallenge() {
        return challenge;
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

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }

}
