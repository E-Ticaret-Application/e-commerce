package rafa.ecommerce.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Table(name = "customers")
@Entity
@Data
@AllArgsConstructor
@PrimaryKeyJoinColumn(name="id")
@NoArgsConstructor
public class Customer extends  User{


   
	@Column(name="first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


  /*  @OneToMany(fetch = FetchType.LAZY,mappedBy = "customer")
    private List<Order> orders;*/
    
    /*@OneToOne(fetch = FetchType.LAZY,mappedBy = "customer")
	private ShoppingCart shoppingCart;*/

    public Customer(String firstName, String lastName, String username, String password, String email) {
		super(username,password,email);
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
    
    
    
    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }

}
