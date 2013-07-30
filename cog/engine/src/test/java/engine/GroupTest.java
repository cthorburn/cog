package engine;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import org.apache.openejb.api.LocalClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.trabajo.jpa.BeetleJPA;
import com.trabajo.jpa.FlyJPA;
import com.trabajo.jpa.GroupJPA;
import com.trabajo.jpa.GrubJPA;
import com.trabajo.jpa.UserJPA;

@LocalClient
public class GroupTest {

	private static EJBContainer container;
	private static GroupTest instance;
	private boolean injected;

	public GroupTest() {
		instance = this;
	}

	@AfterClass
	public static void xteardown() throws Exception {
		container.close();
	}

	@BeforeClass
	public static void xsetup() throws Exception {
		Properties p = new Properties();

		// p.put("trabajoNonJta", "new://Resource?type=DataSource");
		// p.put("trabajoNonJta.JtaManaged", "false");
		// p.put("trabajoNonJta.username", "colin");
		// p.put("trabajoNonJta.password", "colin");
		// p.put("trabajoNonJta.JdbcDriver", "net.sourceforge.jtds.jdbc.Driver");
		// p.put("trabajoNonJta.JdbcUrl",
		// "jdbc:jtds:sqlserver://localhost:1433/trabajotest");
		// p.put("trabajoNonJta.maxActive", "20");
		// p.put("trabajoNonJta.maxIdle", "10");
		// p.put("trabajoNonJta.validationQuery", "select 1");

		p.put("trabajo", "new://Resource?type=DataSource");
		p.put("trabajo.JtaManaged", "true");
		p.put("trabajo.username", "colin");
		p.put("trabajo.password", "colin");
		p.put("trabajo.JdbcDriver", "net.sourceforge.jtds.jdbc.Driver");
		p.put("trabajo.JdbcUrl", "jdbc:jtds:sqlserver://localhost:1433/trabajo");
		p.put("trabajo.maxActive", "20");
		p.put("trabajo.maxIdle", "10");
		p.put("trabajo.validationQuery", "select 1");
//	p.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(SchemaAction=deleteTableContents,ForeignKeys=true)");
	p.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(SchemaAction=refresh)");
		// "buildSchema(SchemaAction=refresh,ForeignKeys=true)");

		container = EJBContainer.createEJBContainer(p);
	}

	@Before
	public void before() throws Exception {
		if (!instance.injected) {
			container.getContext().bind("inject", instance);
			instance.injected = true;
		}
		if(!nobegin)
			userTransaction.begin();
		nobegin=false;
	}

	@After
	public void after() throws Exception {
		if(!nocommit)
			userTransaction.commit();
		nocommit=false;
	}

	// p.put(Context.INITIAL_CONTEXT_FACTORY,
	// "org.apache.openejb.client.LocalInitialContextFactory");
	// Context context = new InitialContext(p);
	// context.bind("inject", this);

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction userTransaction;
	private boolean nocommit;
	private boolean nobegin;

	@Test
	public void persistQueryDeleteBeetle() throws Exception {
		deleteBeetleIfExists("bob");
		persistBeetle("bob");
		TypedQuery<BeetleJPA> q = em.createQuery("select b from Beetle b where b.username='bob'", BeetleJPA.class);

		BeetleJPA j = q.getSingleResult();

		em.remove(j);

		TypedQuery<BeetleJPA> q2 = em.createQuery("select b from Beetle b where b.username='bob'", BeetleJPA.class);

		try {
			j = q2.getSingleResult();
		} catch (NoResultException e) {
			// OK
		}
	}

	@Test
	public void groups() throws Exception {
		
		UserJPA admin;
		GroupJPA MyGroup;
		TypedQuery<GroupJPA> gQ;
		TypedQuery<UserJPA> uQ;
		
		gQ = em.createQuery("select g from Group g where g.name='MyGroup'", GroupJPA.class);
		uQ = em.createQuery("select u from User u where u.username='colin'", UserJPA.class);

		MyGroup=gQ.getSingleResult();
		admin = uQ.getSingleResult();

		Set<GroupJPA> groups= admin.getGroupSet();

		MyGroup.getUserSet().add(admin);
		admin.getGroupSet().add(MyGroup);
		
		em.persist(MyGroup);
		em.flush();
		em.persist(admin);
		em.flush();

		userTransaction.commit();

		userTransaction.begin();
		gQ = em.createQuery("select g from Group g where g.name='MyGroup'", GroupJPA.class);
		uQ = em.createQuery("select u from User u where u.username='colin'", UserJPA.class);

		MyGroup=gQ.getSingleResult();
		admin = uQ.getSingleResult();

		groups= admin.getGroupSet();
		
		
		
		assertEquals(1, groups.size());
		
	}

	@Test
	public void addGrubs() throws Exception {
		ensureBeetle("brian");
		GrubJPA graham;
		GrubJPA george;
		BeetleJPA brian; 
		TypedQuery<GrubJPA> gQ;
		TypedQuery<BeetleJPA> bQ;
		TypedQuery<FlyJPA> fQ;
		
		bQ = em.createQuery("select b from Beetle b where b.username='brian'", BeetleJPA.class);

		brian = bQ.getSingleResult();

		Set<GrubJPA> grubs = brian.getGrubSet();

		graham = new GrubJPA();
		graham.setName("graham");
		grubs.add(graham);
		graham.setDad(brian);

		george = new GrubJPA();
		george.setName("george");
		grubs.add(george);
		george.setDad(brian);

		em.persist(george);
		em.persist(graham);
		em.persist(brian);
		em.flush();

		userTransaction.commit();

		userTransaction.begin();
		gQ = em.createQuery("select g from Grub g where g.name='graham'", GrubJPA.class);
		graham= gQ.getSingleResult();
		assertEquals("brian", graham.getDad().getUsername());
		
		bQ = em.createQuery("select b from Beetle b where b.username='brian'", BeetleJPA.class);
		brian = bQ.getSingleResult();
		assertEquals(2, brian.getGrubs().size());
		
		
		FlyJPA fiona = new FlyJPA();
		fiona.setName("fiona");

		FlyJPA fred = new FlyJPA();
		fred.setName("fred");
		em.persist(fiona);
		em.persist(fred);
		userTransaction.commit();

		userTransaction.begin();
		gQ = em.createQuery("select g from Grub g where g.name='graham'", GrubJPA.class);
		graham= gQ.getSingleResult();
		Set<FlyJPA> flies = graham.getFlySet();

		flies.addAll(em.createQuery("select f from Fly f", FlyJPA.class).getResultList());

		userTransaction.commit();
		userTransaction.begin();

		gQ = em.createQuery("select g from Grub g where g.name='graham'", GrubJPA.class);
		graham=gQ.getSingleResult();

		assertEquals(2, graham.getFlys().size());

		fQ = em.createQuery("select f from Fly f where f.name='fiona'", FlyJPA.class);
		fiona=fQ.getSingleResult();

		assertEquals(1, fiona.getGrubs().size());
	}

	
	@Test
	public void uniqueBeetle() throws Exception {
		deleteBeetleIfExists("bob");
		persistBeetle("bob");
		try {
			persistBeetle("bob");
		} catch (EntityExistsException e) {
			// OK
			e.printStackTrace();
			nocommit=true;
			nobegin=true;
		}
	}

	public void persistBeetle(String username) throws Exception {
		BeetleJPA j = new BeetleJPA();
		j.setUsername(username);
		em.persist(j);
		em.flush();
	}
	public void ensureBeetle(String username) throws Exception {
		BeetleJPA j = new BeetleJPA();
		j.setUsername(username);
		try{
			em.persist(j);
			em.flush();
		}catch(EntityExistsException e){
		}
	}
	private void deleteBeetleIfExists(String username) {
		BeetleJPA j = null;
		try {
			TypedQuery<BeetleJPA> q = em.createQuery("select b from Beetle b where b.username=?1", BeetleJPA.class);
			q.setParameter(1, username);
			j = q.getSingleResult();
			em.remove(j);
			em.flush();
		} catch (NoResultException e) {
			return;
		}
	}

	@Test
	public void persistsGroup() throws Exception {
			GroupJPA j = new GroupJPA();

			j.setName("MyGroup");
			j.setCategory("com.me.groups");
			j.setDescription("A Group");
			em.persist(j);
			em.flush();
	}
	/*
	 * @SuppressWarnings("serial")
	 * 
	 * @Test public void persistsUser() throws Exception {
	 * userTransaction.begin(); try { UserJPA j = new UserJPA(); final RoleJPA
	 * role = new RoleJPA(); role.setCategory("test.category");
	 * role.setDescription("desc"); role.setGroups(new HashSet<GroupJPA>());
	 * role.setUsers(new HashSet<UserJPA>()); role.setRolePermissions(new
	 * HashSet<RolePermissionJPA>()); role.setTasks(new HashSet<NodeJPA>());
	 * em.persist(role); em.flush();
	 * 
	 * j.setCreatedOn(new GregorianCalendar()); j.setFullName("Colin Thorburn");
	 * j.setLangPref("EN"); j.setLocale("EN"); j.setLoginRole(role);
	 * j.setPassword("secret"); j.setPrimaryEmail("colin@here.com");
	 * j.setRoles(new HashSet<RoleJPA>() { { add(role); } }); j.setTitle("Mr");
	 * j.setUsername("colin");
	 * 
	 * em.persist(j); em.flush(); } finally { userTransaction.commit();
	 * container.close(); } }
	 */
}
