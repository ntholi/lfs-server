package com.breakoutms.lfs.server.client_startup;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StartupDataService {

	@PersistenceContext
	EntityManager entityManager;

	public Optional<StartupData> get() {
		var sql = "select name from FuneralScheme";
		TypedQuery<String> query = entityManager.createQuery(sql, String.class);
		var names = query.getResultList();
		Optional<StartupData> res = Optional.empty();
		if(!names.isEmpty()) {
			StartupData startupData = new StartupData();
			startupData.setFuneralSchemes(names);
			res = Optional.of(startupData);
		}
		return res;
	}
}
