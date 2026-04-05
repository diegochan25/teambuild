package org.typecrafters.teambuild.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("organizations")
public class OrganizationController {
    @PostMapping
    public void createOrganization() {
        
    }
}
