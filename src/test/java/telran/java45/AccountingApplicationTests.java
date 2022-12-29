package telran.java45;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import telran.java45.accounting.dao.UserAccountRepository;
import telran.java45.accounting.dto.RolesResponseDto;
import telran.java45.accounting.dto.UserAccountResponseDto;
import telran.java45.accounting.dto.UserRegisterDto;
import telran.java45.accounting.dto.UserUpdateDto;
import telran.java45.accounting.dto.exceptions.UserExistsException;
import telran.java45.accounting.dto.exceptions.UserNotFoundException;
import telran.java45.accounting.service.UserAccountServiceImpl;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
class AccountingApplicationTests {

	final UserAccountServiceImpl userAccountService;

	final UserAccountRepository repository;

	@BeforeAll
	public void setUp(){
		userAccountService.addUser(UserRegisterDto.builder().firstName("name1").lastName("lastname1").login("user1").password("pass1").build());
		userAccountService.addUser(UserRegisterDto.builder().firstName("name2").lastName("lastname2").login("user2").password("pass2").build());
		userAccountService.addUser(UserRegisterDto.builder().firstName("name3").lastName("lastname3").login("user3").password("pass3").build());



	}

	@Test
	void addUserTest() {
		UserRegisterDto user = new UserRegisterDto("user4", "pass4", "name4", "lastname4");
		userAccountService.addUser(user);
		Assertions.assertEquals(UserAccountResponseDto.builder().login("user4").lastName("lastname4").firstName("name4").build(), userAccountService.getUser("user4"));
		Exception exception = assertThrows(UserExistsException.class, () -> userAccountService.addUser(UserRegisterDto.builder().firstName("name4").lastName("lastname4").login("user4").password("pass4").build()));
		String expectedMessage = "User user4 exists";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}
	@Test
	void getUserTest(){
		Assertions.assertEquals(UserAccountResponseDto.builder().firstName("name3").lastName("lastname3").login("user3").build(), userAccountService.getUser("user3"));
		Exception exception = assertThrows(UserNotFoundException.class, () -> userAccountService.getUser("user10"));
		String expectedMessage = "An error message";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}
	@Test
	void removeUserTest(){
		Assertions.assertEquals(UserAccountResponseDto.builder().firstName("name1").lastName("lastname1").login("user1").build(), userAccountService.removeUser("user1"));
		Exception exception = assertThrows(UserNotFoundException.class, () -> userAccountService.removeUser("user1"));
		String expectedMessage = "An error message";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	@Test
	void  editUserTest(){
		UserUpdateDto update = new UserUpdateDto("name111", "lastname");
		Assertions.assertEquals(UserAccountResponseDto.builder().login("user1").lastName("lastname").firstName("name111").build(), userAccountService.editUser("user1", update));
		Exception exception = assertThrows(UserNotFoundException.class, () -> userAccountService.editUser("user11", update));
		String expectedMessage = "An error message";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	@Test
	void changeRolesListTest(){
		Assertions.assertEquals(RolesResponseDto.builder().role("user").login("user1").build(), userAccountService.changeRolesList("user1", "user", true));
		Assertions.assertEquals(RolesResponseDto.builder().login("user1").build(), userAccountService.changeRolesList("user1", "user", false));
		assertThrows(UserNotFoundException.class, () -> userAccountService.changeRolesList("user10","user", true));
	}
	@Test
	void changePasswordTest(){
		userAccountService.changePassword("user2", "pass20");
		Assertions.assertEquals("pass20", repository.findUserAccountByLogin("user2").getPassword());
	}

}
