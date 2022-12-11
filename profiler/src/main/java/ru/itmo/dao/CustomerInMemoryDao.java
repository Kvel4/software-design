package ru.itmo.dao;

import org.springframework.stereotype.Component;
import ru.itmo.domain.Customer;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author akirakozov
 */
@Component
public class CustomerInMemoryDao {
    private static final AtomicInteger currentId = new AtomicInteger(1);
    private final HashMap<Integer, Customer> customers = new HashMap<>();

    public int addCustomer(Customer customer) {
        int id = currentId.getAndIncrement();
        customers.put(id, customer);
        return id;
    }

    public Customer findCustomer(int id) {
        if (customers.containsKey(id)) {
            return customers.get(id);
        } else {
            throw new EntityNotFoundException("Customer couldn't be found by id: " + id);
        }
    }
}
