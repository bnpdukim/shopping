package com.example.order;

import com.example.order.dto.OrderDto;
import com.example.order.service.ProductEndPoint;
import com.example.order.service.UserEndPoint;
import com.example.product.dto.ProductDto;
import com.example.user.dto.UserDto;
import com.example.user.type.Sex;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.mock.Calls;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

	@MockBean
    private ProductEndPoint productEndPoint;

    @MockBean
    private UserEndPoint userEndPoint;

	@Before
	public void setup() {
		ORDER_URI = "/api/"+apiVersion;

        Response<ProductDto.Response> productResponse = Response.success(new ProductDto.Response(3L, "productname", 3000));

		when(productEndPoint.product(anyLong()))
                .thenReturn(Calls.response(productResponse));

        Response<UserDto.Response> userResponse = Response.success(new UserDto.Response(1L, "bnp", "username", Sex.MALE));

        when(userEndPoint.userProfile(anyString()))
                .thenReturn(Calls.response(userResponse));
	}

	@Test
	public void 주문() throws Exception {
		String principalId = "bnp";
		Long productId = 1L;
		Integer quantity = 5;

		OrderDto.New newOrder = new OrderDto.New(principalId, productId, quantity);
        ResultActions actions = createOrderAndResultAction(prepareNewObjectBuilder(ORDER_URI, newOrder));
        actions.andExpect(status().isCreated());
	}

    private ResultActions createOrderAndResultAction(RequestBuilder requestBuilder) throws Exception {
        return mockMvc
                .perform(requestBuilder);
    }

    private RequestBuilder prepareNewObjectBuilder(String uri, Object newProduct) throws JsonProcessingException {
		return post(uri)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(newProduct));
	}

	@Test
	public void 주문_전체조회() throws Exception {
	    int orderCount = 10;

        List<OrderDto.New> orders = createOrderDummyAndRegist(orderCount);
        log.info("{}",orders);
        retrieveUsersAndAssert(10);
	}

    private void retrieveUsersAndAssert(int userCount) throws Exception {
        retrieveUsersActions()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(userCount)));
    }

    private ResultActions retrieveUsersActions() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get(ORDER_URI)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        return mockMvc.perform(asyncDispatch(mvcResult));
    }

    private List<OrderDto.New> createOrderDummyAndRegist(int orderCount) {
        List<OrderDto.New> orders = createOrderDummy(orderCount);
        orders.stream().forEach(newOrder -> {
                try {
                    createOrderAndResultAction(prepareNewObjectBuilder(ORDER_URI, newOrder));
                } catch (Exception e) {
                    new RuntimeException();
                }
            }
        );
        return orders;
    }

    private List<OrderDto.New> createOrderDummy(int orderCount) {
        return IntStream
                .rangeClosed(1, orderCount)
                .mapToObj(n->new OrderDto.New(
                    RandomStringUtils.randomAlphabetic(8),
                    (long)(Math.random() * 10),
                    (int)(Math.random() * 10)
                )).collect(Collectors.toList());
    }

    @Test
	public void 주문_회원아이디로조회() {
		fail();
	}

	@Test
	public void 주문_id조회() {
		fail();
	}
}
