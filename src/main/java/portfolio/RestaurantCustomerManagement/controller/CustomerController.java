package portfolio.RestaurantCustomerManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolio.RestaurantCustomerManagement.data.Customer;
import portfolio.RestaurantCustomerManagement.data.Preferences;
import portfolio.RestaurantCustomerManagement.data.VisitRecords;
import portfolio.RestaurantCustomerManagement.domain.CustomerDetail;
import portfolio.RestaurantCustomerManagement.exception.NotFoundException;
import portfolio.RestaurantCustomerManagement.service.CustomerService;

import java.util.List;

/**
 * 顧客情報に関する操作を行うREST APIを提供するコントローラー
 */
@Validated
@RestController
public class CustomerController {

  private final CustomerService service;

  @Autowired
  public CustomerController(CustomerService service) {
    this.service = service;
  }

  /**
   * 全ての顧客情報を取得します。
   *
   * @return 顧客情報のリスト
   */
  @Operation(
      summary = "全ての顧客情報を取得",
      description = "システム内の全ての顧客情報を取得します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "検索成功",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CustomerDetail.class))),
          @ApiResponse(responseCode = "204", description = "データなし")
      }
  )
  @GetMapping("/customerList")
  public ResponseEntity<List<CustomerDetail>> findAllCustomers() {
    List<CustomerDetail> customers = service.findAllCustomers();
    if (customers.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(customers);
  }

  /**
   * 特定の顧客IDに基づいて顧客情報を取得します。
   *
   * @param customerId 顧客ID
   * @return 顧客情報
   */
  @Operation(
      summary = "顧客情報の取得",
      description = "指定されたIDに基づいて顧客情報を取得します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "検索成功",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CustomerDetail.class))),
          @ApiResponse(responseCode = "404", description = "顧客が見つかりませんでした")
      }
  )
  @GetMapping("/customers/{customerId}")
  public ResponseEntity<CustomerDetail> getCustomerById(@PathVariable int customerId) {
    try {
      CustomerDetail customer = service.findCustomerById(customerId);
      return ResponseEntity.ok(customer);
    } catch (NotFoundException e) {
      throw new portfolio.RestaurantCustomerManagement.exception.NotFoundException(e.getMessage());
    }
  }

  /**
   * 条件に基づいて顧客詳細情報を検索します。
   * @param name 名前
   * @param furigana フリガナ
   * @param gender 性別
   * @param phoneNumber 電話番号
   * @param email メールアドレス
   * @param address 住所
   * @return 条件に一致する顧客詳細情報のリスト
   */
  @GetMapping("/customers")
  public List<CustomerDetail> findCustomerDetailsByConditions(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String furigana,
      @RequestParam(required = false) String gender,
      @RequestParam(required = false) String phoneNumber,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String address
  ) {
    return service.searchCustomerDetails(name, furigana, gender, phoneNumber, email, address);
  }

  /**
   * 新しい顧客を登録します。
   *
   * @param customerDetail 顧客情報
   * @return 登録された顧客情報
   */
  @Operation(
      summary = "顧客登録",
      description = "新しい顧客情報をシステムに登録します。",
      responses = {
          @ApiResponse(responseCode = "201", description = "登録成功",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CustomerDetail.class))),
          @ApiResponse(responseCode = "400", description = "入力エラー")
      }
  )
  @PostMapping("/customers")
  public ResponseEntity<CustomerDetail> registerCustomer(@RequestBody @Valid CustomerDetail customerDetail) {
    CustomerDetail createdCustomer = service.registerCustomer(customerDetail);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
  }

  /**
   * 顧客情報を更新します。
   *
   * @param id             顧客ID
   * @param customerDetail 顧客情報
   * @return 更新結果
   */
  @Operation(
      summary = "顧客更新",
      description = "既存の顧客情報を更新します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "更新成功"),
          @ApiResponse(responseCode = "400", description = "入力エラー"),
          @ApiResponse(responseCode = "404", description = "顧客が見つかりませんでした")
      }
  )
  @PutMapping("/customers/{id}")
  public ResponseEntity<String> updateCustomer(@PathVariable int id, @RequestBody @Valid CustomerDetail customerDetail) {
    try {
      service.updateCustomer(customerDetail);
      return ResponseEntity.ok("顧客情報が更新されました。");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  /**
   * 顧客の好み情報を取得します。
   *
   * @param customerId 顧客ID
   * @return 顧客の好み情報
   */
  @Operation(
      summary = "顧客の好み情報を取得",
      description = "指定された顧客IDに基づいて顧客の好み情報を取得します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "検索成功",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Preferences.class))),
          @ApiResponse(responseCode = "404", description = "顧客の好みが見つかりませんでした")
      }
  )
  @GetMapping("/customers/{customerId}/preferences")
  public ResponseEntity<Preferences> getCustomerPreferences(@PathVariable int customerId) {
    Preferences preferences = service.findPreferencesByCustomerId(customerId);
    if (preferences == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(preferences);
    }
  }

  /**
   * 顧客の訪問履歴を取得します。
   *
   * @param customerId 顧客ID
   * @return 顧客の訪問履歴
   */
  @Operation(
      summary = "顧客の訪問履歴を取得",
      description = "指定された顧客IDに基づいて顧客の訪問履歴を取得します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "検索成功",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = VisitRecords.class))),
          @ApiResponse(responseCode = "404", description = "訪問履歴が見つかりませんでした")
      }
  )
  @GetMapping("/customers/{customerId}/visitRecords")
  public ResponseEntity<List<VisitRecords>> getCustomerVisitRecords(@PathVariable int customerId) {
    List<VisitRecords> visitRecords = service.findVisitRecordsByCustomerId(customerId);
    if (visitRecords == null || visitRecords.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(visitRecords);
    }
  }
}
