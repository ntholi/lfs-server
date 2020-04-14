package lfs.server.preneed.pricing;

import java.math.BigDecimal;
import java.util.List;

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

import lfs.server.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
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
	@Column(nullable=false, length = 25)
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
			cascade=CascadeType.ALL, 
			orphanRemoval=true) 
	private List<Premium> premiums;
	
	@OneToMany(mappedBy="funeralScheme", 
			cascade=CascadeType.ALL, 
			orphanRemoval=true)
	private List<DependentBenefit> dependentBenefits;
	
	@OneToMany(mappedBy="funeralScheme", 
			cascade=CascadeType.ALL, 
			orphanRemoval=true)
	private List<FuneralSchemeBenefit> benefits;
	
	@OneToMany(mappedBy="funeralScheme", 
			cascade=CascadeType.ALL, 
			orphanRemoval=true)
	private List<PenaltyDeductable> penaltyDeductables;
	
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
