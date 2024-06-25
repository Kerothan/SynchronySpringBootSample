package com.kerothan.synchronyapichallenge.user;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
class UserDataController {

    private final UserDataService userService;

    UserDataController(UserDataService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    User newUserData(@RequestBody User newUserData) {
        return userService.newUser(newUserData);
    }

    // Single item

    @GetMapping("/profile")
    User profile(@RequestHeader("Authorization") String authstring) { return userService.findByUsername(authstring); }

    @PutMapping("/users/{id}")
    User replaceUserData(@RequestBody User newUserData, @RequestHeader("Authorization") String authstring) { return userService.updateUser(authstring, newUserData); }

    @DeleteMapping("/users")
    void deleteUserData(@RequestHeader("Authorization") String authstring) {
        userService.deleteUserData(authstring);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image, @RequestHeader("Authorization") String authstring) {
        return userService.newImage(image, authstring);
    }
}