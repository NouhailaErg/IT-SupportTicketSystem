package com.nouhaila.ticketsystem.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
	EMPLOYEE, IT_SUPPORT;

	@Override
    public String getAuthority() {
        return name(); // Converts role name to Spring Security format
    }
}

