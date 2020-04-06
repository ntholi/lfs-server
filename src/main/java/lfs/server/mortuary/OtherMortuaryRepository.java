package lfs.server.mortuary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherMortuaryRepository extends JpaRepository<OtherMortuary, Integer>{
	
	Optional<OtherMortuary> findFirstByName(String name);

//	@Query(value="SELECT EXISTS( SELECT 1 FROM other_mortuary WHERE id = :id LIMIT 1)", nativeQuery = true)
//	int exists(@Param("id") Integer id);

}
