package ru.itmo;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.itmo.aspect.ProfilingAspect;
import ru.itmo.cache.LruCache;
import ru.itmo.domain.Customer;
import ru.itmo.domain.CustomerManager;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ProfilingApplication implements CommandLineRunner {
    private @Autowired LruCache<Integer, Integer> cache;
    private final ProfilingAspect profilingAspect;
    private final CustomerManager manager;

    public ProfilingApplication(ProfilingAspect profilingAspect, CustomerManager manager) {
        this.profilingAspect = profilingAspect;
        this.manager = manager;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProfilingApplication.class, args);
    }

    @Bean
    public LruCache<Integer, Integer> cache() {
        return new LruCache<>(10);
    }

    @Override
    public void run(String... args) {
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        cache.put(4, 40);
        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.get(4);
        cache.get(5);
        cache.getCapacity(); // final java function

        cache.test();

        int id = manager.addCustomer(new Customer("Petr"));
        manager.findCustomer(id);

        profilingAspect.getMethodStatistics().forEach((key, value) -> {
            System.out.printf("%s = {%n count = %s,%n sum = %s,%n average = %s%n}%n",
                key, value.getCount(), value.getSum(), value.getAverage()
            );
        });
    }
}
