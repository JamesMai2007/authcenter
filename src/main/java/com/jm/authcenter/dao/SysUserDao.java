package com.jm.authcenter.dao;

import com.jm.authcenter.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/27.
 */
public interface SysUserDao extends BaseDao<SysUser> {

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 根据用户名，查询系统用户
     */
    SysUser queryByUserName(String username);

    /**
     * 修改密码
     */
    int updatePassword(Map<String, Object> map);
}
