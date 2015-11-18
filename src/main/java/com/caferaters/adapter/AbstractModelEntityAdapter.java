package com.caferaters.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public abstract class AbstractModelEntityAdapter<M, E> implements ModelEntityAdapter<M, E> {

	public Collection<M> toModels(Collection<E> entities) {
		if (CollectionUtils.isEmpty(entities)) {
			return Collections.emptyList();
		}

		final List<M> models = new ArrayList<M>();
		for (E entity : entities) {
			models.add(toModel(entity));
		}
		return models;
	}

	public Collection<E> toEntities(Collection<M> models) {
		if (CollectionUtils.isEmpty(models)) {
			return Collections.emptyList();
		}

		final List<E> entities = new ArrayList<E>();
		for (M model : models)
			entities.add(toEntity(model));

		return entities;
	};
}