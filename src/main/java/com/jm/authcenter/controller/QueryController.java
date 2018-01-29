package com.jm.authcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.jm.authcenter.service.SysUserService;
import com.jm.authcenter.utils.R;
import com.jm.authcenter.utils.ShiroUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by MJL on 2018/1/27.
 */
@Controller
public class QueryController {

    @Autowired
    private SysUserService sysUserService;

    @ResponseBody
    @GetMapping("/query")
    public R query() {
        JSONObject json = new JSONObject();
        try {
            Subject subject = ShiroUtils.getSubject();
            json.put("isLogin",subject.isAuthenticated());
            json.put("user",subject.getPrincipal());
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error(e.getMessage());
        } catch (LockedAccountException e) {
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }

        return R.ok(json);
    }

}
