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
import org.integrityrater.entity.type.ComplaintType;
import org.integrityrater.service.user.UserContext;


@Entity
public class Complaint extends PersistentEntityBase implements Comparable, Timestamped {

    private String title;
    private String body;
    private Date createdTime;
    private Date lastModifiedTime;
    private Person person;
    private Person createdBy;
    private List<Challenge> challenges = new ArrayList<Challenge>();
    private List<ComplaintVote> votes = new ArrayList<ComplaintVote>();
    private ComplaintType type;
    private Date date1;
    private Date date2;
    private String source1;
    private String source2;
    private String quote1;
    private String quote2;
    private String hatTipA;
    private String hatTipB;
    private String hatTipC;
    
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
        ComplaintVote userVote = null;
        if (userContext != null) {
            userVote = getServices().getRepository().findComplaintVote(this, userContext.getLoggedInUser());    
        }        
        return (userVote != null);
    }
    
    @OneToMany(mappedBy = "complaint")
    public List<ComplaintVote> getVotes() {
        return votes;
    }
    
    @OneToMany(mappedBy = "complaint")
    public List<Challenge> getChallenges() {
        return challenges;
    }
    
    public void setChallenges(List<Challenge> list) {
        this.challenges = list;
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
    public Person getPerson() {
        return person;
    }
    
    public void setPerson(Person person) {
        this.person = person;
    }
    public void setVotes(List<ComplaintVote> votes) {
        this.votes = votes;
    }
    
    @ManyToOne(optional=false)
    public Person getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }
    public ComplaintType getType() {
        return type;
    }
    public void setType(ComplaintType type) {
        this.type = type;
    }
    public Date getDate1() {
        return date1;
    }
    public void setDate1(Date date1) {
        this.date1 = date1;
    }
    public Date getDate2() {
        return date2;
    }
    public void setDate2(Date date2) {
        this.date2 = date2;
    }
    public String getSource1() {
        return source1;
    }
    public void setSource1(String source1) {
        this.source1 = source1;
    }
    public String getSource2() {
        return source2;
    }
    public void setSource2(String source2) {
        this.source2 = source2;
    }
    
    @Lob
    public String getQuote1() {
        return quote1;        
    }
    public void setQuote1(String quote1) {
        this.quote1 = quote1;
    }
    
    @Lob
    public String getQuote2() {
        return quote2;
    }
    public void setQuote2(String quote2) {
        this.quote2 = quote2;
    }
    public String getHatTipA() {
        return hatTipA;
    }
    public void setHatTipA(String hatTipA) {
        this.hatTipA = hatTipA;
    }
    public String getHatTipB() {
        return hatTipB;
    }
    public void setHatTipB(String hatTipB) {
        this.hatTipB = hatTipB;
    }
    public String getHatTipC() {
        return hatTipC;
    }
    public void setHatTipC(String hatTipC) {
        this.hatTipC = hatTipC;
    }


}
