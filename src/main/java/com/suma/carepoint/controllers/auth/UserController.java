package com.suma.carepoint.controllers.auth;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.auth.UserRequest;
import com.suma.carepoint.models.auth.UserResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.services.auth.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.User.BASE)
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping(ApiConstant.User.CREATE)
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody UserRequest request) {

        UserResponse response = userService.create(request);
        ApiResponse apiResponse = new ApiResponse(1, "", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping(ApiConstant.User.GET_BY_ID)
    public ResponseEntity<ApiResponse> getById(@PathVariable @Min(1) Long userId) {

        UserResponse response = userService.getById(userId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.User.GET_ALL)
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        PageResponse response = userService.getAll(search, active, page, size);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.User.GET_BY_STAFF)
    public ResponseEntity<ApiResponse> getByStaffId(@PathVariable @Min(1) Long staffId) {
        UserResponse response = userService.getByStaffId(staffId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @PutMapping(ApiConstant.User.UPDATE)
    public ResponseEntity<ApiResponse> update(
            @PathVariable @Min(1) Long userId,
            @Valid @RequestBody UserRequest request) {

        UserResponse response = userService.update(userId, request);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @PatchMapping(ApiConstant.User.STATUS)
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable @Min(1) Long userId,
            @RequestParam boolean status) {

        UserResponse response = userService.updateStatus(userId, status);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @DeleteMapping(ApiConstant.User.DELETE)
    public ResponseEntity<ApiResponse> delete(@PathVariable @Min(1) Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok(
                new ApiResponse(1, "User deleted successfully", null));
    }
}
