package portfolio.RestaurantCustomerManagement.converter;

import java.util.Objects;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import portfolio.RestaurantCustomerManagement.data.Customer;
import portfolio.RestaurantCustomerManagement.data.Preferences;
import portfolio.RestaurantCustomerManagement.data.VisitRecords;
import portfolio.RestaurantCustomerManagement.domain.CustomerDetail;

/**
 * 顧客情報、顧客の好み情報、訪問記録情報の変換を行うコンバーターです。
 */
@Component
public class CustomerConverter {

  /**
   * 顧客情報を顧客詳細情報に変換します。
   *
   * @param customerList 顧客情報のリスト
   * @param preferencesList 顧客の好み情報のリスト
   * @param visitRecordsList 訪問記録情報のリスト
   * @return 顧客詳細情報のリスト
   */
  public List<CustomerDetail> convertCustomerDetails(List<Customer> customerList,
      List<Preferences> preferencesList, List<VisitRecords> visitRecordsList) {
    List<CustomerDetail> customerDetails = new ArrayList<>();
    customerList.forEach(customer -> {
      // 顧客情報を設定
      CustomerDetail customerDetail = convertToCustomerDetail(customer, preferencesList, visitRecordsList);
      customerDetails.add(customerDetail);
    });
    return customerDetails;
  }

  /**
   * 単一の顧客情報を顧客詳細に変換します。
   *
   * @param customer 顧客情報
   * @param preferencesList 顧客の好み情報リスト
   * @param visitRecordsList 顧客の訪問記録情報リスト
   * @return 顧客詳細情報
   */
  public CustomerDetail convertToCustomerDetail(Customer customer, List<Preferences> preferencesList,
      List<VisitRecords> visitRecordsList) {
    CustomerDetail customerDetail = new CustomerDetail();
    customerDetail.setCustomer(customer);

    // 顧客に紐づく好み情報を設定
    List<Preferences> customerPreferences = preferencesList.stream()
        .filter(preference -> Objects.equals(customer.getId(), preference.getCustomerId()))
        .collect(Collectors.toList());
    customerDetail.setPreferences(customerPreferences);

    // 顧客に紐づく訪問記録情報を設定
    List<VisitRecords> customerVisitRecords = visitRecordsList.stream()
        .filter(visitRecord -> Objects.equals(customer.getId(), visitRecord.getCustomerId()))
        .collect(Collectors.toList());
    customerDetail.setVisitRecords(customerVisitRecords);

    return customerDetail;
  }
}