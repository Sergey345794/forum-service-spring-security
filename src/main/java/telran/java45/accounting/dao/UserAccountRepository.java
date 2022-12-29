package telran.java45.accounting.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.repository.CrudRepository;

import telran.java45.accounting.model.UserAccount;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    UserAccount findUserAccountByLogin(String login);

}
