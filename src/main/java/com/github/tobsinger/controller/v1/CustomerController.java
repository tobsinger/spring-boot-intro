package com.github.tobsinger.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tobsinger.entity.Customer;
import com.github.tobsinger.entity.QCustomer;
import com.github.tobsinger.repository.CustomerRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.net.URISyntaxException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

//import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/v1/person/")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EntityManager entityManager;

    /**
     * Get a {@link Customer} by ID
     *
     * @param id the ID of the customer to be returned
     * @return A JSON representation of the found customer
     * @throws JsonProcessingException
     */
    @RequestMapping(path = "id/{id}", method = GET)
    @ApiOperation(value = "Fetches a customer by ID", response = Customer.class)
    public ResponseEntity<String> index( @ApiParam(value = "ID of the user to be searched for", required = true)
                                             @PathVariable Long id) throws JsonProcessingException {
        final Customer customer = customerRepository.findOne(id);
        return buildResponse(customer);
    }

    /**
     * Get {@link Customer}s by their last name
     *
     * @param name the last name to look for
     * @return A JSON representation of the found {@link Customer}s
     * @throws URISyntaxException
     * @throws JsonProcessingException
     */
    @RequestMapping(path = "lastname/{name}", method = GET)
    @ApiOperation(value = "Fetches customers by last name", response = Customer[].class)
    public ResponseEntity<String> customQuery( @ApiParam(value = "last name to be searched for", required = true)
                                                   @PathVariable String name) throws URISyntaxException, JsonProcessingException {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        final QueryResults<Customer> customers = queryFactory.selectFrom(QCustomer.customer)
                .where(QCustomer.customer.lastName.eq(name))
                .fetchResults();
        return buildResponse(customers.getResults());
    }

    /**
     * Build a response with the given object and transform the data into JSON
     *
     * @param object The object to be serialized as JSON
     * @return The built {@link ResponseEntity}
     * @throws JsonProcessingException
     */
    private ResponseEntity<String> buildResponse(final Object object) throws JsonProcessingException {
        if (object == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.valueOf("application/json"));
        final ObjectMapper mapper = new ObjectMapper();
        final String result = mapper.writer().writeValueAsString(object);
        return new ResponseEntity<>(result, responseHeaders, HttpStatus.OK);
    }
}