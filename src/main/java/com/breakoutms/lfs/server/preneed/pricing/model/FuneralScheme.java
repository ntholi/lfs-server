package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;

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
@Table(indexes = {
        @Index(columnList = "name", name = "unique_funeral_scheme_name", unique=true)
})
public class FuneralScheme extends AuditableEntity<Integer> {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@NotBlank
	@Size(min = 1, max = 35)
	@Column(nullable=false, length = 35)
	private String name;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer=6, fraction=2)
	@Column(precision=8, scale=2)
	private BigDecimal registrationFee;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Max(255)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private int monthsBeforeActive;
	
	//Whether or not registration fee includes first premium
	private boolean includesFirstPremium;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer=6, fraction=2)
	@Column(precision=8, scale=2)
	private BigDecimal penaltyFee;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	@Max(255)
	private int monthsBeforePenalty;
	
	@OneToMany(mappedBy="funeralScheme", 
			cascade=CascadeType.ALL) 
	private Set<Premium> premiums;
	
	@OneToMany(mappedBy="funeralScheme", 
			cascade=CascadeType.ALL)
	private Set<DependentBenefit> dependentBenefits;
	
	@OneToMany(mappedBy="funeralScheme", 
			cascade=CascadeType.ALL)
	private Set<FuneralSchemeBenefit> benefits;
	
	@OneToMany(mappedBy="funeralScheme", 
			cascade=CascadeType.ALL)
	private Set<PenaltyDeductible> penaltyDeductibles;
	
	public FuneralScheme(String name) {
		this.name = name;
	}
	
//	public FuneralSchemeBenefit getBenefit(ItemType itemType) {
//		
//		for(FuneralSchemeBenefit f : PolicyDAO.getFuneralSchemeBenefit(this)) {
//			if(f.getItemType() == itemType) {
//				return f;
//			}
//		}
//		return null;
//	}
//	
//	public DependentBenefit getDependentBenefitByAge(int age) {
//		FuneralSchemeDAO dao = new FuneralSchemeDAO();
//		for(DependentBenefit dp: dao.getDependentBenefits(this)) {
//			if(age >= dp.getMinmumAge() && age <= dp.getMaximumAge()) {
//				return dp;
//			}
//		}
//		return null;
//	}
//	
//	public double getPenaltyDeductableByMonths(int unpaidMonths) {
//		double amount = 0;
//		FuneralSchemeDAO dao = new FuneralSchemeDAO();
//		for(PenaltyDeductable pd: dao.getPenaltyDeductables(this)) {
//			if(pd.getMonths() == unpaidMonths) {
//				return pd.getAmount();
//			}
//			if(unpaidMonths > pd.getMonths()) {
//				amount = -1;
//			}
//		}
//		
//		return amount;
//	}
}
