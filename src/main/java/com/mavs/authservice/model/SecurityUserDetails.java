package com.mavs.authservice.model;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

import static java.lang.Boolean.TRUE;

@Data
@Entity
@Builder
@Table(name = "security_user_details")
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUserDetails implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_details_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)

    @Builder.Default
    private Set<Authority> authorities = Sets.newHashSet(Authority.USER);
    private String password;
    private String username;

    @Builder.Default
    private boolean accountNonExpired = TRUE;
    @Builder.Default
    private boolean accountNonLocked = TRUE;
    @Builder.Default
    private boolean credentialsNonExpired = TRUE;
    @Builder.Default
    private boolean enabled = TRUE;
}
