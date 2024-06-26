package com.demo.recipeappbackend.service;


import com.demo.recipeappbackend.dtos.ProfileUpdateRequestDTO;
import com.demo.recipeappbackend.dtos.UserRolesUpdateDTO;
import com.demo.recipeappbackend.errors.EmailUsedException;
import com.demo.recipeappbackend.errors.InvalidEmailException;
import com.demo.recipeappbackend.errors.ResourceNotFoundException;
import com.demo.recipeappbackend.errors.UnauthorizedAccessException;
import com.demo.recipeappbackend.models.Recipe;
import com.demo.recipeappbackend.models.User;
import com.demo.recipeappbackend.repositories.UserRepository;
import com.demo.recipeappbackend.repositories.UsersToFavouritesRepository;
import com.demo.recipeappbackend.security.Role;
import com.demo.recipeappbackend.security.UserDetailsImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PrePostAnnotationSecurityMetadataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        return userDetails.getUser();
    }

    public List<User> getAllUsersExceptLoggedInAdmin() {
        User loggedInUser = getLoggedInUser();
        int userId = Integer.parseInt(String.valueOf(loggedInUser.getId()));
        return userRepository.findAllExceptAdmin(userId);
    }

    @Transactional
    public User updateLoggedInUser(ProfileUpdateRequestDTO profileUpdateRequestDTO) {

        User loggedInUser = getLoggedInUser();
        if (profileUpdateRequestDTO.getFirstName() != null) {
            loggedInUser.setFirstName(profileUpdateRequestDTO.getFirstName());
        }
        if (profileUpdateRequestDTO.getLastName() != null) {
            loggedInUser.setLastName(profileUpdateRequestDTO.getLastName());
        }
        if (profileUpdateRequestDTO.getEmail() != null && !profileUpdateRequestDTO.getEmail().equals(loggedInUser.getEmail())) {
            if (userRepository.existsByEmail(profileUpdateRequestDTO.getEmail())) {
                throw new EmailUsedException("Email is already in use.");
            }
            if (!isValidEmail(profileUpdateRequestDTO.getEmail())) {
                throw new InvalidEmailException("Invalid email format.");
            }
            loggedInUser.setEmail(profileUpdateRequestDTO.getEmail());
        }
        userRepository.save(loggedInUser);
        return loggedInUser;
    }

    @Transactional
    public List<User> updateRoles(@RequestBody  List<UserRolesUpdateDTO> usersAndRoles) {
        List<User> updatedUsers = new ArrayList<>();
        for (UserRolesUpdateDTO dto : usersAndRoles) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for ID: " + dto.getUserId()));
            Role newRole = Role.valueOf(dto.getNewRole());
            user.setRole(newRole);
            updatedUsers.add(userRepository.save(user));
        }
        return updatedUsers;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }


}
