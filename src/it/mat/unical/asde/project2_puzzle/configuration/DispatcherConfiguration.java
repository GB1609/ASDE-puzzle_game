package it.mat.unical.asde.project2_puzzle.configuration;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan("it.mat.unical.asde.project2_puzzle.components")
public class DispatcherConfiguration implements WebMvcConfigurer {
	final static String USERNAME = "YOUR USERNAME";
	final static String PASSWORD = "YOUR PASSWORD";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
	}

	@Bean
	public DataSource getDataSource() {
		final ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
		} catch (final PropertyVetoException e) {
			e.printStackTrace();
		}
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/puzzle_game");
		dataSource.setUser(USERNAME);
		dataSource.setPassword(PASSWORD);
		return dataSource;
	}

	@Bean
	public SessionFactory sessionFactory() {
		LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
		lsfb.setDataSource(getDataSource());
		lsfb.setHibernateProperties(getHibernateProperties());
		lsfb.setPackagesToScan("it.mat.unical.asde.project2_puzzle.model");
		try {
			lsfb.afterPropertiesSet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lsfb.getObject();
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		return new InternalResourceViewResolver("WEB-INF/views/", ".jsp");
	}

	private Properties getHibernateProperties() {
		Properties prop = new Properties();
		prop.put("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
		prop.put("hibernate.show_sql", true);
		prop.put("hibernate.format_sql", true);
		prop.put("hibernate.hbm2ddl.auto", "update");
		return prop;
	}
}
