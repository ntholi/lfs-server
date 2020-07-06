package com.breakoutms.lfs.server.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.breakoutms.lfs.server.persistence.IdGenerator;

import lombok.Data;

@Entity
@Data
@GenericGenerator(
        name = "audit_revision_info_id",          
        strategy = "com.breakoutms.lfs.server.persistence.IdGenerator",
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_LONG)
})
@RevisionEntity(AuditRevisionListener.class)
public class AuditRevisionInfo {

	@Id
	@GeneratedValue(generator = "audit_revision_info_id")
	@RevisionNumber
	private Long id;
	
//	@NotNull THIS WAS COMMENTED ONLY FOR TESTING PURPOSES, PLEASE PLEASE REMOVE IT NOW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//	@Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED")
	private Integer userId;
	
	@RevisionTimestamp
	private Date date;
}
