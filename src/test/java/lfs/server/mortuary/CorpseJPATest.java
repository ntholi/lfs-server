package lfs.server.mortuary;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import lfs.server.branch.Branch;
import lfs.server.branch.BranchRepository;
import lfs.server.branch.CurrentBranch;
import lfs.server.util.BeanUtil;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@DataJpaTest
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*", "javax.management.*"})
@PrepareForTest(BeanUtil.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace=Replace.NONE)
class CorpseJPATest {

	@Autowired
	private OtherMortuaryRepository otherRepository;
	
	@Autowired
	private CorpseRepository corpseRepo;
	
	@MockBean
	CurrentBranch currentBranch;
	
	@Autowired
	private BranchRepository branchRepo;
	
	public CorpseJPATest() {}
	
	@Before
	public void init() {
		Branch branch = branchRepo.findByName("Maseru").orElse(null);
		if(branch == null) {
			throw new RuntimeException("Maseru branch not found in db, cannot continue");
		}
		when(currentBranch.get()).thenReturn(branch);
		
		PowerMockito.mockStatic(BeanUtil.class);
        BDDMockito.given(BeanUtil.getBean(CurrentBranch.class)).willReturn(currentBranch);
	}
	
//	@Test
//	public void testOtherMortuaryRepository_findByCorpse_when_saving_corpseWithMortuary() {
//		//Arrange 
//		String mortuary = "MKM";
//		String corpseName = "Thabo Lebese";
//		
//		Corpse corpse = new Corpse();
//		corpse.setNames(corpseName);
//		corpse.setTransferredFrom(new OtherMortuary(mortuary));
//		
//		//Act 
//		Corpse savedCorpse = corpseRepo.save(corpse);
//		corpseRepo.save(corpse);
//		List<OtherMortuary> list = Lists.newArrayList(otherRepository.findByCorpse(savedCorpse.getTagNo()));
//		
//		//Assert
//		assertThat(savedCorpse.getNames()).isEqualTo(corpseName);
//		assertThat(list).size().isEqualTo(1);
//		assertThat(list.get(0).getName()).isEqualTo(mortuary);
//	}
	
//	@Test
//	public void testOtherMortuaryRepository_findByCorpse_when_saving_corpseWithoutMortuary() {
//		//Arrange 
//		String corpseName = "Thabo Lebese";
//		Corpse corpse = new Corpse();
//		corpse.setNames(corpseName);
//		
//		//Act 
//		Corpse savedCorpse = corpseRepo.saveAndFlush(corpse);
//		List<OtherMortuary> list = Lists.newArrayList(otherRepository.findByCorpse(savedCorpse.getTagNo()));
//		
//		//Assert
//		assertThat(list).isEmpty();
//		assertThat(savedCorpse.getNames()).isEqualTo(corpseName);
//	}
}
