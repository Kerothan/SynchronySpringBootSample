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

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    List<User> all() {
        return userService.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    User newUserData(@RequestBody User newUserData) {
        return userService.newUser(newUserData);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) { return userService.findUserDataById(id); }

    @GetMapping("/profile")
    User profile(@RequestHeader("Authorization") String authstring) { return userService.findByUsername(authstring); }

    @PutMapping("/users/{id}")
    User replaceUserData(@RequestBody User newUserData, @RequestHeader("Authorization") String authstring) { return userService.updateUser(authstring, newUserData); }

    @DeleteMapping("/users/{id}")
    void deleteUserData(@PathVariable Long id) {
        userService.deleteUserData(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image, @RequestHeader("Authorization") String authstring) {
        return userService.newImage(image, authstring);
    }
}