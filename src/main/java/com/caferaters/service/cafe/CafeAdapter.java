package com.caferaters.service.cafe;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.caferaters.adapter.AbstractModelEntityAdapter;
import com.caferaters.model.Cafe;
import com.caferaters.repository.entity.CafeEntity;

@Component
public class CafeAdapter extends AbstractModelEntityAdapter<Cafe, CafeEntity> {

	@Override
	public Cafe toModel(CafeEntity entity) {
		Cafe cafe = new Cafe();
		BeanUtils.copyProperties(entity, cafe);
		return cafe;
	}

	@Override
	public CafeEntity toEntity(Cafe cafe) {
		CafeEntity cafeEntity = new CafeEntity();
		BeanUtils.copyProperties(cafe, cafeEntity);
		return cafeEntity;
	}

	public CafeEntity toEntity(Cafe cafe, String... ignoreProperties) {
		CafeEntity cafeEntity = new CafeEntity();
		BeanUtils.copyProperties(cafe, cafeEntity, ignoreProperties);
		return cafeEntity;
	}
}
