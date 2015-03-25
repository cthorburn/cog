package com.trabajo.jpa;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity(name = "User")
@Table(name = "TUSER")
public class UserJPA implements JPAEntity<UserJPA> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserJPA other = (UserJPA) obj;
		if (getUsername() == null) {
			if (other.getUsername() != null)
				return false;
		} else if (!getUsername().equals(other.getUsername()))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public UserJPA() {
		super();
	}

	private int id;

    @Override
	@Id
	@SequenceGenerator(name = "USER_SEQUENCE_GENERATOR", sequenceName = "USER_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQUENCE_GENERATOR")
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String username;

	@Column(name = "USERNAME", nullable = false, unique = true, length = 64)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private RoleJPA loginRole;

	@OneToOne
	@Column(name = "LOGINROLE", nullable = false, length = 32)
	public RoleJPA getLoginRole() {
		return loginRole;
	}

	public void setLoginRole(RoleJPA loginRole) {
		this.loginRole = loginRole;
	}

	private UserPrefsJPA userPrefs;
	@OneToOne
	@Column(name = "PREFS", nullable = true)
	public UserPrefsJPA getUserPrefs() {
		return userPrefs;
	}

	public void setUserPrefs(UserPrefsJPA userPrefs) {
		this.userPrefs = userPrefs;
	}

	private String password;

	@Column(name = "PASSWORD", nullable = false, length = 64)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private GregorianCalendar createdOn;

	@Column(name = "CREATEDON", nullable = false)
	public GregorianCalendar getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(GregorianCalendar createdOn) {
		this.createdOn = createdOn;
	}

	private String locale;

	@Column(name = "LOCALE", nullable = false, length = 2)
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	private String langPref;

	@Column(name = "LANGPREF", nullable = false, length = 2)
	public String getLangPref() {
		return langPref;
	}

	public void setLangPref(String langPref) {
		this.langPref = langPref;
	}

	private String fullName;

	@Column(name = "FULL_NAME", nullable = false, length = 255)
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	private String title;

	@Column(name = "TITLE", nullable = false, length = 32)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String primaryEmail;

	@Column(name = "PRIMARYEMAIL", nullable = false, length = 255)
	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	private String systemRole;
	@Column(name = "SYSTEM_ROLE", nullable = false, length = 64)
	public String getSystemRole() {
		return systemRole;
	}
	public void setSystemRole(String systemRole) {
		this.systemRole = systemRole;
	}

	
	@Expose(serialize=false, deserialize=false)
	public Set<RoleJPA> roles;
	@ManyToMany(cascade={ CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "USER_ROLES", joinColumns = { @JoinColumn(name = "UR_USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "UR_ROLE_ID", referencedColumnName = "ID") })
	public Set<RoleJPA> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleJPA> roles) {
		this.roles = roles;
	}

	@Expose(serialize=false, deserialize=false)
	public Set<GroupJPA> groups;
	@ManyToMany(mappedBy="users", cascade={ CascadeType.PERSIST, CascadeType.MERGE })
	public Set<GroupJPA> getGroups() {
		return groups;
	}
	public void setGroups(Set<GroupJPA> groups) {
		this.groups = groups;
	}

	public Set<RoleJPA> getRoleSet() {
		Set<RoleJPA> s = getRoles();
		if (s == null) {
			s = new HashSet<RoleJPA>();
			setRoles(s);
		}
		return getRoles();
	}

	public Set<GroupJPA> getGroupSet() {
		Set<GroupJPA> s = getGroups();
		if (s == null) {
			s = new HashSet<GroupJPA>();
			setGroups(s);
		}
		return getGroups();
	}

}
