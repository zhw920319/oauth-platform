package com.taiji.oauthplatform;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class passwordEncoderTest {
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Test
    public  void test1() {
        System.out.println("加密后："+passwordEncoder().encode("secret2"));
    }
}
