package it.mat.unical.asde.project2_puzzle.configuration;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("it.mat.unical.asde.project2_puzzle.components")
public class DispatcherConfiguration implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
	}

	@Bean
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://asde2018project2.chozlfc5zlbk.us-east-1.rds.amazonaws.com:3306/puzzle2018");
//        jdbc:mysql://localhost:3306/TestDB
		dataSource.setUsername("puzzle2018");
		dataSource.setPassword("ASDE-puzzle2018");
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
		prop.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		prop.put("hibernate.show_sql", true);
		prop.put("hibernate.format_sql", true);
		prop.put("hibernate.hbm2ddl.auto", "create");
		return prop;
	}
}
