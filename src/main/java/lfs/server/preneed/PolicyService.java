package lfs.server.preneed;

import org.springframework.stereotype.Service;

import lfs.server.core.BaseService;

@Service
public class PolicyService extends BaseService<Policy, String, PolicyRepository>{

	public PolicyService(PolicyRepository repo) {
		super(repo);
	}

}
