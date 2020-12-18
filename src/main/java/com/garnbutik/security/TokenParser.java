package com.garnbutik.security;

import com.garnbutik.model.User;

public interface TokenParser {

    User parseToken(String token);


}
