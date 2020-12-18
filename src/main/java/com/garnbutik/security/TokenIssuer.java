package com.garnbutik.security;

import com.garnbutik.configuration.Configuration;
import com.garnbutik.model.User;

public interface TokenIssuer {

    String issueToken(User user);
}
