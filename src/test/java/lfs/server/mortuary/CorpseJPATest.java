package lfs.server.mortuary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
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

import com.google.common.collect.Lists;

import lfs.server.branch.Branch;
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
	
	public CorpseJPATest() {}
	
	@Before
	public void init() {
		Branch branch = new Branch();
		branch.setSyncNumber((short)256);
		when(currentBranch.get()).thenReturn(branch);
		
		PowerMockito.mockStatic(BeanUtil.class);
        BDDMockito.given(BeanUtil.getBean(CurrentBranch.class)).willReturn(currentBranch);
	}
	
	@Test
	public void testFindByCorpse_returnsExpectedResults() {
		
		String name = "MKMs";
		
		Corpse corpse = new Corpse();
		corpse.setTransferredFrom(new OtherMortuary(name));
		Corpse savedCorpse = corpseRepo.save(corpse);
		List<OtherMortuary> result = Lists.newArrayList(otherRepository.findByCorpse(savedCorpse));
		
		assertThat(result).size().isEqualTo(1);
		assertThat(result.get(0).getName()).isEqualTo(name);
	}
	
	@Test
	public void testFindByCorpse_returnsExpectedResults2() {
		String name1 = "MKM";
		String name2 = "Sentebale";
		String corpseFirstName = "Molise";
		
		Corpse corpse1 = new Corpse();
		corpse1.setTransferredFrom(new OtherMortuary(name1));
		corpseRepo.save(corpse1);
		
		Corpse corpse2 = new Corpse();
		corpse2.setNames(corpseFirstName);
		corpse2.setTransferredFrom(new OtherMortuary(name2));
		Corpse savedCorpse2 = corpseRepo.save(corpse2);
		
		List<OtherMortuary> result = Lists.newArrayList(otherRepository.findByCorpse(savedCorpse2));
		List<Corpse> corpseList = Lists.newArrayList(corpseRepo.findAll());
		
		assertThat(corpseList.size()).isEqualTo(2);
		assertThat(savedCorpse2.getNames()).isEqualTo(corpseFirstName);
		assertThat(result).size().isEqualTo(1);
		assertThat(result.get(0).getName()).isEqualTo(name2);
	}
}
