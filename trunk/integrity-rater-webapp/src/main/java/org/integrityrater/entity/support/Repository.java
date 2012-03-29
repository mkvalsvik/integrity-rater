package org.integrityrater.entity.support;

import java.util.List;

import org.catamarancode.type.Name;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.integrityrater.entity.Challenge;
import org.integrityrater.entity.ChallengeVote;
import org.integrityrater.entity.Complaint;
import org.integrityrater.entity.ComplaintVote;
import org.integrityrater.entity.Person;
import org.springframework.orm.hibernate3.SessionFactoryUtils;



public class Repository extends RepositoryBase {
    
    public Repository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public Person loadPerson(long id) {
        return (Person) this.loadPersistentEntity(Person.class, id);
    }
    
    public Complaint loadComplaint(long id) {
        return (Complaint) this.loadPersistentEntity(Complaint.class, id);
    }
    
    public Complaint getComplaint(long id) {
        return (Complaint) this.getPersistentEntity(Complaint.class, id);
    }
    
    public Challenge loadChallenge(long id) {
        return (Challenge) this.loadPersistentEntity(Challenge.class, id);
    }
    
    public Challenge getChallenge(long id) {
        return (Challenge) this.getPersistentEntity(Challenge.class, id);
    }


    public Person findPersonByEmail(String email) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);
        Criteria crit = session.createCriteria(Person.class);
        crit.add(Restrictions.eq("email", email));
        return (Person) crit.uniqueResult();
    }   
    
    public List<Person> findPersonByName(String firstName, String lastName) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);
        Criteria crit = session.createCriteria(Person.class);
        crit.add(Restrictions.eq("name.first", firstName));
        crit.add(Restrictions.eq("name.last", lastName));
        return crit.list();
    }
    
    public List<Person> findPersonByName(Name name) {
        return findPersonByName(name.getFirst(), name.getLast());
    }
    
    public List<Person> findPersonByPartialLastName(String term) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);        
        Query q = session
        .createQuery("from Person p where p.name.last like ?");
        q.setString(0, term + "%");
        return q.list();
    }
    
    public List<Person> findPersonByPartialFirstName(String term) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);        
        Query q = session
        .createQuery("from Person p where p.name.first like ?");
        q.setString(0, term + "%");
        return q.list();
    }
    
    public List<Person> findPersonByPartialName(String firstName, String lastName) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);        
        Query q = session
        .createQuery("from Person p where p.name.first like ? and p.name.last like ?");
        q.setString(0, firstName + "%");
        q.setString(1, lastName + "%");
        return q.list();
    }
    
    public List<Person> findPersonByPartialName(String name) {
        
        List<Person> persons = null;
        
        // Got a comma?
        if (name.indexOf(',') > 0) {
            
            // Last, First
            String first = null;
            String last = null;
            String[] nameParts = name.split(",");
            last = nameParts[0].trim();
            first = nameParts[1].trim();
            persons = this.findPersonByPartialName(first, last);
        } else {
            
            // "first last" or "first" or "last"
            String[] nameParts = name.split(" ");
            if (nameParts.length > 1) {
                
                // Try "first last"
                persons = this.findPersonByPartialName(nameParts[0], nameParts[1]);
                if (persons.isEmpty()) {
                    
                    // Try "last first"
                    persons = this.findPersonByPartialName(nameParts[1], nameParts[0]);
                }
            } else {
                
                // Try "last"
                persons = this.findPersonByPartialLastName(nameParts[1]);
                if (persons.isEmpty()) {
                    
                    // Try "first"
                    persons = this.findPersonByPartialFirstName(nameParts[0]);
                }
            }    
        }                
        
        return persons;
    }            
    
    public List<Complaint> listComplaints(Person person) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);
        Criteria crit = session.createCriteria(Complaint.class);
        crit.add(Restrictions.eq("person", person));
        return crit.list();
    }
    
    public List<ComplaintVote> listComplaintVotes(Complaint complaint) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);
        Criteria crit = session.createCriteria(ComplaintVote.class);
        crit.add(Restrictions.eq("complaint", complaint));
        return crit.list();
    }
    
    public List<ChallengeVote> listChallengeVotes(Challenge challenge) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);
        Criteria crit = session.createCriteria(ChallengeVote.class);
        crit.add(Restrictions.eq("challenge", challenge));
        return crit.list();
    }
    
    public ChallengeVote findChallengeVote(Challenge challenge, Person voter) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);
        Criteria crit = session.createCriteria(ChallengeVote.class);
        crit.add(Restrictions.eq("challenge", challenge));
        crit.add(Restrictions.eq("createdBy", voter));
        return (ChallengeVote) crit.uniqueResult();
    }

    public ComplaintVote findComplaintVote(Complaint complaint, Person voter) {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);
        Criteria crit = session.createCriteria(ComplaintVote.class);
        crit.add(Restrictions.eq("complaint", complaint));
        crit.add(Restrictions.eq("createdBy", voter));
        return (ComplaintVote) crit.uniqueResult();
    }
    
    public List<Person> listPersons() {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);
        Criteria crit = session.createCriteria(Person.class);
        crit.addOrder(Order.asc("name.last"));
        return crit.list();
    }
    
    public List<Complaint> listAllComplaints() {
        Session session = SessionFactoryUtils.getSession(this
                .getSessionFactory(), true);
        Criteria crit = session.createCriteria(Complaint.class);
        crit.addOrder(Order.desc("lastModifiedTime"));        
        return crit.list();
    }

}
