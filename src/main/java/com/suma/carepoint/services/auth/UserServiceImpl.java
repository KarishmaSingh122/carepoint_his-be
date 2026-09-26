package com.suma.carepoint.services.auth;

import com.suma.carepoint.entities.auth.User;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.auth.UserRequest;
import com.suma.carepoint.models.auth.UserResponse;
import com.suma.carepoint.models.mapper.UserMapper;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.repositories.auth.UserRepository;
import com.suma.carepoint.repositories.organization.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.suma.carepoint.models.utility.PageResponse.validatePagination;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRequest request) {
        String username = request.getUsername().trim();
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new ConflictException("Username already exists");
        }
        Staff staff = resolveStaff(request.getStaffId());
        if (staff != null && userRepository.existsByStaffStaffId(staff.getStaffId())) {
            throw new ConflictException("Staff already has a user account");
        }

        User user = User.builder()
                .staff(staff)
                .username(username)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .build();
        try {
            User saved = userRepository.save(user);
            log.info("User created successfully. userId={}, staffId={}",
                    saved.getUserId(), staff != null ? staff.getStaffId() : null);
            return userMapper.toResponse(saved);
        } catch (DataIntegrityViolationException exception) {
            log.error("User creation failed due to database constraint. username={}", username, exception);
            throw new ConflictException("Username or staff account already exists");
        }
    }

    @Override
    public UserResponse getById(Long userId) {

        return userMapper.toResponse(findUser(userId));
    }

    @Override
    public PageResponse getAll(String search, Boolean active, int page, int size) {

        validatePagination(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "username"));
        String normalizedSearch = search == null ? null : search.trim();
        Page<User> users;

        if (normalizedSearch != null && !normalizedSearch.isBlank() && active != null) {
            users = userRepository.findByActiveAndUsernameContainingIgnoreCase(active, normalizedSearch, pageable);
        } else if (normalizedSearch != null && !normalizedSearch.isBlank()) {
            users = userRepository.findByUsernameContainingIgnoreCase(normalizedSearch, pageable);
        } else if (active != null) {
            users = userRepository.findByActive(active, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        List<UserResponse> content =
                users.getContent()
                        .stream()
                        .map(userMapper::toResponse)
                        .toList();

        return PageResponse.builder()
                .content(content)
                .page(users.getNumber())
                .size(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .first(users.isFirst())
                .last(users.isLast())
                .build();
    }

    @Override
    public UserResponse getByStaffId(Long staffId) {
        User user = userRepository.findByStaffStaffId(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for staff"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long userId, UserRequest request) {

        User user = findUser(userId);
        String username = request.getUsername().trim();
        userRepository.findByUsernameIgnoreCase(username
        ).ifPresent(existingUser -> {
            if (!existingUser.getUserId().equals(userId)) {
                throw new ConflictException("Username already exists");
            }
        });
        Staff staff = resolveStaff(request.getStaffId());

        if (staff != null) {
            userRepository.findByStaffStaffId(staff.getStaffId())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getUserId().equals(userId)) {
                            throw new ConflictException("Staff already has a user account");
                        }
                    });
        }
        user.setStaff(staff);
        user.setUsername(username);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        try {
            User updated = userRepository.save(user);
            log.info("User updated successfully. userId={}, staffId={}",
                    updated.getUserId(), staff != null ? staff.getStaffId() : null);
            return userMapper.toResponse(updated);
        } catch (DataIntegrityViolationException exception) {
            log.error("User update failed due to database constraint. userId={}", userId, exception);
            throw new ConflictException("Username or staff account already exists");
        }
    }

    @Override
    public UserResponse updateStatus(Long userId, boolean active) {
        User user = findUser(userId);
        user.setActive(active);
        User updated = userRepository.save(user);
        log.info("User status updated. userId={}, active={}", userId, active);
        return userMapper.toResponse(updated);
    }

    @Override
    public void delete(Long userId) {
        User user = findUser(userId);
        try {
            userRepository.delete(user);
            log.info("User deleted successfully. userId={}", userId);
        } catch (DataIntegrityViolationException exception) {
            log.error("User deletion failed due to database constraint. userId={}", userId, exception);
            throw new ConflictException("User cannot be deleted because it is referenced by other records");
        }
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Staff resolveStaff(Long staffId) {

        if (staffId == null) {
            return null;
        }
        return staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
    }
}
