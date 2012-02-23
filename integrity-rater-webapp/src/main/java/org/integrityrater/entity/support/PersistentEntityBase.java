package org.integrityrater.entity.support;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.catamarancode.util.ThreadLocalUtils;
import org.catamarancode.util.Timestamped;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.integrityrater.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;




@MappedSuperclass
public abstract class PersistentEntityBase extends IdentifiableObjectBase {

	private static Logger logger = LoggerFactory.getLogger(PersistentEntityBase.class);

	private long id;

	/**
	 * Allows access to outside services from persistent domain objects.  Must be statically injected.
	 */
	private static EntityServices services;
	
	/**
	 * Allows internal access to hibernate session factory.  Must be statically injected.
	 */
	private static SessionFactory sessionFactory;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	@Transient
	protected static EntityServices getServices() {
		return services;
	}
	
	/**
	 * Saves the entity using the injected sessionFactory.
	 * 
	 * Note that we are using SessionFactoryUtils.getSession(..) to retrieve the
	 * hibernate session bound to threadlocal instead of relying on Spring's
	 * HibernateTemplate to do it. Either way probably works but using
	 * SessionFactoryUtils allows for greater control of session binding and
	 * release, which was found to be useful when setting up unit tests that
	 * include lazy loading of objects (in assert statements).
	 * @see http://today.java.net/pub/a/today/2005/10/11/testing-hibernate-mapping.html?page=2
	 * 
	 * @return
	 */
	public long save() {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		// logger.warn("FLUSHMODE: " + session.getFlushMode());
		if (this instanceof Timestamped) {
            ((Timestamped) this).setLastModifiedTime(new Date());                
        }
		if (this.getId().longValue() == 0) {
		    if (this instanceof Timestamped) {
		        ((Timestamped) this).setCreatedTime(new Date());		        
		    }
			session.save(this);
		} else {
			
			// Only call explicit update if instanced is not known to the session (o/w it will be updated at flush time)
			if (!session.contains(this)) {
				session.update(this);
			}
		}
		
		// TODO: Move flush to the end of OpenSessionInView somehow
		session.flush(); //Need to manually flush, as we've removed AUTO_FLUSH from the OpenSessionInViewFilter
		return this.getId();
	}

	@SuppressWarnings("unused")
	private void setId(long id) {
		this.id = id;
	}

	@Transient
	public static void setServices(EntityServices s) {
		services = s;
	}
	
	@Transient
	public static void setSessionFactory(SessionFactory s) {
		sessionFactory = s;
	}

}