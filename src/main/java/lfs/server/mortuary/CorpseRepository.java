package lfs.server.mortuary;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorpseRepository extends PagingAndSortingRepository<Corpse, String>{

}
