package com.todoList.TodoList.config;

import com.todoList.TodoList.entity.User;
import com.todoList.TodoList.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TodoListUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username);

        if(user == null) throw new UsernameNotFoundException("User details not found for the user: "+username);

        List<GrantedAuthority> authorities = user.getRolesSet()
                .stream()
                .map(authorites -> new SimpleGrantedAuthority(authorites.toString()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(username, user.getEncodedPassword(), authorities);
    }
}
