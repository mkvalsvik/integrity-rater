package org.integrityrater.entity;

import java.util.Date;

import javax.persistence.Entity;

import org.catamarancode.util.Timestamped;
import org.integrityrater.entity.support.PersistentEntityBase;
import org.integrityrater.entity.type.CommentTargetType;


@Entity
public class Comment extends PersistentEntityBase implements Comparable, Timestamped {

    private String body;
    private Date createdTime;
    private Date lastModifiedTime;
    private Long commentTargetId;
    private CommentTargetType target;
    
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
    public Long getInResponseToCommentId() {
        return commentTargetId;
    }
    public void setInResponseToCommentId(Long inResponseToCommentId) {
        this.commentTargetId = inResponseToCommentId;
    }
    public CommentTargetType getTarget() {
        return target;
    }
    public void setTarget(CommentTargetType target) {
        this.target = target;
    }
    

}
