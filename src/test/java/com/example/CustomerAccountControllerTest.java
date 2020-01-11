package com.example;

import com.example.entity.CustomerAccount;
import com.example.helper.TestHelper;
import com.example.service.CustomerAccountService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static javax.servlet.http.HttpServletResponse.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerAccountControllerTest {

    @Autowired
    private CustomerAccountService customerAccountService;
    @Autowired
    private TestHelper testHelper;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public final void setup() {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void create() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        long id =
                given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toJSONString()).
                        when().post("/customerAccount").
                        then().statusCode(SC_OK).extract().path("id");

        customerAccountService.delete(id);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createWithEmptyName() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("name", "");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customerAccount").
                then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createWithEmptyEmail() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("email", "");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customerAccount").
                then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createWithNullName() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("name", nullValue());

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customerAccount").
                then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createWithNullEmail() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("email", nullValue());

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customerAccount").
                then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createWithInvalidEmail() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("email", "invalidEmail");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customerAccount").
                then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void getAll() {
        when().get("/customerAccount").
                then().statusCode(SC_OK);
    }

    @Test
    public void getById() {
        // create
        CustomerAccount customerAccount = customerAccountService.create(new CustomerAccount("Test Name", "testEmail@gmail.com"));
        long id = customerAccount.getId();

        try {
            // get by id
            when().get("/customerAccount/" + id).
                    then().statusCode(SC_OK).body("id", equalTo(id));
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void getByIdNotFound() {
        when().get("/customerAccount/0123456789").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    public void update() {
        // create
        CustomerAccount customerAccount = customerAccountService.create(new CustomerAccount("Test Name", "testEmail@gmail.com"));
        long id = customerAccount.getId();

        try {
            // change
            CustomerAccount changedCustomerAccount = customerAccountService.findById(id);
            changedCustomerAccount.setName("Another Name");

            // update
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(changedCustomerAccount).
                    when().
                    put("/customerAccount").
                    then().statusCode(SC_OK);

            // check changed
            CustomerAccount checkCustomerAccount = customerAccountService.findById(id);
            Assert.assertEquals(changedCustomerAccount.toString(), checkCustomerAccount.toString());
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void updateWithWrongId() {
        // create
        CustomerAccount customerAccount = new CustomerAccount("Test Name", "testEmail@gmail.com");
        customerAccount.setId(123456789L);

        // update
        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                body(customerAccount).
                when().
                put("/customerAccount").
                then().statusCode(SC_NOT_FOUND);

    }

    @Test
    public void updateWithNullId() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                body(jsonObject.toString()).
                when().
                put("/customerAccount").
                then().
                statusCode(SC_NOT_FOUND);
    }

    @Test
    public void updateWithNameNull() {
        // create
        CustomerAccount customerAccount = customerAccountService.create(new CustomerAccount("Test Name", "testEmail@gmail.com"));
        long id = customerAccount.getId();

        try {
            // change
            customerAccount.setName(null);

            //update
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(customerAccount).
                    when().
                    put("/customerAccount").
                    then().statusCode(SC_BAD_REQUEST);
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void updateWithEmailNull() {
        // create
        CustomerAccount customerAccount = customerAccountService.create(new CustomerAccount("Test Name", "testEmail@gmail.com"));
        long id = customerAccount.getId();

        try {
            // change
            customerAccount.setEmail(null);

            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(customerAccount).
                    when().
                    put("/customerAccount").
                    then().statusCode(SC_BAD_REQUEST);
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void updateWithEmptyName() {
        // create
        CustomerAccount customerAccount = customerAccountService.create(new CustomerAccount("Test Name", "testEmail@gmail.com"));
        long id = customerAccount.getId();

        try {
            // change
            customerAccount.setName("");

            //update
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(customerAccount).
                    when().
                    put("/customerAccount").
                    then().statusCode(SC_BAD_REQUEST);
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void updateWithEmptyEmail() {
        // create
        CustomerAccount customerAccount = customerAccountService.create(new CustomerAccount("Test Name", "testEmail@gmail.com"));
        long id = customerAccount.getId();

        try {
            // change
            customerAccount.setEmail("");

            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(customerAccount).
                    when().
                    put("/customerAccount").
                    then().statusCode(SC_BAD_REQUEST);
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void updateWithInvalidEmail() {
        // create
        CustomerAccount customerAccount = customerAccountService.create(new CustomerAccount("Test Name", "invalidEmail"));
        long id = customerAccount.getId();

        try {
            // change
            customerAccount.setEmail("");

            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(customerAccount).
                    when().
                    put("/customerAccount").
                    then().statusCode(SC_BAD_REQUEST);
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void delete() {
        // create
        CustomerAccount customerAccount = customerAccountService.create(new CustomerAccount("Test Name", "testEmail@gmail.com"));
        long id = customerAccount.getId();

        try {
            //delete
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    when().
                    delete("/customerAccount/{id}",id).
                    then().statusCode(SC_OK);
        } finally {
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void deleteIdNotFound() {
        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/customerAccount/0123456789").
                then().
                statusCode(SC_NOT_FOUND);
    }

    @Test
    public void deleteWithNullId() {
        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/customerAccount/" + nullValue()).
                then().
                statusCode(SC_BAD_REQUEST);
    }
}
