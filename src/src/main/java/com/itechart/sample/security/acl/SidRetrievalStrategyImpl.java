package com.itechart.sample.security.acl;

import com.itechart.sample.security.auth.GroupAuthority;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Strategy interface that provides an ability to determine the {@link Sid} instances
 * applicable for an {@link Authentication}
 *
 * @author andrei.samarou
 */
public class SidRetrievalStrategyImpl implements SidRetrievalStrategy {

    public List<Sid> getSids(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<Sid> sids = new ArrayList<>(authorities.size() + 1);
        sids.add(new PrincipalSid(authentication));
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof GroupAuthority) {
                sids.add(new GrantedAuthoritySid(authority.getAuthority()));
            }
        }
        return sids;
    }
}