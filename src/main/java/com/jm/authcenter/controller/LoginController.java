package com.jm.authcenter.controller;

import com.jm.authcenter.service.SysUserService;
import com.jm.authcenter.utils.R;
import com.jm.authcenter.utils.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by MJL on 2018/1/27.
 */
@Controller
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    @ResponseBody
    @PostMapping("/sys/login")
    public R login(@RequestParam("username") String username, @RequestParam("password") String password , Boolean rememberMe) {
        try {
            Subject subject = ShiroUtils.getSubject();
            //sha256加密
            password = new Sha256Hash(password).toHex();

            rememberMe = rememberMe == null ? false : rememberMe;

            UsernamePasswordToken token = new UsernamePasswordToken(username, password , rememberMe);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error(e.getMessage());
        } catch (LockedAccountException e) {
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }

        return R.ok();
    }

    public static void main(String[] args){
        String s = new Sha256Hash("admin").toHex();
        System.out.println(s);
    }

}
