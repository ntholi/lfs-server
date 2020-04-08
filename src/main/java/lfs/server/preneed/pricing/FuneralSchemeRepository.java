package lfs.server.preneed.pricing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuneralSchemeRepository extends JpaRepository<FuneralScheme, Integer> {

}
