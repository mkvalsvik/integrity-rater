package org.integrityrater.entity;

import java.util.Date;

import javax.persistence.Entity;

import org.catamarancode.util.Timestamped;
import org.integrityrater.entity.support.PersistentEntityBase;



@Entity
public class AuditEntry extends PersistentEntityBase implements Timestamped {

	private long actorId; // person
	private long subjectId; // "parent" (person, complaint)
	private long objectId; // "child" (complaint or challenge)
    private Date createdTime;
    private Date lastModifiedTime;
	private TransactionType transactionType;
	private String transactionTypeString;

	public static void insert(long actorId, long subjectId, long objectId, TransactionType type) {
	    AuditEntry entry = new AuditEntry();
	    entry.setActorId(actorId);
	    entry.setSubjectId(subjectId);
	    entry.setObjectId(objectId);
	    entry.setTransactionType(type);
	    entry.setTransactionTypeString(type.name());
	    entry.setCreatedTime(new Date());
	    entry.save();
	}
	
	public enum TransactionType {
		COMPLAINT_CREATE, COMPLAINT_UPDATE, CHALLENGE_CREATE, CHALLENGE_UPDATE, PERSON_CREATE, PERSON_SIGNUP, PERSON_CLAIM;
	}

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionTypeString() {
        return transactionTypeString;
    }

    public void setTransactionTypeString(String transactionTypeString) {
        this.transactionTypeString = transactionTypeString;
    }

}
