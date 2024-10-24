package live.mukeshtechlab.userservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import live.mukeshtechlab.userservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;

    // Default Constructor
    public CustomGrantedAuthority(){}

    // Role to GrantedAuthority
    public CustomGrantedAuthority(Role role){
        this.authority = role.getRoleType();
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
