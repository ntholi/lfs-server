package lfs.server.mortuary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherMortuaryRepository extends CrudRepository<OtherMortuary, Integer>{

	@Query("SELECT t FROM Corpse c JOIN c.transferredFrom t where c = :corpse")
	Iterable<OtherMortuary> findByCorpse(@Param("corpse") Corpse corpse);

}
