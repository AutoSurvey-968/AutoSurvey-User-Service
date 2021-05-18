package com.revature.autosurvey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.SessionFactoryFactoryBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

@Configuration
@EnableCassandraRepositories(basePackages = { "com.group3.data" })
public class CassandraConfig {

	@Bean
	public CqlSessionFactoryBean session() {
		CqlSessionFactoryBean factory = new CqlSessionFactoryBean();
		DriverConfigLoader loader = DriverConfigLoader.fromClasspath("application.conf");
		factory.setSessionBuilderConfigurer(builder -> builder.withConfigLoader(loader).withKeyspace("AutoSurvey"));
		factory.setKeyspaceName("AutoSurvey");
		return factory;
	}

	@Bean
	public SessionFactoryFactoryBean sessionFactory(CqlSession session, CassandraConverter converter) {
		SessionFactoryFactoryBean sessionFactory = new SessionFactoryFactoryBean();
		((MappingCassandraConverter) converter).setUserTypeResolver(new SimpleUserTypeResolver(session));
		sessionFactory.setSession(session);
		sessionFactory.setConverter(converter);
		sessionFactory.setSchemaAction(SchemaAction.CREATE_IF_NOT_EXISTS);

		return sessionFactory;
	}

	@Bean
	public CassandraMappingContext mappingContext(CqlSession cqlSession) {
		return new CassandraMappingContext();
	}

	@Bean
	public CassandraConverter converter(CassandraMappingContext mappingContext) {
		return new MappingCassandraConverter(mappingContext);
	}

	@Bean
	public CassandraOperations cassandraTemplate(SessionFactory sessionFactory, CassandraConverter converter) {
		return new CassandraTemplate(sessionFactory, converter);
	}
}

