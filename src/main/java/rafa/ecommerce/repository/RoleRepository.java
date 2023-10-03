package rafa.ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rafa.ecommerce.domain.Role;
import rafa.ecommerce.domain.enums.ERole;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

	Optional<Role>  findByName(ERole role);

}
