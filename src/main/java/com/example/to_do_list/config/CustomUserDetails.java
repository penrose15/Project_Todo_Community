//package com.example.to_do_list.config;
//
//import com.example.to_do_list.domain.Users;
//import com.example.to_do_list.domain.role.Role;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//@Getter
//public class CustomUserDetails implements UserDetails, OAuth2User {
//
//    private Long id;
//    private String email;
//    private Collection<? extends GrantedAuthority> authorities;
//    private Map<String, Object> attributes;
//
//    public CustomUserDetails(Long id, String email, Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.email = email;
//        this.authorities = authorities;
//    }
//
//    public static CustomUserDetails create(Users users) {
//        List<GrantedAuthority> authorities1 = Collections.
//                singletonList(new SimpleGrantedAuthority(Role.USER.getRole()));
//
//        return new CustomUserDetails(users.getUsersId(),
//                users.getEmail(), authorities1);
//    }
//
//    public static CustomUserDetails create(Users users, Map<String, Object> attributes) {
//        CustomUserDetails userDetails = CustomUserDetails.create(users);
//        userDetails.setAttributes(attributes);
//        return userDetails;
//    }
//
//    @Override
//    public String getName() {
//        return String.valueOf(id);
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return attributes;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    public void setAttributes(Map<String, Object> attributes) {
//        this.attributes = attributes;
//    }
//}
