package ru.itmo.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.dao.CustomerInMemoryDao;

/**
 * @author akirakozov
 */
@Component
public class CustomerManagerImpl implements CustomerManager {
    CustomerInMemoryDao customerDao;

    public CustomerManagerImpl(@Autowired CustomerInMemoryDao customerDao) {
        this.customerDao = customerDao;
    }

    public int addCustomer(Customer customer) {
        return customerDao.addCustomer(customer);
    }

    public Customer findCustomer(int id) {
        return customerDao.findCustomer(id);
    }
}
