package com.ryan.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ryan.dto.DepartmentDTO;
import com.ryan.dto.RoleDTO;

public class SecurityUser implements UserDetails{
	private static final long serialVersionUID = -7956808578756344578L;
	
	private Long id;
	private String username;
	private String email;
	private Integer sex;
	private Date createTime;
	private Date updateTime;
	private String password;
	private String avator;

	private boolean enabled = true;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;

	private DepartmentDTO departmentDTO;
	
	private Set<RoleDTO> roleDTOs;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Set<RoleDTO> roleDTOs = this.getRoleDTOs();
		if (roleDTOs != null) {
			for (RoleDTO roleDTO : roleDTOs) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleDTO.getName());
				authorities.add(authority);
			}
		}
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Set<RoleDTO> getRoleDTOs() {
		return roleDTOs;
	}

	public void setRoleDTOs(Set<RoleDTO> roleDTOs) {
		this.roleDTOs = roleDTOs;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvator() {
		return avator;
	}

	public void setAvator(String avator) {
		this.avator = avator;
	}

	public DepartmentDTO getDepartmentDTO() {
		return departmentDTO;
	}

	public void setDepartmentDTO(DepartmentDTO departmentDTO) {
		this.departmentDTO = departmentDTO;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof SecurityUser ? this.username.equals(((SecurityUser) o).username) : false;
	}

	@Override
	public int hashCode() {
		return this.username.hashCode();
	}
}
