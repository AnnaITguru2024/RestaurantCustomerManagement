package portfolio.RestaurantCustomerManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import portfolio.RestaurantCustomerManagement.converter.CustomerConverter;
import portfolio.RestaurantCustomerManagement.data.Customer;
import portfolio.RestaurantCustomerManagement.data.Preferences;
import portfolio.RestaurantCustomerManagement.data.VisitRecords;
import portfolio.RestaurantCustomerManagement.domain.CustomerDetail;
import portfolio.RestaurantCustomerManagement.repository.CustomerRepository;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @Mock
  private CustomerRepository repository;

  @Mock
  private CustomerConverter converter;

  private CustomerService sut; // System Under Test

  @BeforeEach
  void setup() {
    sut = new CustomerService(repository, converter);
  }

  @Test
  void 全ての顧客を取得する際にリポジトリとコンバータが正しく呼び出される() {
    List<Customer> customers = new ArrayList<>();
    List<Preferences> preferencesList = new ArrayList<>();
    List<VisitRecords> visitRecordsList = new ArrayList<>();
    List<CustomerDetail> expectedCustomerDetails = new ArrayList<>();

    when(repository.findAllCustomers()).thenReturn(customers);
    when(repository.findAllPreferences()).thenReturn(preferencesList);
    when(repository.findAllVisitRecords()).thenReturn(visitRecordsList);
    when(converter.convertCustomerDetails(customers, preferencesList, visitRecordsList)).thenReturn(expectedCustomerDetails);

    List<CustomerDetail> actualCustomerDetails = sut.findAllCustomers();

    verify(repository, times(1)).findAllCustomers();
    verify(repository, times(1)).findAllPreferences();
    verify(repository, times(1)).findAllVisitRecords();
    verify(converter, times(1)).convertCustomerDetails(customers, preferencesList, visitRecordsList);

    assertEquals(expectedCustomerDetails, actualCustomerDetails);
  }

  @Test
  void 顧客が存在しない場合に空のリストが返される() {
    when(repository.findAllCustomers()).thenReturn(new ArrayList<>());
    when(repository.findAllPreferences()).thenReturn(new ArrayList<>());
    when(repository.findAllVisitRecords()).thenReturn(new ArrayList<>());
    when(converter.convertCustomerDetails(anyList(), anyList(), anyList())).thenReturn(new ArrayList<>());

    List<CustomerDetail> actualCustomerDetails = sut.findAllCustomers();

    assertTrue(actualCustomerDetails.isEmpty());
  }
}