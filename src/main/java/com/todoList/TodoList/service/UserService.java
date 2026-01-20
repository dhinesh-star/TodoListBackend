package com.todoList.TodoList.service;

import com.todoList.TodoList.config.TodoListUserDetailsService;
import com.todoList.TodoList.constants.ApplicationConstants;
import com.todoList.TodoList.entity.User;
import com.todoList.TodoList.enumPackage.Roles;
import com.todoList.TodoList.repository.UserRepository;
import com.todoList.TodoList.requestDTO.AddNewUserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TodoListUserDetailsService todoListUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Environment environment;

    public String addNewUser(AddNewUserDTO addNewUserDTO) throws Exception{
        User checkUserExist = userRepository.findUserByUserName(addNewUserDTO.getUserName());
        if(checkUserExist != null) throw new Exception("User Already exist! Please enter other user");

        String rawPassword = addNewUserDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User newUserCreated = new User();
        newUserCreated.setEmailId(addNewUserDTO.getEmailId());
        newUserCreated.setUserName(addNewUserDTO.getUserName());
        newUserCreated.setEncodedPassword(encodedPassword);

        //Setting the basic roles for the user
        Set<Roles> rolesSet = newUserCreated.getRolesSet();
        if(rolesSet == null) rolesSet = new HashSet<>();
        rolesSet.add(Roles.ROLE_USER);
        newUserCreated.setRolesSet(rolesSet);

        userRepository.save(newUserCreated);
        return "New User Added Successfully";
    }
    public String authenicateUser(String userName, String rawPassword){
        String jwtToken = null;
        Authentication unAuthenticatedResponse = UsernamePasswordAuthenticationToken.unauthenticated(userName, rawPassword);
        Authentication authentication = authenticationManager.authenticate(unAuthenticatedResponse);
        // User user = todoListUserDetailsService.loadUserByUsername(userName);
        if(authentication != null && authentication.isAuthenticated() == true && environment != null){
            String secret = environment.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                    ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            jwtToken = Jwts.builder()
                    .subject("Todo-List Authentication")
                    .claim("username", authentication.getName())
                    .claim("authorities", authorities)
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + 86400 * 1000))
                    .signWith(secretKey)
                    .compact();
            return jwtToken;
        }
        return jwtToken;
    }
}
