package lfs.server.branch;

import org.springframework.data.repository.CrudRepository;

public interface BranchRepository extends CrudRepository<Branch, Integer>{

	Branch findByName(String name);

}
