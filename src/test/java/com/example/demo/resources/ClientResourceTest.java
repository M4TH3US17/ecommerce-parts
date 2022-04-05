package com.example.demo.resources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.configurations.jwt.JwtService;
import com.example.demo.configurations.jwt.dto.CredentialsDTO;
import com.example.demo.configurations.jwt.dto.TokenDTO;
import com.example.demo.entities.Client;
import com.example.demo.entities.Role;
import com.example.demo.services.ClientService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientResourceTest {
    private final static Long ID_CLIENT_INVALID = 2L;

	private final static Long ID_CLIENT = 1L;
	private final static String NAME = "Matheus";
	private final static String EMAIL = "matheus123@gmail.com";
	private final static String PASSWORD = "matheus123";
	private final static String CONTACT = "(00) 00000-0000";
	
	private final static Long  ID_ROLE_ADMIN = 1L;
	private final static String  ROLE_ADMIN = "ROLE_ADMIN";
	
	private final static Long  ID_ROLE_USER = 2L;
	private final static String ROLE_USER = "ROLE_USER";
	
	@InjectMocks
	private ClientResource resource;
	@Mock
	private ClientService service;
	@Mock
	private JwtService jwt;
	@Mock
	private PasswordEncoder encoderTest;
	
	private Client client;
	private Set<Role> roles;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this.resource);
		startClient();
	}

	@Test
	public void whenFindByIdThenReturnSuccess() throws Exception {
		when(service.findById(anyLong())).thenReturn(client);
		
		ResponseEntity<Client> response = resource.findById(ID_CLIENT);
		Iterator<Role> role = response.getBody().getRoles().iterator();
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseEntity.class, response.getClass());
		
		assertEquals(ID_CLIENT, response.getBody().getId());
		assertEquals(EMAIL, response.getBody().getEmail());
		assertNull(response.getBody().getPassword());
		assertEquals("ROLE_ADMIN", role.next().getName());
		assertEquals("ROLE_USER", role.next().getName());
	}
	
	@Test
	public void whenSavingClientThenReturnCreated() {
		Client costumer = new Client(2L, "James", "james@gmail.com", encoderTest.encode("james123"), "(00) 00000-0000");
		costumer.addRole(new Role(ID_ROLE_USER, ROLE_USER));
		when(service.saveClient(any())).thenReturn(costumer);
		
		costumer.setId(2L);
		ResponseEntity<Client> response = resource.saveClient(costumer);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		
		assertEquals(2L, response.getBody().getId());
		assertEquals("james@gmail.com", response.getBody().getEmail());
		
		Iterator<Role> iterator = response.getBody().getRoles().iterator();
		assertEquals(ROLE_USER, iterator.next().getName());
	}
	
	@Test
	public void whenSavingAdminThenReturnCreated() {
		when(service.saveAdmin(any())).thenReturn(client);
		
		ResponseEntity<Client> response = resource.saveAdmin(client);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertNotNull(response.getBody());
		assertNotNull(response);
		
		assertEquals(client.getId(), response.getBody().getId());
		assertEquals(client.getEmail(), response.getBody().getEmail());
		assertNull(response.getBody().getPassword());
		
		Iterator<Role> iterator = response.getBody().getRoles().iterator();
	    assertEquals(ROLE_ADMIN, iterator.next().getName());
		assertEquals(ROLE_USER, iterator.next().getName());
	}
	
	@Test
	public void whenAuthenticateThenReturnToken() throws Exception {
		when(service.authenticate(any())).thenReturn(client);
		when(jwt.generationToken(any())).thenReturn(jwt.generationToken(client));
		
		ResponseEntity<TokenDTO> response = resource.authenticate(
				new CredentialsDTO(client.getEmail(), client.getPassword()));
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getName());
		assertNull(response.getBody().getToken());
	}
	
	private void startClient() {
		roles = new HashSet<>();
		roles.add(new Role(ID_ROLE_ADMIN, ROLE_ADMIN));
		roles.add(new Role(ID_ROLE_USER, ROLE_USER));
		
		client = new Client(ID_CLIENT, NAME, EMAIL, encoderTest.encode(PASSWORD), CONTACT);
		client.setRoles(roles);
	}	
}
