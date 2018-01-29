package com.jm.authcenter.service;



import com.jm.authcenter.dao.SysUserDao;
import com.jm.authcenter.entity.SysUser;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 系统用户
 * 
 */
@Service("sysUserService")
public class SysUserService{
	@Autowired
	private SysUserDao sysUserDao;
	//@Autowired
	//private SysUserRoleService sysUserRoleService;

	
	public List<String> queryAllPerms(Long userId) {
		return sysUserDao.queryAllPerms(userId);
	}

	
	public List<Long> queryAllMenuId(Long userId) {
		return sysUserDao.queryAllMenuId(userId);
	}

	
	public SysUser queryByUserName(String username) {
		return sysUserDao.queryByUserName(username);
	}
	
	
	public SysUser queryObject(Long userId) {
		return sysUserDao.queryObject(userId);
	}

	
	public List<SysUser> queryList(Map<String, Object> map){
		return sysUserDao.queryList(map);
	}
	
	
	public int queryTotal(Map<String, Object> map) {
		return sysUserDao.queryTotal(map);
	}

	
	@Transactional
	public void save(SysUser user) {
		user.setCreateTime(new Date());
		//sha256加密
		user.setPassword(new Sha256Hash(user.getPassword()).toHex());
		sysUserDao.save(user);
		
		//保存用户与角色关系
		//sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	
	@Transactional
	public void update(SysUser user) {
		/*if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword()).toHex());
		}
		sysUserDao.update(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());*/
	}

	
	@Transactional
	public void deleteBatch(Long[] userId) {
		sysUserDao.deleteBatch(userId);
	}

	
	public int updatePassword(Long userId, String password, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return sysUserDao.updatePassword(map);
	}
}
