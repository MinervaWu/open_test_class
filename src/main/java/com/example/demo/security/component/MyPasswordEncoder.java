package com.example.demo.security.component;

import com.example.demo.util.Base64Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author wuziwei
 * @description
 * @date 2022/12/5
 */
@Component
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        String rawPassword = charSequence.toString();
        return Base64Util.encode(rawPassword);
    }


    @Override
    public boolean matches(CharSequence charSequence, String encodedPassword) {
        String rawPassword = charSequence.toString();
        String newPassword = Base64Util.encode(rawPassword);
        return newPassword.equals(encodedPassword);
    }
}
