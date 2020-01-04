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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Main.class)
@SpringBootTest
public class CustomerAccountControllerTests {

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
        System.out.println(jsonObject.toJSONString());
        long id =
                given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toJSONString()).
                        when().post("/customerAccount").
                        then().statusCode(SC_OK).extract().path("id");

        customerAccountService.delete(id);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void create_With_Empty_Name() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("name", "");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customerAccount").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void create_With_Empty_Email() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("email", "");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customerAccount").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void create_With_Null_Name() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("name", nullValue());

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customerAccount").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void create_With_Null_Email() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("email", nullValue());

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/customerAccount").
                then().statusCode(SC_NOT_FOUND);
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
    public void getById_NotFound() {
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
    public void update_With_Wrong_Id() {
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
    public void update_With_Null_Id() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                body(jsonObject.toString()).
                when().
                put("/customerAccount").
                then().
                statusCode(SC_NOT_FOUND);
    }

    @Test
    public void update_With_Name_Null() {
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
                    then().statusCode(SC_NOT_FOUND);
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void update_With_Email_Null() {
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
                    then().statusCode(SC_NOT_FOUND);
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void update_With_Empty_Name() {
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
                    then().statusCode(SC_NOT_FOUND);
        } finally {
            // delete
            if (customerAccountService.exists(id)) {
                customerAccountService.delete(id);
            }
        }
    }

    @Test
    public void update_With_Empty_Email() {
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
                    then().statusCode(SC_NOT_FOUND);
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
    public void delete_Id_NotFound() {
        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/customerAccount/0123456789").
                then().
                statusCode(SC_NOT_FOUND);
    }

    @Test
    public void delete_With_Null_Id() {
        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/customerAccount/" + nullValue()).
                then().
                statusCode(SC_NOT_FOUND);
    }
}
