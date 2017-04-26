package com.example.product;

import com.example.product.dto.ProductDto;
import com.example.product.service.ProductService;
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
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

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
public class ProductTests {

	@Autowired
	private MockMvc mockMvc;
	@Value("${api.version}")
	private String apiVersion;
	@Autowired
	private ObjectMapper objectMapper;

	private String PRODUCT_URI;

	@Before
	public void setup() {
		PRODUCT_URI = "/api/"+apiVersion;
	}

	@Test
	public void 제품_등록() throws Exception {
        ProductDto.New newProduct = new ProductDto.New("name", 30000);
        mockMvc
            .perform(prepareNewProductBuilder(newProduct))
            .andExpect(status().isCreated());
    }

	@Autowired
	private ProductService productService;

	@Test(expected = DataAccessException.class)
	public void 제품_2번등록() throws Exception {
		ProductDto.New newProduct = new ProductDto.New("name", 30000);
		productService.create(newProduct);
		productService.create(newProduct);
	}

	private RequestBuilder prepareNewProductBuilder(ProductDto.New newProduct) throws JsonProcessingException {
		return post(PRODUCT_URI)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(newProduct));
	}

	@Test
	public void 제품_전체조회() throws Exception {
	    int productCount = 5;
        createDummyProductAndRegister(productCount);
        assertProductsSize(productCount);
	}

    private void assertProductsSize(int productCount) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get(PRODUCT_URI)
                .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andReturn();
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productCount)));
    }

    private List<ProductDto.New> createDummyProduct(String productName, int initialValue, int count) {
		return IntStream
				.rangeClosed(1, count)
				.mapToObj(n->new ProductDto.New(productName+n, initialValue+n))
				.collect(Collectors.toList());
	}

	@Test
	public void 제품_아이디조회() throws Exception {
        int productCount = 5;
        List<ProductDto.Response> productList = createDummyProductsAndGet(productCount);

        ProductDto.Response aProduct = productList.get(0);
        mockMvc.perform(
                get(PRODUCT_URI+"/"+aProduct.getId()).accept(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect( jsonPath("$.id", is(aProduct.getId().intValue())) )
        .andExpect( jsonPath("$.name", is(aProduct.name())) )
        .andExpect( jsonPath("$.price", is(aProduct.price())) );
	}

	@Test
	public void 제품_삭제() throws Exception {
        int productCount = 5;
        List<ProductDto.Response> productList = createDummyProductsAndGet(productCount);

        ProductDto.Response aProduct = productList.get(0);
        mockMvc.perform(
                delete(PRODUCT_URI+"/"+aProduct.getId())
        ).andExpect(status().isNoContent());
        assertProductsSize(productCount-1);
	}

    private List<ProductDto.Response> createDummyProductsAndGet(int productCount) throws Exception {
        createDummyProductAndRegister(productCount);

        return retrieveProducts();
    }

    private List<ProductDto.Response> retrieveProducts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get(PRODUCT_URI).accept(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        MvcResult result = mockMvc.perform(asyncDispatch(mvcResult)).andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDto.Response.class));
    }

    private void createDummyProductAndRegister(int productCount) {
        List<ProductDto.New> newProducts = createDummyProduct("productName", 3000, productCount);
        newProducts.stream().forEach(newProduct -> {
            try {
                mockMvc.perform(prepareNewProductBuilder(newProduct));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
	public void 제품_수정() throws Exception {
	    int productCount = 1;
        List<ProductDto.Response> productList = createDummyProductsAndGet(productCount);

        ProductDto.Response aProduct = productList.get(0);
        String newName = "rename";
        int newPrice = 333;
        mockMvc.perform(
                put(PRODUCT_URI+"/"+aProduct.getId()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(new ProductDto.New(newName,newPrice)))
        ).andExpect(status().isOk());

        mockMvc.perform(
                get(PRODUCT_URI+"/"+aProduct.getId()).accept(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect( jsonPath("$.id", is(aProduct.getId().intValue())) )
                .andExpect( jsonPath("$.name", is(newName)) )
                .andExpect( jsonPath("$.price", is(newPrice)) );
	}

}
