package com.example.user;

import com.example.user.dto.UserDto;
import com.example.user.type.Sex;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserTests {
	@Autowired
	private MockMvc mockMvc;
	@Value("${api.version}")
	private String apiVersion;
	@Autowired
	private ObjectMapper objectMapper;

	private String USER_URI;

	@Before
	public void setup() {
		USER_URI = "/api/"+apiVersion;
	}

	@Test
	public void 유저_생성() throws Exception {
		UserDto.New newUser = new UserDto.New("id", "pw","김동억", Sex.MALE);
		mockMvc
				.perform(prepareNewObjectBuilder(USER_URI, newUser))
				.andExpect(status().isCreated());
	}

	private RequestBuilder prepareNewObjectBuilder(String uri, Object newProduct) throws JsonProcessingException {
		return post(uri)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(newProduct));
	}


	@Test
	public void 유저_전체조회() throws Exception {
		int userCount = 5;
		createDummyUserAndRegister(userCount);
		retrieveUsersAndAssert(userCount);
	}

	@Test
	public void 유저_아이디조회() throws Exception {
		int userCount = 5;
		createDummyUserAndRegister(userCount);

		List<UserDto.Response> userList = retrieveUsers();
		UserDto.Response aUser = userList.get(0);
		mockMvc.perform(
				get(USER_URI+"/"+aUser.getId()).accept(MediaType.APPLICATION_JSON_UTF8)
		).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect( jsonPath("$.id", is(aUser.getId().intValue())) )
				.andExpect( jsonPath("$.principalId", is(aUser.getPrincipalId())) )
				.andExpect( jsonPath("$.name", is(aUser.getName())) )
				.andExpect( jsonPath("$.sex", is(aUser.getSex().name())) );

	}

	private List<UserDto.Response> retrieveUsers() throws Exception {
		MvcResult result = retrieveUsersActions().andReturn();
		return objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, UserDto.Response.class));
	}

	private void retrieveUsersAndAssert(int userCount) throws Exception {
		retrieveUsersActions()
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(userCount)));
	}

	private ResultActions retrieveUsersActions() throws Exception {
		MvcResult mvcResult = mockMvc.perform(
			get(USER_URI)
				.accept(MediaType.APPLICATION_JSON_UTF8)
		).andReturn();
		return mockMvc.perform(asyncDispatch(mvcResult));
	}

	private void createDummyUserAndRegister(int userCount) {
		List<UserDto.New> newUsers = createDummyUser("userName", userCount);
		newUsers.stream().forEach(newUser -> {
			try {
				mockMvc.perform(prepareNewObjectBuilder(USER_URI, newUser));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	private List<UserDto.New> createDummyUser(String prefix, int count) {
		return IntStream
				.rangeClosed(1, count)
				.mapToObj(n->new UserDto.New(prefix+n, prefix+n, prefix+n, Sex.MALE))
				.collect(Collectors.toList());
	}
}
