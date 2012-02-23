package org.integrityrater.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.catamarancode.util.Timestamped;
import org.integrityrater.entity.support.PersistentEntityBase;
import org.integrityrater.service.user.UserContext;


@Entity
public class Challenge extends PersistentEntityBase implements Comparable, Timestamped {

    private String title;
    private String body;
    private Date createdTime;
    private Date lastModifiedTime;
    private Complaint complaint;
    private Person createdBy;
    private List<ChallengeVote> votes = new ArrayList<ChallengeVote>();
    
    @OneToMany(mappedBy = "challenge")
    public List<ChallengeVote> getVotes() {
        return votes;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Did current user vote?
     * @return
     */
    @Transient
    public boolean isVoted() {
        UserContext userContext = getServices().getUserContextLocator().locate();
        ChallengeVote userVote = getServices().getRepository().findChallengeVote(this, userContext.getLoggedInUser());
        return (userVote != null);
    }
    
    @Lob
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }
    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    
    @ManyToOne(optional=false)
    public Complaint getComplaint() {
        return complaint;
    }
    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public void setVotes(List<ChallengeVote> votes) {
        this.votes = votes;
    }
    
    @ManyToOne(optional=false)
    public Person getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }

}
