package com.caferaters.adapter;

public interface ModelEntityAdapter<M,E> {
	M toModel(E entity);
	E toEntity(M model);
}
