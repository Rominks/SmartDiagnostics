package com.smrt.smartdiagnostics.Services;

import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Repositories.UserRepository;
import com.smrt.smartdiagnostics.Services.UserService;
import com.sun.source.tree.AssertTree;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
	private UserRepository userRepository;
	private UserService userService;

	@Before
	public void setup() {
		userRepository = mock(UserRepository.class);
		userService = new UserService(userRepository);
	}

	@Test
	public void testCreateUser_Success() {
		// Given
		User user = new User();
		user.setEmail("newuser@example.com");
		user.setPassword("password123");

		when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(false);
		when(userRepository.existsUserByPassword(user.getPassword())).thenReturn(false);

		// When
		userService.createUser(user);

		// Then
		verify(userRepository).existsUserByEmail(user.getEmail());
		verify(userRepository).save(user);
	}

	@Test
	public void testCreateUser_EmailExists() {
		// Given
		User user = new User();
		user.setEmail("newuser@example.com");
		user.setPassword("password123");

		when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(true);

		// When
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.createUser(user));

		// When -> Then
		assertEquals("An account exists with the provided email.", exception.getMessage());
	}

	@Test
	public void testGetUserByEmail_Success() {
		// Given
		String email = "test@example.com";
		User user = new User();
		Optional<User> optional = Optional.of(user);
		when(userRepository.getUserByEmail(email)).thenReturn(optional);

		// When
		Optional<User> result = userService.getUserByEmail(email);

		// Then
		assertTrue(result.isPresent());
		assertEquals(user, result.get());
	}

	@Test
	public void testGetUserByEmail_Fail() {
		// Given
		String email = "test@example.com";
		User user = new User();
		when(userRepository.getUserByEmail(email)).thenReturn(Optional.empty());

		// When
		Optional<User> result = userService.getUserByEmail(email);

		// Then
		assertFalse(result.isPresent());
	}

	@Test
	public void testGetUserByUsername_Success() {
		// Given
		User user = new User();
		user.setUsername("username");
		Optional<User> optional = Optional.of(user);
		when(userRepository.getUserByEmail(user.getUsername())).thenReturn(optional);

		// When
		Optional<User> result = userService.getUserByEmail(user.getUsername());

		// Then
		assertTrue(result.isPresent());
		assertEquals(user, result.get());
	}

	@Test
	public void testGetUserByUsername_Fail() {
		// Given
		User user = new User();
		user.setUsername("username");
		when(userRepository.getUserByEmail(user.getUsername())).thenReturn(Optional.empty());

		// When
		Optional<User> result = userService.getUserByEmail(user.getUsername());

		// Then
		assertFalse(result.isPresent());
	}

	@Test
	public void testUpdateUser_Success() {
		// Given
		User user = new User();

		// When
		userService.updateUser(user);

		// Then
		verify(userRepository).saveAndFlush(user);
	}

	@Test
	public void testUpdateUser_Fail() {
		// Given
		User user = new User();
		when(userRepository.saveAndFlush(user)).thenThrow(DataAccessResourceFailureException.class);

		// When -> Then
		assertThrows(DataAccessResourceFailureException.class, () -> userService.updateUser(user));
	}

	@Test
	public void deleteUser_Success() {
		// Given
		long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

		// When
		userService.deleteUser(userId);

		// Then
		verify(userRepository).findById(userId);
		verify(userRepository).deleteById(userId);
	}

	@Test
	public void deleteUser_Fail() {
		// Given
		long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// When
		userService.deleteUser(userId);

		// Then
		verify(userRepository).findById(userId);
		verify(userRepository, never()).deleteById(userId);
	}

	@Test
	public void testGetUserByCredentials_UserExists() {
		// Given
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword("password");
		user.setUsername("username");
		Optional<User> optionalUser = Optional.of(user);
		when(userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(optionalUser);
		when(userRepository.getUserByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(optionalUser);

		// When
		Optional<User> result = userService.getUserByCredentials(user);

		// Then
		assertEquals(user, result.get());
	}

	@Test
	public void testGetUserByCredentials_UserDoesNotExist() {
		// Given
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword("password");
		user.setUsername("username");
		Optional<User> optionalUser = Optional.of(user);
		when(userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(Optional.empty());
		when(userRepository.getUserByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());

		// When
		Optional<User> result = userService.getUserByCredentials(user);

		// Then
		assertNotEquals(user, result);
	}

	@Test
	public void testUpdateUserCredentials_Successful() {
		// Given
		long userId = 1L;
		User existingUser = new User();
		existingUser.setEmail("old@example.com");
		existingUser.setPassword("oldpassword");
		existingUser.setUsername("oldusername");

		User newInfo = new User();
		newInfo.setEmail("new@example.com");
		newInfo.setPassword("newpassword");
		newInfo.setUsername("newusername");

		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

		// When
		userService.updateCredentials(userId, newInfo);

		// Then
		verify(userRepository).findById(userId);

		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).saveAndFlush(userCaptor.capture());

		User savedUser = userCaptor.getValue();
		assertEquals("new@example.com", savedUser.getEmail());
		assertEquals("newpassword", savedUser.getPassword());
		assertEquals("newusername", savedUser.getUsername());
	}

	@Test
	public void testUpdateUserCredentials_IllegalStateException() {
		// Given
		long userId = 1L;
		User newInfo = new User();

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// When
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.updateCredentials(userId, newInfo));

		// Then
		verify(userRepository).findById(userId);
		assertEquals("No such user found.", exception.getMessage());
		verify(userRepository, never()).saveAndFlush(newInfo);
	}
}
