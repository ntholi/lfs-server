package lfs.server.preneed.pricing;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lfs.server.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class FuneralScheme extends AuditableEntity<Integer> {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@Column(nullable=false, unique=true)
	private String name;
	
	@OneToMany(mappedBy="funeral_scheme_id", 
			cascade=CascadeType.ALL, 
			orphanRemoval=true) 
	private List<Premium> premiums;
	
	@OneToMany(mappedBy="funeral_scheme_id", 
			cascade=CascadeType.ALL, 
			orphanRemoval=true)
	private List<DependentBenefit> dependentBenefits;
	
	@OneToMany(mappedBy="funeral_scheme_id", 
			cascade=CascadeType.ALL, 
			orphanRemoval=true)
	private List<FuneralSchemeBenefit> benefits;
	
	private double registrationFee;
	
	private int monthsBeforeActive;
	
	//Whether or not registration fee includes first premium
	private boolean firstPremium;
	
	private double penaltyFee;
	
	private int monthsBeforePenalty;
	
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
