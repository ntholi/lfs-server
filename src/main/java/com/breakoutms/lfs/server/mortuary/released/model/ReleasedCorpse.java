package com.breakoutms.lfs.server.mortuary.released.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "released_corpse_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE sales SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class ReleasedCorpse extends AuditableEntity<Integer> {

	@Id
	@GeneratedValue(generator = "released_corpse_id")
	private Integer id;
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Corpse corpse;
	@NotNull
	private LocalDateTime date;
	@Column(length = 50)
	private String dressedBy;
	@Column(length = 50)
	private String coffinedBy;
}
