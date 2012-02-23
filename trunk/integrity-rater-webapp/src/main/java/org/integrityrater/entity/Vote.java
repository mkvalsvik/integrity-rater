package org.integrityrater.entity;

import java.util.Date;


public interface Vote {
    
    Person getCreatedBy();
    Date getCreatedTime();
}
