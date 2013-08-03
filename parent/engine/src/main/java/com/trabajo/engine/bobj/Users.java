package com.trabajo.engine.bobj;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.jpa.RoleJPA;
import com.trabajo.jpa.UserJPA;
import com.trabajo.process.IRole;
import com.trabajo.process.IUser;
import com.trabajo.values.CreateUserSpec;

public class Users {

	public static IUser findByName(EntityManager em, String username) {
		String jpql = "select u from User u where u.username=?1";
		TypedQuery<UserJPA> q = em.createQuery(jpql, UserJPA.class);
		q.setParameter(1, username);
		try {
			return new UserImpl(em, q.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}

	public static IUser createUser(EntityManager em, CreateUserSpec spec) {

		IRole dflt = Roles.findByName(em, "default");

		UserJPA user = new UserJPA();
		user.setUsername(spec.getUserName().getValue());
		user.setRoles(new HashSet<RoleJPA>());
		user.setCreatedOn(new GregorianCalendar());
		user.setTitle(spec.getTitle().getValue());
		user.setLangPref(spec.getLangPref().getValue());
		user.setLoginRole(((RoleImpl) dflt).entity());
		user.setLocale(spec.getLocale().getCountry());
		user.setFullName(spec.getFullName().getValue());
		user.setPassword(spec.getPassword().getValue());
		user.setPrimaryEmail(spec.getEmail().getValue());
		user.setSystemRole("cog_user");

		em.persist(user);
		em.flush();
		
		em.refresh(user);
		user.getRoles().add(((RoleImpl) dflt).entity());
		em.flush();
		return new UserImpl(em, user);
	}

	public static IUser findById(EntityManager em, long id) {
		return toImpl(em, em.find(UserJPA.class, id));
	}

	public static IUser findByUsername(EntityManager em, String username) {
		try {
			TypedQuery<UserJPA> q = em.createQuery("SELECT u FROM User u WHERE u.username=?1", UserJPA.class);
			q.setParameter(1, username);
			return new UserImpl(em, q.getSingleResult());
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	public static List<String> getUserRoleNames(EntityManager em, String username) {
		TypedQuery<String> q = em.createQuery("SELECT r.name from Role r INNER JOIN r.users u WHERE u.username=?1", String.class);
		q.setParameter(1, username);
		List<String> result = q.getResultList();
		return result;
	}

	public static Set<IUser> toImpl(EntityManager em, Set<UserJPA> set) {
		Set<IUser> impls = new HashSet<>();
		for (UserJPA s : set) {
			impls.add(new UserImpl(em, s));
		}
		return impls;
	}

	public static IUser toImpl(EntityManager em, UserJPA s) {
		return new UserImpl(em, s);
	}

	public static Set<UserJPA> toEntity(Set<IUser> impls) {
		Set<UserJPA> result = new HashSet<>(impls.size());
		for (IUser impl : impls) {
			result.add((UserJPA) impl.entity());
		}
		return result;
	}
}
