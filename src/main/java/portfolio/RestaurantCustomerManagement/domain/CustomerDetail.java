package portfolio.RestaurantCustomerManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import portfolio.RestaurantCustomerManagement.data.Customer;
import portfolio.RestaurantCustomerManagement.data.Preferences;
import portfolio.RestaurantCustomerManagement.data.VisitRecords;

@Schema(description = "顧客詳細情報を格納するクラス。顧客情報、顧客の好み、訪問履歴を含みます。")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetail {

  @Valid
  @Schema(description = "顧客の基本情報")
  private Customer customer;

  @Valid
  @Schema(description = "顧客の好みを表す情報")
  private List<Preferences> preferences;

  @Valid
  @Schema(description = "顧客の訪問履歴のリスト")
  private List<VisitRecords> visitRecords;

}
