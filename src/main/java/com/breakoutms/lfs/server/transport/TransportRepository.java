package com.breakoutms.lfs.server.transport;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TransportRepository extends JpaRepository<Transport, String>, JpaSpecificationExecutor<Transport>{

	@Query("from Vehicle where registrationNumber = :registrationNumber")
	Optional<Vehicle> findVehicleByRegNumber(String registrationNumber);

}
