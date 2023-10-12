package ca.gbc.productservice;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Assertions;

import org.springframework.data.mongodb.core.query.Query;


//import javax.management.Query;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests extends AbstractContainerBaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    private ProductRequest getProductRequest(){
        return ProductRequest.builder()
                .name("Apple IPad 2023")
                .description("Apple IPad Version 2023")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

    private List<Product> getProductList() {

        List<Product> products = new ArrayList<>();
        UUID uuid = UUID.randomUUID();

        Product product = Product.builder()
                .id(uuid.toString())
                .description("Apple IPad Version 2023")
                .price(BigDecimal.valueOf(1200))
                .build();

        products.add(product);

        return products;

    }

    private String convertObjectToString(List<ProductResponse> productList) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(productList);
    }

    private List<ProductResponse> convertStringToObject(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<List<ProductResponse>>() {
        });


    }


    @Test
    void createProduct() throws Exception {

        ProductRequest productRequest = getProductRequest();
        String productRequestJsonString = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //Assertions
        Assertions.assertTrue(productRepository.findAll().size() > 0);

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Apple IPad 2023"));
        List<Product> product = mongoTemplate.find(query, Product.class);
        Assertions.assertTrue(product.size() > 0);
    }

    /******
     *  BOD - Behaviour Driven Development
     * //Given - Setup
     * // When - Action
     * // Then - Verify
     ***/
    //
    @Test
    void getAllProducts() throws Exception{

        //GIVEN
        productRepository.saveAll(getProductList());

        //WHEN
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.
                get("/api/product")
                .accept(MediaType.APPLICATION_JSON));

        //THEN
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

        int actualSize = jsonNodes.size();
        int expectedSize = getProductList().size();

        Assertions.assertEquals(expectedSize, actualSize);

    }

    @Test
    void updateProducts() throws Exception{

        //GIVEN
        Product savedProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Widget")
                .description("Widget Original Price")
                .price(BigDecimal.valueOf(100))
                .build();

        //Saved product with original price
        productRepository.save(savedProduct);

        //prepare updated product and productRequest
        savedProduct.setPrice(BigDecimal.valueOf(200));
        String productRequestString = objectMapper.writeValueAsString(savedProduct);

        //WHEN
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/product/" + savedProduct.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestString));


        //THEN
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedProduct.getId()));
        Product storedProduct = mongoTemplate.findOne(query, Product.class);

        assertEquals(savedProduct.getPrice(), storedProduct.getPrice());
    }

    @Test
    void deleteProducts() throws Exception{

        //GIVEN
        Product savedProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Java Microservices Programming")
                .description("Course TextBook - Java Microservices Programming")
                .price(BigDecimal.valueOf(100))
                .build();

        productRepository.save(savedProduct);

        //WHEN
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/product/" + savedProduct.getId().toString())
                .contentType(MediaType.APPLICATION_JSON));

        //THEN
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedProduct.getId()));
        Long productCount = mongoTemplate.count(query, Product.class);

        assertEquals(0, productCount);

    }
}