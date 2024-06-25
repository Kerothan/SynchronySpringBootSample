package com.kerothan.synchronyapichallenge.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserDataRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}