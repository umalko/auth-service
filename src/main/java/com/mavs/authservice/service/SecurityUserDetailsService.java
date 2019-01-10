package com.mavs.authservice.service;

import com.mavs.authservice.model.SecurityUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityUserDetailsService extends UserDetailsService {

    SecurityUserDetails save(SecurityUserDetails userDetails);
}
