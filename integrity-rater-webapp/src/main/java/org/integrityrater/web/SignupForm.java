package org.integrityrater.web;

import org.integrityrater.entity.Person;


public class SignupForm {
    
    public static final String KEY = "signupForm"; 
    
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Long subjectId; // Long not long so that it can be null when not set
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

}
