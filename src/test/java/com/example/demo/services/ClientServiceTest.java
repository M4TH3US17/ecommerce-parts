package com.example.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.entities.Client;
import com.example.demo.entities.Role;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.exceptions.notfound.ClientNotFoundException;
import com.example.demo.services.exceptions.notfound.ProductNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceTest {
	//Client with id 2 was not mocked, that is, it does not exist.
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
	private ClientService service;
	@Mock
	private ClientRepository clientRepository;
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private PasswordEncoder encoderTest;
	
	private Client client;
	private Set<Role> roles;
	private Optional<Client> optionalClient;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this.service);
		startClient();
	}	
	
	@Test
	public void whenFindByIdTheReturnAnClientInstance() throws ClientNotFoundException {
		Mockito.when(clientRepository.findById(anyLong())).thenReturn(optionalClient);
		
	   Client newClient = service.findById(ID_CLIENT);
	   
	   assertEquals(Client.class, newClient.getClass());
	   assertEquals(ID_CLIENT, newClient.getId());
	   assertEquals(EMAIL, newClient.getEmail());
	   assertEquals(client.getPassword(), encoderTest.encode(newClient.getPassword()));
	   assertEquals(roles, newClient.getRoles());
	}
	
	@Test
	public void whenFindByIdThenReturnAnClientNotFoundException() {
		Mockito.when(clientRepository.findById(ID_CLIENT)).thenReturn(optionalClient);
		
		var thrown = assertThrows(ClientNotFoundException.class,
				                  () -> {
				                	  service.findById(ID_CLIENT_INVALID);
				                  },
				                  "Failed: \"whenFindByIdThenReturnAnClientNotFoundException\" test failed.");
		
		assertThat(thrown).hasMessage("Client with id " + ID_CLIENT_INVALID + " not found.");
		verify(clientRepository, Mockito.times(1)).findById(ID_CLIENT_INVALID);
	}

	@Test
	public void whenSavingTheAdminReturnSuccess() {
		Mockito.when(roleRepository.findByName(ROLE_ADMIN)).thenReturn(new Role(ID_ROLE_ADMIN, ROLE_ADMIN));
		Mockito.when(roleRepository.findByName(ROLE_USER)).thenReturn(new Role(ID_ROLE_USER, ROLE_USER));
		Mockito.when(clientRepository.save(Mockito.any())).thenReturn(client);
		
		client.setId(2L);
		Client response = service.saveAdmin(client);
		Iterator<Role> iterator = response.getRoles().iterator();
		
		assertEquals(Client.class, response.getClass());
		assertEquals(2L, response.getId());
		assertEquals(iterator.next().getName(), ROLE_ADMIN);
		assertEquals(iterator.next().getName(), ROLE_USER);
	}
	
	@Test
	public void whenSavingTheUserReturnSuccess() {
		Mockito.when(roleRepository.findByName(ROLE_USER)).thenReturn(new Role(ID_ROLE_USER, ROLE_USER));
		Mockito.when(clientRepository.save(Mockito.any())).thenReturn(client);
		
		client.setId(2L);
		Client response = service.saveClient(client);
		Iterator<Role> iterator = response.getRoles().iterator();
		
		assertEquals(Client.class, response.getClass());
		assertEquals(ROLE_USER, iterator.next().getName());
	}
	
	@Test
	public void whenUpdateTheClientAdminReturnSuccess() throws ProductNotFoundException {
		Mockito.when(clientRepository.existsById(ID_CLIENT)).thenReturn(true);
		Mockito.when(clientRepository.getById(ID_CLIENT)).thenReturn(client);
		Mockito.when(clientRepository.save(Mockito.any())).thenReturn(client);
		
		client.setName("Jubuscleiton");
		Client response = service.update(ID_CLIENT, client);
		Iterator<Role> iterator = response.getRoles().iterator();
		
		assertEquals(ROLE_ADMIN, iterator.next().getName());
		assertEquals(ROLE_USER, iterator.next().getName());
		assertEquals("Jubuscleiton", response.getName());
		verify(clientRepository).existsById(ID_CLIENT);
		verify(clientRepository).save(client);
	}
	
	@Test
	public void whenUpdateTheClientUserReturnSuccess() throws ProductNotFoundException {
		Client c = new Client(2L, "Maria", "maria@gmail.com", "maria123", "(00) 00000-0000");
		Mockito.when(roleRepository.findByName(ROLE_USER)).thenReturn(new Role(ID_ROLE_USER, ROLE_USER));
		Mockito.when(clientRepository.save(Mockito.any())).thenReturn(c);
		Client newClient = service.saveClient(c);
		
		Mockito.when(clientRepository.existsById(newClient.getId())).thenReturn(true);
		Mockito.when(clientRepository.getById(newClient.getId())).thenReturn(newClient);
		Mockito.when(clientRepository.save(Mockito.any())).thenReturn(newClient);
		
		newClient.setName("Jubuscleiton");
		Client response = service.update(newClient.getId(), newClient);
		Iterator<Role> iterator = response.getRoles().iterator();
		
		assertEquals(ROLE_USER, iterator.next().getName());
		var thrown = assertThrows(NoSuchElementException.class, 
				() -> {
					iterator.next();
				});
		assertThat(thrown).hasMessage(thrown.getMessage());
		assertEquals("Jubuscleiton", response.getName());
		verify(clientRepository, Mockito.times(1)).existsById(newClient.getId());
		verify(clientRepository, Mockito.times(2)).save(Mockito.any());
	}
	
	private void startClient() {
		roles = new HashSet<>();
		roles.add(new Role(ID_ROLE_ADMIN, ROLE_ADMIN));
		roles.add(new Role(ID_ROLE_USER, ROLE_USER));
		
		client = new Client(ID_CLIENT, NAME, EMAIL, encoderTest.encode(PASSWORD), CONTACT);
		client.setRoles(roles);
		
		optionalClient = Optional.of(client);
	}
	
	@Bean
	private PasswordEncoder encoderTest() {
		return new BCryptPasswordEncoder();
	}
}
