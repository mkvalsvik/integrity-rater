package org.integrityrater.entity.support;

import org.catamarancode.util.ClassUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author mkvalsvik
 */
public class RepositoryBase {

    private static Logger logger = LoggerFactory.getLogger(RepositoryBase.class);
    
    private SessionFactory sessionFactory;

    public RepositoryBase(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Loads an object and wraps objects not found in a local exception in order
     * to reduce the tight coupling with Hibernate
     * 
     * @param clazz
     *            - the class of the desired object
     * @param id
     *            - the identifier of desired object
     * @throws com.RepositoryBaseObjectNotFoundException.autosite.domain.repository.RepositoryObjectNotFoundException.repository.ObjectNotFoundException
     *             if object not found
     */
    protected Object loadPersistentEntity(Class clazz, Long id) {
        if (!ClassUtils.isInstanceOf(clazz, PersistentEntityBase.class)) {
            throw new RuntimeException(
                    "Class does not inherit from persistent entity base class: "
                            + clazz.getName());
        }
        Session session = SessionFactoryUtils.getSession(this
                .sessionFactory, true);
        Object obj = null;
        
        try {
            obj = session.load(clazz, id);            
        } catch (org.hibernate.ObjectNotFoundException e) {
            throw new RepositoryBaseObjectNotFoundException(e
                    .getMessage(), e);
        }
        return obj;
    }
    
    protected Object getPersistentEntity(Class clazz, Long id) {
        if (!ClassUtils.isInstanceOf(clazz, PersistentEntityBase.class)) {
            throw new RuntimeException(
                    "Class does not inherit from persistent entity base class: "
                            + clazz.getName());
        }
        Session session = SessionFactoryUtils.getSession(this
                .sessionFactory, true);
        Object obj = session.get(clazz, id);        
        return obj;
    }

    /**
     * Bind a session to ThreadLocal for use by this thread. Note Spring MVC
     * Framework will normally do this automatically.
     */
    public void manuallyBindHibernateSessionToThreadLocal() {
        Session session = SessionFactoryUtils.getSession(this.sessionFactory,
                true);
        /*
        session.setFlushMode(FlushMode.MANUAL);
        logger.warn("FLUSHMODE: " + session.getFlushMode());
        */
        TransactionSynchronizationManager.bindResource(this.sessionFactory,
                new SessionHolder(session));
    }

    /**
     * Unbind session from ThreadLocal and release session
     */
    public void manuallyUnBindHibernateSessionFromThreadLocal() {
        Session session = SessionFactoryUtils.getSession(this.sessionFactory,
                false);
        if (session != null) {
            TransactionSynchronizationManager
                    .unbindResource(this.sessionFactory);
            SessionFactoryUtils.releaseSession(session, this.sessionFactory);
        }
    }

    /**
     * TODO: Move flush to the end of OpenSessionInView to speed things up and
     * reduce number of flushes
     */
    public void flush() {
        Session session = SessionFactoryUtils.getSession(this.sessionFactory,
                true);
        session.flush();
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }    

}
