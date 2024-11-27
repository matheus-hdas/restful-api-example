package com.matheushdas.restfulapi.service;

import com.matheushdas.restfulapi.model.User;
import com.matheushdas.restfulapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username);
        if(username != null) return user;
        throw new UsernameNotFoundException("The user with the username or email provided was not found in our records!");
    }
}
