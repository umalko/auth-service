package com.mavs.authservice.service.impl;

import com.mavs.authservice.model.SecurityUserDetails;
import com.mavs.authservice.repository.SecurityUserDetailsRepository;
import com.mavs.authservice.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SecurityUserDetailsServiceImpl implements SecurityUserDetailsService {

    private final SecurityUserDetailsRepository securityUserDetailsRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    public SecurityUserDetailsServiceImpl(SecurityUserDetailsRepository securityUserDetailsRepository) {
        this.securityUserDetailsRepository = securityUserDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUserDetails securityUserDetails = securityUserDetailsRepository.findByUsername(username);
        if (securityUserDetails != null) {
            return securityUserDetails;
        } else {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
    }

    @Override
    public SecurityUserDetails save(SecurityUserDetails userDetails) {
        userDetails.setPassword(encoder.encode(userDetails.getPassword()));
        return securityUserDetailsRepository.save(userDetails);
    }
}
