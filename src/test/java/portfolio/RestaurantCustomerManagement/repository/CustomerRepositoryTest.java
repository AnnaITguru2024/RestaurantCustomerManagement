package portfolio.RestaurantCustomerManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import portfolio.RestaurantCustomerManagement.data.Customer;
import portfolio.RestaurantCustomerManagement.data.Customer.Gender;

@MybatisTest
class CustomerRepositoryTest {

  @Autowired
  private  CustomerRepository sut;

  private static Customer createCustomer(){
    Customer customer = new Customer();
    customer.setName("江並こうじ");
    customer.setFurigana("エナミコウジ");
    customer.setGender(Gender.男性);
    customer.setPhoneNumber("08055557777");
    customer.setEmail("testXXX@example.com");
    customer.setBirthday(Date.valueOf(String.valueOf(1978-11-10)));
    customer.setAddress("奈良県");
    return customer;
  }

  @Test
  void 顧客の全件検索が行えること() {
    List<Customer> actual = sut.findAllCustomers();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 指定した顧客IDに紐づいく顧客の検索が行えること() {
    Customer actual = sut.findCustomerById(1); // 1はテスト用の既存IDに変更
    assertThat(actual).isNotNull();
    assertThat(actual.getName()).isEqualTo("田中 太郎"); // 既存の名前に合わせて変更
  }

  @Test
  void 顧客の登録が行えること() {
    // 新しい顧客情報を設定
    Customer customer = new Customer();
    customer.setName("長井 宏樹");
    customer.setFurigana("ナガイ ヒロキ");
    customer.setGender(Gender.男性);
    customer.setPhoneNumber("08055556666");
    customer.setEmail("testYYY@example.com");
    customer.setBirthday(Date.valueOf(LocalDate.of(1990, 3, 3)));
    customer.setAddress("大阪市");

    // 顧客登録
    sut.registerCustomer(customer);

    // 全顧客情報を取得
    List<Customer> actual = sut.findAllCustomers();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void  顧客の更新が行えること() {
    Customer customer = sut.findCustomerById(1); // 1はテスト用の既存IDに変更
    customer.setPhoneNumber("09000000000");

    sut.updateCustomer(customer);

    Customer updatedCustomer = sut.findCustomerById(1);
    assertThat(updatedCustomer.getPhoneNumber()).isEqualTo("09000000000");
  }
}