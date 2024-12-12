package portfolio.RestaurantCustomerManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "常連客の個別の好みに関する情報（例: 好きな料理やアレルギー情報など）")
@Getter
@Setter
public class Preferences {

  private int id;

  private int customerId;

  @NotBlank
  private String preference;

  private Date createdAt;

  private Date updatedAt;
}
