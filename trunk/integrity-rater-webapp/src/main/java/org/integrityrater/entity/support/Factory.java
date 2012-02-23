package org.integrityrater.entity.support;

import org.catamarancode.type.Name;
import org.integrityrater.entity.Challenge;
import org.integrityrater.entity.Complaint;
import org.integrityrater.entity.Person;


public class Factory  {
    
    public static Complaint createComplaint() {
        return createComplaint(createPerson());
    }
    
    public static Complaint createComplaint(Person person) {
        Complaint complaint = new Complaint();
        complaint.setPerson(person);
        return complaint;
    }

    public static Challenge createChallenge(Complaint complaint) {
        Challenge challenge = new Challenge();
        challenge.setComplaint(complaint);
        return challenge;
    }
    
    public static Person createPerson() {
        Person person = new Person();
        person.setName(createName());
        return person;
    }
    
    public static Name createName() {
        return new Name();
    }
}
