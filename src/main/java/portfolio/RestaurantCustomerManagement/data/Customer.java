package portfolio.RestaurantCustomerManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "常連客")
@Getter
@Setter
public class Customer {

  private int id;

  @NotBlank
  private String name;

  @NotBlank
  private String furigana;

  private Gender gender;

  private String phoneNumber;

  @Email
  private String email;

  private Date birthday;

  private String address;

  private Date createdAt;

  private Date updatedAt;

  public enum Gender {
    男性, 女性, その他
  }

}
