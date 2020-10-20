package com.breakoutms.lfs.server.products.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.TransportType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@PrimaryKeyJoinColumn(name = "product_id")
public class TransportPrice extends Product {

	@Enumerated
	private TransportType transportType;
	@Column(name="trans_from")
	private String from;
	@Column(name="trans_to")
	private String to;
}