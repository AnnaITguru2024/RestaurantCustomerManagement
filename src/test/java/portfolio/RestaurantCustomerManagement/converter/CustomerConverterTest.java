package portfolio.RestaurantCustomerManagement.converter;

import java.sql.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import portfolio.RestaurantCustomerManagement.data.Customer;
import portfolio.RestaurantCustomerManagement.data.Preferences;
import portfolio.RestaurantCustomerManagement.data.VisitRecords;
import portfolio.RestaurantCustomerManagement.domain.CustomerDetail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerConverterTest {

  private CustomerConverter sut;

  @BeforeEach
  void before() {
    sut = new CustomerConverter();
  }

  @Test
  void 顧客リスト好み情報リスト訪問記録リストを渡して顧客詳細リストが作成できること() {
    // 顧客データの準備
    Customer customer = new Customer();
    customer.setId(1); // 顧客IDを設定
    customer.setName("山田 太郎");
    customer.setEmail("yamada@example.com");

    // 好み情報データの準備
    Preferences preference = new Preferences();
    preference.setId(1);
    preference.setCustomerId(1);  // 顧客IDを合わせる
    preference.setPreference("寿司");

    // 訪問記録データの準備
    VisitRecords visitRecord = new VisitRecords();
    visitRecord.setId(1);
    visitRecord.setCustomerId(1);  // 顧客IDを合わせる
    visitRecord.setVisitDate(Date.valueOf("2024-01-01"));

    // リストを作成
    List<Customer> customerList = List.of(customer);
    List<Preferences> preferencesList = List.of(preference);
    List<VisitRecords> visitRecordsList = List.of(visitRecord);

    // テスト対象メソッドの実行
    List<CustomerDetail> actual = sut.convertCustomerDetails(customerList, preferencesList, visitRecordsList);

    // actualリストが空でないことを確認
    System.out.println("actual: " + actual);  // デバッグ用
    assertThat(actual).isNotEmpty();

    // 顧客が正しいことを確認
    assertThat(actual.get(0).getCustomer()).isEqualTo(customer);  // インデックス0に期待される顧客情報

    // 好み情報が正しいことを確認
    assertThat(actual.get(0).getPreferences()).isNotEmpty();  // インデックス0に期待される好み情報
    Preferences actualPreference = actual.get(0).getPreferences().get(0);
    assertThat(actualPreference.getId()).isEqualTo(preference.getId());
    assertThat(actualPreference.getCustomerId()).isEqualTo(preference.getCustomerId());
    assertThat(actualPreference.getPreference()).isEqualTo(preference.getPreference());

    // 訪問記録が正しいことを確認
    assertThat(actual.get(0).getVisitRecords()).isNotEmpty();  // インデックス0に期待される訪問記録
    VisitRecords actualVisitRecord = actual.get(0).getVisitRecords().get(0);
    assertThat(actualVisitRecord.getId()).isEqualTo(visitRecord.getId());
    assertThat(actualVisitRecord.getCustomerId()).isEqualTo(visitRecord.getCustomerId());
    assertThat(actualVisitRecord.getVisitDate()).isEqualTo(visitRecord.getVisitDate());
  }

  @Test
  void 顧客リスト好み情報リスト訪問記録リストを渡した時に紐づかない情報が除外されること() {
    // 顧客データの準備
    Customer customer = new Customer();
    customer.setName("山田 太郎");
    customer.setEmail("yamada@example.com");

    // 紐づかない好み情報の準備
    Preferences unrelatedPreference = new Preferences();
    unrelatedPreference.setId(2);
    unrelatedPreference.setCustomerId(2);
    unrelatedPreference.setPreference("ラーメン");

    // 紐づかない訪問記録の準備
    VisitRecords unrelatedVisitRecord = new VisitRecords();
    unrelatedVisitRecord.setId(2);
    unrelatedVisitRecord.setCustomerId(2);
    unrelatedVisitRecord.setVisitDate(Date.valueOf("2024-01-02"));

    // リストを作成
    List<Customer> customerList = List.of(customer);
    List<Preferences> preferencesList = List.of(unrelatedPreference);
    List<VisitRecords> visitRecordsList = List.of(unrelatedVisitRecord);

    // テスト対象メソッドの実行
    List<CustomerDetail> actual = sut.convertCustomerDetails(customerList, preferencesList, visitRecordsList);

    // 検証
    assertThat(actual).hasSize(1);
    assertThat(actual.get(0).getCustomer()).isEqualTo(customer);
    assertThat(actual.get(0).getPreferences()).isEmpty();
    assertThat(actual.get(0).getVisitRecords()).isEmpty();
  }


  @Test
  void 空のリストを渡した場合に空の顧客詳細リストが返されること() {
    // 空リストを準備
    List<Customer> customerList = Collections.emptyList();
    List<Preferences> preferencesList = Collections.emptyList();
    List<VisitRecords> visitRecordsList = Collections.emptyList();

    // テスト対象メソッドの実行
    List<CustomerDetail> actual = sut.convertCustomerDetails(customerList, preferencesList, visitRecordsList);

    // 検証
    assertThat(actual).isEmpty();
  }
}
