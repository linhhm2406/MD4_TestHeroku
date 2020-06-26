package com.atag.atagbank.formatter;

import com.atag.atagbank.model.Role;
import com.atag.atagbank.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

@Component
public class RoleFormatter implements Formatter<Role> {
    RoleService roleService;
    @Autowired
    public RoleFormatter (RoleService roleService){
        this.roleService = roleService;
    }
    @Override
    public Role parse(String text, Locale locale) throws ParseException {
        Optional<Role> optionalRole = roleService.findById(Long.parseLong(text));
        Role role = optionalRole.get();
        return role;
    }
    @Override
    public String print(Role object, Locale locale) {
        return null;
    }
}
