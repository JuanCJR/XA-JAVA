package acdemxaMvcprocess.daoservices;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * ServiceLocator class.
 *
 * @author TESTER
 * @since (2011-06-08.15:26:45)
 */
public class ServiceLocator {
	public static DataSource getDataSource() {
		return (DataSource)SpringFramework.getBean("datasource");
	}

	public static DriverManagerDataSource getDriverManagerDataSource() {
		return (DriverManagerDataSource)SpringFramework.getBean("datasource");
	}

	public static TableDaoService<?, ?> getFromApplicationContext(String tableName) {
		String serviceBeanId = tableName + "Service";
		Object obj = SpringFramework.getBean(serviceBeanId);

		if (!(TableDaoService.class.isInstance(obj))) {
			throw new RuntimeException("The bean registered as '" +
				serviceBeanId + "'" +
				" in the application context does not implement interface " +
				serviceBeanId);
		}

		return (TableDaoService<?, ?>)obj;
	}
}
