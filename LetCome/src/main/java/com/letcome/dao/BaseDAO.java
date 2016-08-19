package com.letcome.dao;

import org.hibernate.SessionFactory;

/**
 * Created by rjt on 16/8/19.
 */
public class BaseDAO {
    protected SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
