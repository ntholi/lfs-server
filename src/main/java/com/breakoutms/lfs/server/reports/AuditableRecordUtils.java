package com.breakoutms.lfs.server.reports;

import java.time.LocalDate;
import java.time.LocalTime;

import com.breakoutms.lfs.server.audit.QAuditableEntity;
import com.querydsl.jpa.impl.JPAQuery;

public interface AuditableRecordUtils {

	public static <T> JPAQuery<T> filter(QAuditableEntity table, LocalDate from, LocalDate to, Integer branch, Integer user,
			JPAQuery<T> query) {
		query = query.where(table.deleted.isFalse());
		if(from != null) {
			query = query.where(table.createdAt.after(from.atStartOfDay()));
		}
		if(to != null) {
			query = query.where(table.createdAt.before(to.atTime(LocalTime.MAX)));
		}
		if(branch != null) {
			query = query.where(table.branch.id.eq(branch));
		}
		if(user != null) {
			query = query.where(table.createdBy.id.eq(user));
		}
		return query;
	}
}
