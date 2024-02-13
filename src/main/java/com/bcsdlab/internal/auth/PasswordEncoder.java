package com.bcsdlab.internal.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.HashData;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import static at.favre.lib.crypto.bcrypt.BCrypt.Version.VERSION_2Y;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class PasswordEncoder {

    @Value("${password.salt}")
    private String passwordSalt;

    @Value("${password.salt-cost}")
    private Integer cost;

    public String encode(String password) {
        HashData hashData = BCrypt.with(VERSION_2Y)
            .hashRaw(cost, passwordSalt.getBytes(UTF_8), password.getBytes(UTF_8));
        return new String(hashData.rawHash);
    }

    public boolean match(String password, String hashedPassword) {
        Result result = BCrypt.verifyer()
            .verify(password.getBytes(UTF_8), cost, passwordSalt.getBytes(UTF_8), hashedPassword.getBytes(UTF_8));
        return result.verified;
    }
}
