package cz.iens.fifosql.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.*;

import cz.iens.fifosql.builder.UserBuilder;
import cz.iens.fifosql.database.EmbeddedH2Database;
import cz.iens.fifosql.model.User;
import cz.iens.fifosql.repository.Repository;
import cz.iens.fifosql.repository.UserJdbcRepository;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserJdbcRepositoryTest {

	Repository repository;
	
	@BeforeAll
	public static void initAll() {
		UserJdbcRepository rep = new UserJdbcRepository(EmbeddedH2Database.INSTANCE);
		rep.drop();
		rep.create();
	}
	
	@BeforeEach 
	void initEach() {
		repository = new UserJdbcRepository(EmbeddedH2Database.INSTANCE);
	}
	
	@Test
	@Order(1)
	void insertUser() {
		User u = new UserBuilder().id(1L).guid("a1").name("Robert").build();
		List<User> before = repository.selectAll();
		assertEquals(0, before.size());
		assertDoesNotThrow(() -> {
			repository.insert(u);
		});
		List<User> after = repository.selectAll();
		assertEquals(1, after.size());
		assertThat(after).containsExactlyInAnyOrder(u);
	}

	@Test
	@Order(2)
	void insertNextUserAndDeleteAll() {
		User u = new UserBuilder().id(2L).guid("a2").name("Martin").build();
		assertDoesNotThrow(() -> {
			repository.insert(u);
		});
		List<User> all = repository.selectAll();
		assertEquals(2, all.size());
		assertThat(all).contains(u);
		repository.deleteAll();
		List<User> allAfterDelete = repository.selectAll();
		assertEquals(0, allAfterDelete.size());
	}

}
