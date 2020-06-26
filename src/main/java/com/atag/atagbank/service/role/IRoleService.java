package com.atag.atagbank.service.role;

import com.atag.atagbank.model.Role;

import java.util.Optional;

public interface IRoleService {
    Iterable<Role> findAll();
    Optional<Role> findById(Long id);
    Role findByRole(String role);
}
