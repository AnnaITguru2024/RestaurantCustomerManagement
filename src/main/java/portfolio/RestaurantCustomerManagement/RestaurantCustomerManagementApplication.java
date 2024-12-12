package portfolio.RestaurantCustomerManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "レストラン常連客管理システム"))
@SpringBootApplication
public class RestaurantCustomerManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantCustomerManagementApplication.class, args);
	}
}
