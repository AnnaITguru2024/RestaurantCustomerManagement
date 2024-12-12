package portfolio.RestaurantCustomerManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "訪問履歴を格納するクラス。顧客ID、訪問日、支出金額、メモなどの情報を含みます。")
@Getter
@Setter
public class VisitRecords {

  private int id;

  private int customerId;

  private Date visitDate;

  private double totalSpent; // 支出金額

  private String notes;

  private Date createdAt;

  private Date updatedAt;
}
