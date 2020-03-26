package lfs.server.mortuary;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorpseRepository extends PagingAndSortingRepository<Corpse, String>{

	@Query(value = "SELECT distinct received_by FROM corpse", nativeQuery = true)
	List<String> getReceivedBy();

}
