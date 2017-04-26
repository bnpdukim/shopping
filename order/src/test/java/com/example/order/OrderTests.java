package com.example.order;

import com.example.order.dto.OrderDto;
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
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderTests {
	@Autowired
	private MockMvc mockMvc;
	@Value("${api.version}")
	private String apiVersion;
	@Autowired
	private ObjectMapper objectMapper;

	private String ORDER_URI;

	@Before
	public void setup() {
		ORDER_URI = "/api/"+apiVersion;
	}

	@Test
	public void 주문() throws Exception {
		String principalId = "bnp";
		Long productId = 1L;
		Integer quantity = 5;

		OrderDto.New newOrder = new OrderDto.New(principalId, productId, quantity);
		mockMvc
				.perform(prepareNewObjectBuilder(ORDER_URI, newOrder))
				.andExpect(status().isCreated());
	}

	private RequestBuilder prepareNewObjectBuilder(String uri, Object newProduct) throws JsonProcessingException {
		return post(uri)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(newProduct));
	}

	@Test
	public void 주문_전체조회() {
		fail();
	}

	@Test
	public void 주문_id조회() {
		fail();
	}
}
