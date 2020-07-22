package com.bookstore.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class JpaDAO<E> {
	protected EntityManager entityManager;

	private static EntityManagerFactory entityManagerFactory;

	static {
		entityManagerFactory = Persistence.createEntityManagerFactory("BookStoreWebsite");
	}

	public JpaDAO() {
	}

	public JpaDAO(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public E create(E entity) {
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.flush();
		entityManager.refresh(entity);
		entityManager.getTransaction().commit();

		return entity;
	}

	public E update(E entity) {
		entityManager.getTransaction().begin();
		entity = entityManager.merge(entity);
		entityManager.getTransaction().commit();
		return entity;
	}

	public E find(Class<E> type, Object id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		E entity = entityManager.find(type, id);

		if (entity != null) {
			entityManager.refresh(entity);
		}
		entityManager.close();

		return entity;
	}
	
	public List<E> findWithNamedQuery(String queryName){
		Query query = entityManager.createNamedQuery(queryName);
		return query.getResultList(); 
	}
	
	public List<E> findWithNameQuery(String queryEmail, String email) {
		Query query = entityManager.createNamedQuery(queryEmail).setParameter("email", email);
		return query.getResultList();
	}
	
	public long countAllRows(String queryName) {
		Query query = entityManager.createNamedQuery(queryName);
		long count = (long)query.getSingleResult();
		System.out.println("count = " + count);
		return count;
		
	}
	
	public void close() {
		entityManager.close();
		entityManagerFactory.close();
	}
}
