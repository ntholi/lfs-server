package lfs.server.branch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

@Entity
@Table(indexes = {
        @Index(columnList = "name", name = "unique_branch_name", unique=true)
})
public class Branch {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@Enumerated
	@Column(columnDefinition = "TINYINT UNSIGNED")
	private District district;
	
	@Column(nullable = false, unique = true,
			columnDefinition = "SMALLINT")
	@Digits(integer = 3, fraction = 0)
	private short syncNumber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public short getSyncNumber() {
		return syncNumber;
	}

	public void setSyncNumber(short syncNumber) {
		this.syncNumber = syncNumber;
	}
}
