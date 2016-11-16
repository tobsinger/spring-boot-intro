package com.github.tobsinger.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tobsinger.entity.Customer;
import com.github.tobsinger.entity.QCustomer;
import com.github.tobsinger.repository.CustomerRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@RestController
@RequestMapping("api/v1/customer")
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
    @RequestMapping(path = "/id/{id}", method = GET)
    @ApiOperation(value = "Fetches a customer by ID", response = Customer.class)
    public Customer getById(@ApiParam(value = "ID of the user to be searched for", required = true)
                          @PathVariable Long id) throws JsonProcessingException {
        final Customer customer = customerRepository.findOne(id);
        return customer;
    }

    /**
     * Get {@link Customer}s by their last name
     *
     * @param name the last name to look for
     * @return A JSON representation of the found {@link Customer}s
     * @throws URISyntaxException
     * @throws JsonProcessingException
     */
    @RequestMapping(path = "/lastname/{name}", method = GET)
    @ApiOperation(value = "Fetches customers by last name", response = Customer[].class)
    public List<Customer> getByLastName(@ApiParam(value = "last name to be searched for", required = true)
                                      @PathVariable String name) throws URISyntaxException, JsonProcessingException {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        final QueryResults<Customer> customers = queryFactory.selectFrom(QCustomer.customer)
                .where(QCustomer.customer.lastName.eq(name))
                .fetchResults();
        return customers.getResults();
    }

    /**
     * Add a {@link Customer} to the database
     *
     * @param customer The customer to be added
     * @return 201 if successful
     */
    @RequestMapping(method = PUT)
    @ApiOperation(value = "Add a customer")
    public ResponseEntity create(@ApiParam(value = "the customer to be added", required = true) @RequestBody final Customer customer) {
        customerRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}