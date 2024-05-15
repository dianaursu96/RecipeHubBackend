package com.demo.recipeappbackend.service;
import com.demo.recipeappbackend.dtos.LoginRequestDTO;
import com.demo.recipeappbackend.dtos.LoginResponseDTO;
import com.demo.recipeappbackend.dtos.RegisterRequestDTO;
import com.demo.recipeappbackend.errors.*;
import com.demo.recipeappbackend.repositories.UserRepository;
import com.demo.recipeappbackend.security.Role;
import com.demo.recipeappbackend.security.UserDetailsImplementation;
import com.demo.recipeappbackend.models.UsersToFavourites;
import com.demo.recipeappbackend.models.User;
import com.demo.recipeappbackend.models.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoginRegisterService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWTService jwtHelper;

    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User with email " + loginRequest.getEmail() + " does not exist");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtHelper.generateJwtCookie(userDetails);

        User user = userDetails.getUser();

        List<Integer> favourites = user.getFavouriteRecipes().stream()
                .map(UsersToFavourites::getRecipe)
                .map(Recipe::getId)
                .collect(Collectors.toList());

        LoginResponseDTO responseDTO = new LoginResponseDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                jwtCookie.getValue(),
                user.getId(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(""),
                "Login successful",
                favourites
        );

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(responseDTO);
    }

    @Transactional
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailUsedException("Email is already in use.");
        }
        if (!isValidEmail(registerRequestDTO.getEmail())) {
            throw new InvalidEmailException("Invalid email format.");
        }
        if (!isValidPassword(registerRequestDTO.getPassword())) {
            throw new InvalidPasswordException("Password needs to be minimum 8 characters long and contain at least one uppercase letter and one digit");
        }
        String role = registerRequestDTO.getRole();
        if (!(role.equals("READER") || role.equals("ADMIN")|| role.equals("CHEF"))) {
            throw new InvalidRoleException("Invalid role: " + role);
        }

        User user = new User(null, registerRequestDTO.getFirstName(), registerRequestDTO.getLastName(), registerRequestDTO.getEmail(), encoder.encode(registerRequestDTO.getPassword()), Role.valueOf(role), null, null);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @Transactional
    public ResponseEntity<?> changeEmail(String newEmail) {
        if (!isValidEmail(newEmail)) {
            throw new InvalidEmailException("Invalid email format.");
        }

        if (userRepository.existsByEmail(newEmail)) {
            throw new EmailUsedException("Email is already in use.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found."));

        user.setEmail(newEmail);
        userRepository.save(user);

        return ResponseEntity.ok("Email updated successfully.");
    }
    @Transactional
    public ResponseEntity<?> changePassword(String newPassword) {
        if (!isValidPassword(newPassword)) {
            throw new InvalidPasswordException("Password needs to be minimum 8 characters long and contain at least one uppercase letter and one digit");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found."));

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully.");
    }


    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        return hasUpperCase && hasDigit;
    }
}