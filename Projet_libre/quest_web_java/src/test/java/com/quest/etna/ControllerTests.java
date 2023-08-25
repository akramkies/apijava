// package com.quest.etna;

// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.List;

// import org.hamcrest.Matchers;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.jdbc.Sql;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.jayway.jsonpath.JsonPath;
// import com.quest.etna.model.Address;
// import com.quest.etna.model.User;
// import com.quest.etna.model.UserDetails;
// import com.quest.etna.model.UserRole;
// import com.quest.etna.service.UserService;

// @ExtendWith(SpringExtension.class)
// @SpringBootTest
// @AutoConfigureMockMvc
// public class ControllerTests {

// 	@Autowired
// 	protected MockMvc mockMvc;

// 	@Autowired
// 	private ObjectMapper objectMapper;

// 	@MockBean
// 	private UserService userService;




// 	@Test
// 	@Sql("./delete.sql")
// 	protected void testAuthenticate() throws Exception {

// 		User user = new User();
// 		user.setId(1);
// 		user.setUsername("test");
// 		user.setPassword("test");
// 		user.setRole("ROLE_USER");

// 		//UserDetails details = new UserDetails(user.getUsername(), UserRole.valueOf(user.getRole()));


// 		this.mockMvc
// 			.perform(
// 					MockMvcRequestBuilders.post("/register")
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.content(objectMapper.writeValueAsString(user)))
// 			.andDo(print())
// 			.andExpect(status().isCreated());



// 		this.mockMvc
// 				.perform(
// 						MockMvcRequestBuilders.post("/register")
// 						.contentType(MediaType.APPLICATION_JSON)
// 						.content(objectMapper.writeValueAsString(user)))
// 				.andDo(print())
// 				.andExpect(status().isConflict());


// 		MvcResult res2 = this.mockMvc
// 				.perform(
// 						MockMvcRequestBuilders.post("/authenticate")
// 						.contentType(MediaType.APPLICATION_JSON)
// 						.content(objectMapper.writeValueAsString(user)))
// 				.andDo(print())
// 				.andExpect(status().isOk())
// 				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
// 				//.andExpect(jsonPath("$.token", Matchers.notNullValue()))
// 				.andReturn();

// 		String token = JsonPath.read(res2.getResponse().getContentAsString(), "$.token");


// 		this.mockMvc
// 				.perform(
// 						MockMvcRequestBuilders.get("/me")
// 						.contentType(MediaType.APPLICATION_JSON)
// 						.content(objectMapper.writeValueAsString(user))
// 						.header("authorization", "Bearer " + token))
// 				.andDo(print())
// 				.andExpect(status().isOk());

// 	}

// 	@Test
// 	@Sql("./data.sql")
// 	public void testUser() throws Exception {

// 		User u1 = new User();
// 		u1.setUsername("test");
// 		u1.setPassword("test");
// 		u1.setRole(UserRole.ROLE_USER.toString());
// 		User u2 = new User();
// 		u2.setUsername("Paul");
// 		u2.setPassword("pass");
// 		u2.setRole(UserRole.ROLE_USER.toString());

// 		UserDetails d1 = new UserDetails(u1.getUsername(), UserRole.valueOf(u1.getRole()));
// 		UserDetails d2 = new UserDetails(u2.getUsername(), UserRole.valueOf(u2.getRole()));

// 		MvcResult res = this.mockMvc
// 				.perform(
// 						MockMvcRequestBuilders.post("/authenticate")
// 						.contentType(MediaType.APPLICATION_JSON)
// 						.content(objectMapper.writeValueAsString(u1)))
// 				.andDo(print())
// 				.andExpect(status().isOk())
// 				.andReturn();

// 		String token = JsonPath.read(res.getResponse().getContentAsString(), "$.token");


// 		Mockito.when(userService.getAllUsers()).thenReturn(List.of(d1, d2));
// 		 this.mockMvc
// 				.perform(
// 						MockMvcRequestBuilders.get("/user")
// 						.contentType(MediaType.APPLICATION_JSON))
// 				.andDo(print())
// 				.andExpect(status().isUnauthorized());
// 				//.andExpect(jsonPath("$.token", Matchers.notNullValue()))

// 		  this.mockMvc
// 			.perform(
// 					MockMvcRequestBuilders.get("/user")
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.header("authorization", "Bearer " + token))
// 			.andDo(print())
// 			.andExpect(status().isOk())
// 			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
// 		 	.andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)));



// 		 this.mockMvc
// 			.perform(
// 					MockMvcRequestBuilders.delete("/user/{id}", "3")
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.header("authorization", "Bearer " + token))
// 			.andDo(print())
// 			.andExpect(status().isForbidden());

// 		 this.mockMvc
// 			.perform(
// 					MockMvcRequestBuilders.delete("/user/{id}", "3")
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.header("authorization", "Bearer " + token))
// 			.andDo(print())
// 			.andExpect(status().isForbidden());
// 	}

// 	@Test
// 	@Sql("./address-user.sql")
// 	public void testAddress() throws Exception {

// 		User u1 = new User();
// 		u1.setUsername("test");
// 		u1.setPassword("test");
// 		u1.setRole(UserRole.ROLE_USER.toString());

// 		MvcResult res = this.mockMvc
// 				.perform(
// 						MockMvcRequestBuilders.post("/authenticate")
// 						.contentType(MediaType.APPLICATION_JSON)
// 						.content(objectMapper.writeValueAsString(u1)))
// 				.andDo(print())
// 				.andExpect(status().isOk())
// 				.andReturn();

// 		String token = JsonPath.read(res.getResponse().getContentAsString(), "$.token");

// 		this.mockMvc
// 		.perform(
// 				MockMvcRequestBuilders.get("/address")
// 				.contentType(MediaType.APPLICATION_JSON))
// 		.andDo(print())
// 		.andExpect(status().isUnauthorized());

// 		this.mockMvc
// 			.perform(
// 					MockMvcRequestBuilders.get("/address")
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.header("authorization", "Bearer " + token))
// 			.andDo(print())
// 			.andExpect(status().isOk());

// 	 Address address = new Address("8 allée", "Paris", "75001", "France", 22);


// 	 this.mockMvc
// 		.perform(
// 				MockMvcRequestBuilders.post("/address")
// 				.contentType(MediaType.APPLICATION_JSON)
// 				.content(objectMapper.writeValueAsString(address))
// 				.header("authorization", "Bearer " + token))
// 		.andDo(print())
// 		.andExpect(status().isCreated());


// 	this.mockMvc
// 	.perform(
// 			MockMvcRequestBuilders.delete("/address/{id}", "22")
// 			.contentType(MediaType.APPLICATION_JSON)
// 			.header("authorization", "Bearer " + token))
// 	.andDo(print())
// 	.andExpect(status().isForbidden());

// 	}

// }
