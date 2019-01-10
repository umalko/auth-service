package com.mavs.authservice.repository;

import com.mavs.authservice.model.SecurityUserDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityUserDetailsRepository extends CrudRepository<SecurityUserDetails, Integer> {

    SecurityUserDetails findByUsername(String username);
}
