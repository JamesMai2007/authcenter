package com.jm.authcenter.shiro;

import com.jm.authcenter.entity.SysUser;
import com.jm.authcenter.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2018/1/27.
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;


    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser sysUser = (SysUser)principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        if("admin".equals(sysUser.getUsername())){
            info.addRole("admin");
            info.addStringPermission("sys");
        }
        else{
            info.addRole("user");
            info.addStringPermission("usr");
        }


        return info;
    }

    //认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[])authenticationToken.getCredentials());

        SysUser user = sysUserService.queryByUserName(username);

        if(user == null){
            throw new UnknownAccountException("账号或密码不正确");
        }

        if(!password.equals(user.getPassword())){
            throw new IncorrectCredentialsException("账号或密码不正确");
        }

        if(user.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user , password , getName());

        return info;
    }
}
