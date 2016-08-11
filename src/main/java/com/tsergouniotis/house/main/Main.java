package com.tsergouniotis.house.main;

import java.sql.Connection;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;
import org.wildfly.swarm.config.logging.Level;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jpa.JPAFraction;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.undertow.WARArchive;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class Main {

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
		container.fraction(new JPAFraction().inhibitDefaultDatasource().defaultDatasource("jboss/datasources/houseds"));

		LoggingFraction loggingFraction = new LoggingFraction().defaultFormatter().consoleHandler(Level.INFO, "PATTERN")
				.fileHandler("org.jboss", "sql-file.log", Level.FINE, "%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n")
				.rootLogger(Level.INFO, "CONSOLE").logger("wildflyswarm.filelogger",
						l -> l.level(Level.FINE).handler("org.jboss").useParentHandlers(false));

		container.fraction(loggingFraction);

		// container.fraction(TransactionsFraction.createDefaultFraction());

		// perform liquibase migration
		migrate();

		// WarDeployment deployment = new DefaultWarDeployment(container);

		WARArchive deployment = ShrinkWrap.create(WARArchive.class);

		// deployment.addClass(Message.class);

		deployment.addPackages(true, "com.tsergouniotis");

		deployment.addDependency("org.liquibase:liquibase-core");
		deployment.addDependency("org.primefaces:primefaces");
		deployment.addDependency("org.apache.poi:poi");

		deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()),
				"classes/META-INF/persistence.xml");

		deployment.addAsWebResource(new ClassLoaderAsset("index.html", Main.class.getClassLoader()), "index.html");
		deployment.addAsWebResource(new ClassLoaderAsset("index.xhtml", Main.class.getClassLoader()), "index.xhtml");
		deployment.addAsWebResource(new ClassLoaderAsset("creditors.xhtml", Main.class.getClassLoader()),
				"creditors.xhtml");

		
//		deployment.addAsResource(new ClassLoaderAsset("images/ajaxloadingbar.gif", Main.class.getClassLoader()), "META-INF/resources/images/ajaxloadingbar.gif");
		deployment.addAsManifestResource(new ClassLoaderAsset("images/ajaxloadingbar.gif", Main.class.getClassLoader()), "resources/images/ajaxloadingbar.gif");
		
//		deployment.addAsResource(new ClassLoaderAsset("images/excel.png", Main.class.getClassLoader()), "META-INF/resources/images/excel.png");
		deployment.addAsManifestResource(new ClassLoaderAsset("images/excel.png", Main.class.getClassLoader()), "resources/images/excel.png");
		
		//the line below is performing via mvn wildfly:run. Running as fat-jar will fail.
//		deployment.addAsResource("images", "META-INF/resources/images");

		deployment.addAsWebInfResource(new ClassLoaderAsset("WEB-INF/web.xml", Main.class.getClassLoader()), "web.xml");
		deployment.addAsWebInfResource(new ClassLoaderAsset("WEB-INF/template.xhtml", Main.class.getClassLoader()),
				"template.xhtml");

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
