package com.atag.atagbank.service.role;

import com.atag.atagbank.model.Role;
import com.atag.atagbank.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Component
public class RoleService implements IRoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Iterable<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
         return roleRepository.findById(id);
    }

    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }

}
