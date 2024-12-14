package portfolio.RestaurantCustomerManagement.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import portfolio.RestaurantCustomerManagement.exception.Handler;
import portfolio.RestaurantCustomerManagement.service.CustomerService;
import portfolio.RestaurantCustomerManagement.domain.CustomerDetail;
import portfolio.RestaurantCustomerManagement.data.Customer;
import portfolio.RestaurantCustomerManagement.data.Preferences;
import portfolio.RestaurantCustomerManagement.data.VisitRecords;


@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

  @Mock
  private CustomerService customerService;

  @InjectMocks
  private CustomerController customerController;

  private MockMvc mockMvc;


  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(customerController)
        .setControllerAdvice(new Handler()) // 例外ハンドラをセット
        .build();
  }

  @Test
  void 顧客の詳細が取得できること() throws Exception {
    int customerId = 1;

    // Customerオブジェクトの生成とフィールド設定
    Customer customer = new Customer();
    customer.setId(1);
    customer.setName("山田 太郎");
    customer.setFurigana("ヤマダ タロウ");
    customer.setGender(Customer.Gender.男性);
    customer.setEmail("example@mail.com");
    customer.setAddress("東京都");

    // Preferencesオブジェクトの生成とフィールド設定
    Preferences preference = new Preferences();
    preference.setId(1);
    preference.setCustomerId(1);
    preference.setPreference("チキン");

    // VisitRecordsオブジェクトの生成とフィールド設定
    VisitRecords visitRecord = new VisitRecords();
    visitRecord.setId(1);
    visitRecord.setCustomerId(1);

    // CustomerDetailオブジェクトの生成とフィールド設定
    CustomerDetail mockCustomerDetail = new CustomerDetail();
    mockCustomerDetail.setCustomer(customer);
    mockCustomerDetail.setPreferences(List.of(preference));
    mockCustomerDetail.setVisitRecords(List.of(visitRecord));

    // モックサービスの設定
    when(customerService.findCustomerById(customerId)).thenReturn(mockCustomerDetail);

    // GETリクエストを実行してレスポンス内容を検証
    mockMvc.perform(get("/customers/{id}", customerId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.customer.id").value(1))
        .andExpect(jsonPath("$.customer.name").value("山田 太郎"))
        .andExpect(jsonPath("$.customer.email").value("example@mail.com"))
        .andExpect(jsonPath("$.customer.address").value("東京都"))
        .andExpect(jsonPath("$.preferences[0].id").value(1))
        .andExpect(jsonPath("$.preferences[0].preference").value("チキン"))
        .andExpect(jsonPath("$.visitRecords[0].id").value(1));

    // サービスメソッドが1回呼ばれたことを確認
    verify(customerService, times(1)).findCustomerById(customerId);
  }



  @Test
  void 顧客リストが取得できること() throws Exception {
    // Customerオブジェクトの生成とフィールド設定
    Customer customer1 = new Customer();
    customer1.setId(1);
    customer1.setName("山田 太郎");
    customer1.setEmail("example@mail.com");
    customer1.setAddress("東京都");

    Customer customer2 = new Customer();
    customer2.setId(2);
    customer2.setName("佐藤 花子");
    customer2.setEmail("example2@mail.com");
    customer2.setAddress("大阪府");

    // CustomerDetailオブジェクトの生成とフィールド設定
    CustomerDetail customerDetail1 = new CustomerDetail();
    customerDetail1.setCustomer(customer1);
    customerDetail1.setPreferences(List.of()); // 空のリストを設定
    customerDetail1.setVisitRecords(List.of()); // 空のリストを設定

    CustomerDetail customerDetail2 = new CustomerDetail();
    customerDetail2.setCustomer(customer2);
    customerDetail2.setPreferences(List.of());
    customerDetail2.setVisitRecords(List.of());

    // モックサービスの設定
    List<CustomerDetail> customerDetails = List.of(customerDetail1, customerDetail2);
    when(customerService.findAllCustomers()).thenReturn(customerDetails);

    // GETリクエストを実行してレスポンス内容を検証
    mockMvc.perform(get("/customerList"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].customer.name").value("山田 太郎"))
        .andExpect(jsonPath("$[1].customer.name").value("佐藤 花子"));

    // サービスメソッドが1回呼ばれたことを確認
    verify(customerService, times(1)).findAllCustomers();
  }

  @Test
  void 新しい顧客が登録できること() throws Exception {
    String newCustomerJson = """
            {
                "customer": {
                    "name": "田中 一郎",
                    "furigana": "タナカ イチロウ",
                    "email": "tanaka@mail.com",
                    "address": "北海道"
                },
                "preferences": [],
                "visitRecords": []
            }
            """;

    Customer customer = new Customer();
    customer.setId(1);
    customer.setName("田中 一郎");
    customer.setFurigana("タナカ イチロウ");
    customer.setEmail("tanaka@mail.com");
    customer.setAddress("北海道");

    CustomerDetail mockCustomerDetail = new CustomerDetail();
    mockCustomerDetail.setCustomer(customer);
    mockCustomerDetail.setPreferences(List.of());
    mockCustomerDetail.setVisitRecords(List.of());

    when(customerService.registerCustomer(any(CustomerDetail.class))).thenReturn(mockCustomerDetail);

    mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(newCustomerJson))
        .andDo(print()) // エラー詳細を出力
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.customer.name").value("田中 一郎"))
        .andExpect(jsonPath("$.customer.furigana").value("タナカ イチロウ"))
        .andExpect(jsonPath("$.customer.email").value("tanaka@mail.com"))
        .andExpect(jsonPath("$.customer.address").value("北海道"));

    verify(customerService, times(1)).registerCustomer(any(CustomerDetail.class));
  }

  @Test
  void 顧客情報が更新できること() throws Exception {
    // PUTリクエストで送信する顧客情報
    String updatedCustomerJson = """
            {
                "id": 1,
                "name": "田中 一郎",
                "email": "tanaka_updated@mail.com",
                "address": "福岡県"
            }
            """;

    // モックサービスの設定
    doNothing().when(customerService).updateCustomer(any(CustomerDetail.class));

    // PUTリクエストの検証
    mockMvc.perform(put("/customers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedCustomerJson))
        .andExpect(status().isOk());

    // サービスメソッドが1回呼ばれたことを確認
    verify(customerService, times(1)).updateCustomer(any(CustomerDetail.class));
  }

  @Test
  void 存在しない顧客を検索した場合404エラーが返ること() throws Exception {
    int invalidCustomerId = 999;

    // モックの振る舞いを設定
    when(customerService.findCustomerById(invalidCustomerId))
        .thenThrow(new portfolio.RestaurantCustomerManagement.exception.NotFoundException(
            "Customer with ID " + invalidCustomerId + " not found"
        ));

    // コントローラの手動セットアップ（モックを注入）
    CustomerController customerController = new CustomerController(customerService);
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController)
        .setControllerAdvice(new Handler()) // Exceptionハンドラをセットアップ
        .build();

    // GETリクエストの検証
    mockMvc.perform(get("/customers/" + invalidCustomerId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound()) // 404 Not Foundを期待
        .andExpect(content().string("Customer with ID " + invalidCustomerId + " not found")); // エラーメッセージを期待

    // サービスメソッドが1回呼ばれたことを確認
    verify(customerService, times(1)).findCustomerById(invalidCustomerId);
  }
}