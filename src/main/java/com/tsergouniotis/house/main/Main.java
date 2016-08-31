package com.tsergouniotis.house.main;

import java.sql.Connection;
import java.util.HashMap;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.bean.validation.BeanValidationFraction;
import org.wildfly.swarm.config.logging.Level;
import org.wildfly.swarm.config.security.Flag;
import org.wildfly.swarm.config.security.SecurityDomain;
import org.wildfly.swarm.config.security.security_domain.ClassicAuthentication;
import org.wildfly.swarm.config.security.security_domain.authentication.LoginModule;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jpa.JPAFraction;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.security.SecurityFraction;
import org.wildfly.swarm.undertow.WARArchive;
import org.wildfly.swarm.undertow.descriptors.WebXmlAsset;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class Main {

	private static final String ROLES_QUERY = "SELECT r.role, 'Roles' FROM roles r inner join users_roles ur on ur.role_sid=r.database_id inner join users u on ur.user_sid=u.database_id WHERE u.username=?";

	private static final String PRINCIPALS_QUERY = "SELECT password FROM users WHERE username=?";

	private static final String SECURITY_DOMAIN = "my-domain";

	private static final String DATASOURCE_NAME = "jboss/datasources/houseds";

	public static void main(String[] args) throws Exception {

		Container container = new Container();

		// Configure the Datasources subsystem with a driver
		// and a datasource
		container.fraction(new DatasourcesFraction().jdbcDriver("postgresql", (d) -> {
			d.driverClassName("org.postgresql.Driver");
			d.xaDatasourceClass("org.postgresql.ds.PGSimpleDataSource");
			d.driverModuleName("org.postgresql");
		}).dataSource("houseds", (ds) -> {
			ds.driverName("postgresql");
			ds.connectionUrl(DBUtils.url());
			ds.userName(DBUtils.user());
			ds.password(DBUtils.pass());
		}));

		// Prevent JPA Fraction from installing it's default datasource fraction
		container.fraction(new JPAFraction().inhibitDefaultDatasource().defaultDatasource(DATASOURCE_NAME));

		LoggingFraction loggingFraction = new LoggingFraction().defaultFormatter().consoleHandler(Level.INFO, "PATTERN")
				.fileHandler("JBOSS", "jboss.log", Level.FINE, "%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n")
				.fileHandler("HOUSE", "house.log", Level.FINE, "%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n")
				.rootLogger(Level.INFO, LoggingFraction.CONSOLE)
				.logger("org.jboss", l -> l.level(Level.FINE).handler("JBOSS").useParentHandlers(false))
				.logger("com.tsergouniotis", l -> l.level(Level.FINE).handler("HOUSE").useParentHandlers(false));

		container.fraction(loggingFraction);

		// container.fraction(new BeanValidationFraction());
		// container.fraction(TransactionsFraction.createDefaultFraction());

		container.fraction(SecurityFraction.defaultSecurityFraction()
				.securityDomain(new SecurityDomain(SECURITY_DOMAIN)
						.classicAuthentication(new ClassicAuthentication<>().loginModule(new LoginModule("Database")
								.code("Database").flag(Flag.REQUIRED).moduleOptions(new HashMap<Object, Object>() {
									{
										put("dsJndiName", DATASOURCE_NAME);
										put("principalsQuery", PRINCIPALS_QUERY);
										put("rolesQuery", ROLES_QUERY);
									}
								})))));

		// WarDeployment deployment = new DefaultWarDeployment(container);

		WARArchive deployment = ShrinkWrap.create(WARArchive.class);

		deployment.addPackages(true, "com.tsergouniotis");

		deployment.addDependency("org.liquibase:liquibase-core");
		deployment.addDependency("org.primefaces:primefaces");
		deployment.addDependency("org.apache.poi:poi");

		deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()),
				"classes/META-INF/persistence.xml");
		deployment.addAsWebInfResource(new ClassLoaderAsset("WEB-INF/web.xml", Main.class.getClassLoader()), "web.xml");
		deployment.addAsWebInfResource(new ClassLoaderAsset("WEB-INF/template.xhtml", Main.class.getClassLoader()),
				"template.xhtml");

		deployment.addAsWebResource(new ClassLoaderAsset("index.html", Main.class.getClassLoader()), "index.html");
		deployment.addAsWebResource(new ClassLoaderAsset("index.xhtml", Main.class.getClassLoader()), "index.xhtml");
		deployment.addAsWebResource(new ClassLoaderAsset("creditors.xhtml", Main.class.getClassLoader()),
				"creditors.xhtml");
		deployment.addAsWebResource(new ClassLoaderAsset("graphs.xhtml", Main.class.getClassLoader()), "graphs.xhtml");

		deployment.addAsManifestResource(new ClassLoaderAsset("images/ajaxloadingbar.gif", Main.class.getClassLoader()),
				"resources/images/ajaxloadingbar.gif");
		deployment.addAsManifestResource(new ClassLoaderAsset("images/excel.png", Main.class.getClassLoader()),
				"resources/images/excel.png");

/*		deployment.addAsManifestResource(new ClassLoaderAsset("META-INF/validation.xml", Main.class.getClassLoader()),
				"validation.xml");*/

		deployment.addAsResource(new ClassLoaderAsset("ValidationMessages.properties", Main.class.getClassLoader()),
				"ValidationMessages.properties");

		WebXmlAsset webXmlAsset = deployment.findWebXmlAsset();
		webXmlAsset.setLoginConfig("BASIC", "my-realm");
		webXmlAsset.protect("/*").withMethod("GET", "POST").withRole("ADMIN");
		deployment.setSecurityDomain(SECURITY_DOMAIN);

		migrate();

		container.start().deploy(deployment);

	}

	private static void migrate() throws Exception {
		try (Connection c = DBUtils.getConnection()) {
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
			Liquibase liquibase = new Liquibase("db/ts/house/db.changelog-master.xml",
					new ClassLoaderResourceAccessor(), database);
			liquibase.update(new Contexts());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
