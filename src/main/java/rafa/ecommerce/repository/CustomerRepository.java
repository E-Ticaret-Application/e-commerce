package rafa.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rafa.ecommerce.domain.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	Customer findByUsername(String userName);
	
	Customer findByEmail(String email);

}
