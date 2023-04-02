package ru.itmo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;;
import ru.itmo.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
