package com.caferaters.repository;

import java.io.Serializable;

import static org.apache.commons.lang3.exception.ExceptionUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractCrudRepository<T, ID extends Serializable>
	extends AbstractRepository
	implements CrudRepository<T, ID> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractCrudRepository.class);

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ID save(final T entity) {
		try {
			return (ID) getSession().save(entity);
		} catch (final Exception e) {
			logException("Entity({}) is not saved: {}", entity, e);
		}
		return null;
	}

	@Override
	@Transactional
	public void delete(final T entity) {
		try {
			getSession().delete(entity);
		} catch (final Exception e) {
			logException("Entity({}) is not deleted: {}", entity, e);
		}
	}

	@Override
	@Transactional
	public void update(final T entity) {
		try {
			getSession().update(entity);
		} catch (final Exception e) {
			logException("Entity({}) is not updated: {}", entity, e);
		}
	}

	private void logException(final String format, final T entity, final Exception e) {
		logger.error(format, entity, getStackTrace(e));
	}
}