package portfolio.RestaurantCustomerManagement.service;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.RestaurantCustomerManagement.converter.CustomerConverter;
import portfolio.RestaurantCustomerManagement.data.Customer;
import portfolio.RestaurantCustomerManagement.data.Preferences;
import portfolio.RestaurantCustomerManagement.data.VisitRecords;
import portfolio.RestaurantCustomerManagement.domain.CustomerDetail;
import portfolio.RestaurantCustomerManagement.exception.NotFoundException;
import portfolio.RestaurantCustomerManagement.repository.CustomerRepository;

import java.util.List;

/**
 * 顧客、訪問履歴、好み情報を取り扱うサービスクラスです。
 */
@Service
public class CustomerService {

  private final CustomerRepository repository;
  private final CustomerConverter converter;

  @Autowired
  public CustomerService(CustomerRepository repository, CustomerConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 全ての顧客を取得します。
   *
   * @return 顧客詳細のリスト
   */
  public List<CustomerDetail> findAllCustomers() {
    List<Customer> customers = repository.findAllCustomers();
    List<Preferences> preferencesList = repository.findAllPreferences();  // 全顧客の好み情報を取得
    List<VisitRecords> visitRecordsList = repository.findAllVisitRecords();  // 全訪問履歴を取得
    return converter.convertCustomerDetails(customers, preferencesList, visitRecordsList);
  }

  /**
   * 顧客IDを基に特定の顧客を取得します。
   *
   * @param id 顧客ID
   * @return 顧客詳細
   */
  public CustomerDetail findCustomerById(int id) {
    Customer customer = repository.findCustomerById(id);
    if (customer == null) {
      throw new NotFoundException("Customer with ID " + id + " not found.");
    }
    List<Preferences> preferencesList = repository.findAllPreferences();
    List<VisitRecords> visitRecordsList = repository.findAllVisitRecords();
    return converter.convertToCustomerDetail(customer, preferencesList, visitRecordsList);
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
  public List<CustomerDetail> searchCustomerDetails(String name, String furigana, String gender,
      String phoneNumber, String email, String address) {
    return repository.findCustomerDetailsByConditions(name, furigana, gender, phoneNumber, email, address);
  }

  /**
   * 顧客情報を登録するヘルパーメソッド。
   *
   * @param preference 顧客の好み情報
   * @param customerId 顧客ID
   */
  private void initPreferences(Preferences preference, int customerId) {
    preference.setCustomerId(customerId);  // 顧客IDを設定
  }

  /**
   * 訪問履歴を登録するヘルパーメソッド。
   *
   * @param visitRecord 訪問履歴
   * @param customerId  顧客ID
   */
  private void initVisitRecord(VisitRecords visitRecord, int customerId) {
    visitRecord.setCustomerId(customerId);  // 顧客IDを設定
  }

  /**
   * 新しい顧客を登録します。
   *
   * @param customerDetail 顧客詳細
   * @return 登録した顧客詳細
   */
  @Transactional
  public CustomerDetail registerCustomer(CustomerDetail customerDetail) {
    // 顧客情報の登録
    Customer customer = customerDetail.getCustomer();
    repository.registerCustomer(customer);

    // 顧客IDを取得
    int customerId = customer.getId();

    // Preferencesの登録
    List<Preferences> preferencesList = customerDetail.getPreferences();
    List<Preferences> registeredPreferences = new ArrayList<>();
    preferencesList.forEach(preference -> {
      initPreferences(preference, customerId);
      repository.registerPreferences(preference);
      registeredPreferences.add(preference);
    });

    // VisitRecordsの登録
    List<VisitRecords> visitRecordsList = customerDetail.getVisitRecords();
    List<VisitRecords> registeredVisitRecords = new ArrayList<>();
    visitRecordsList.forEach(visitRecord -> {
      initVisitRecord(visitRecord, customerId);
      repository.registerVisitRecords(visitRecord);
      registeredVisitRecords.add(visitRecord);
    });

    // 登録された情報をCustomerDetailに再設定
    customerDetail.setPreferences(registeredPreferences);
    customerDetail.setVisitRecords(registeredVisitRecords);

    return customerDetail;
  }



  /**
   * 顧客IDを基に特定の顧客の好み情報を取得します。
   *
   * @param customerId 顧客ID
   * @return 顧客の好み情報
   */
  public Preferences findPreferencesByCustomerId(int customerId) {
    List<Preferences> preferencesList = repository.findAllPreferences();
    return preferencesList.stream()
        .filter(p -> p.getCustomerId() == customerId)
        .findFirst()
        .orElse(null);  // 見つからなければnullを返す
  }

  /**
   * 顧客IDを基に特定の顧客の訪問履歴を取得します。
   *
   * @param customerId 顧客ID
   * @return 顧客の訪問履歴
   */
  public List<VisitRecords> findVisitRecordsByCustomerId(int customerId) {
    List<VisitRecords> visitRecordsList = repository.findAllVisitRecords();
    return visitRecordsList.stream()
        .filter(v -> v.getCustomerId() == customerId)
        .collect(Collectors.toList());  // 顧客IDに該当する訪問履歴をリストとして返す
  }

  /**
   * 新しい訪問履歴を登録します。
   *
   * @param visitRecords 訪問履歴
   */
  @Transactional
  public void registerVisitRecords(VisitRecords visitRecords) {
    repository.registerVisitRecords(visitRecords);
  }

  /**
   * 新しい好み情報を登録します。
   *
   * @param preferences 好み情報
   */
  @Transactional
  public void registerPreferences(Preferences preferences) {
    repository.registerPreferences(preferences);
  }

  /**
   * 顧客情報を更新します。
   *
   * @param customerDetail 更新する顧客詳細
   */
  @Transactional
  public void updateCustomer(CustomerDetail customerDetail) {
    Customer customer = customerDetail.getCustomer();
    repository.updateCustomer(customer);

    // preferences が null でない場合のみ処理
    if (customerDetail.getPreferences() != null) {
      customerDetail.getPreferences().forEach(repository::updatePreferences);
    }

    // visitRecords が null でない場合のみ処理
    if (customerDetail.getVisitRecords() != null) {
      customerDetail.getVisitRecords().forEach(repository::updateVisitRecords);
    }
  }


  /**
   * 訪問履歴を更新します。
   *
   * @param visitRecords 更新する訪問履歴
   */
  @Transactional
  public void updateVisitRecords(VisitRecords visitRecords) {
    repository.updateVisitRecords(visitRecords);
  }

  /**
   * 好み情報を更新します。
   *
   * @param preferences 更新する好み情報
   */
  @Transactional
  public void updatePreferences(Preferences preferences) {
    repository.updatePreferences(preferences);
  }

  /**
   * 特定の顧客と関連データを削除します。
   *
   * @param customerId 削除する顧客ID
   */
  @Transactional
  public void deleteCustomerData(int customerId) {
    // 顧客が存在するか確認
    if (!repository.existsCustomerById(customerId)) {
      throw new NotFoundException("顧客が見つかりません: ID = " + customerId);
    }

    // 顧客関連データの削除
    repository.deleteVisitRecordsByCustomerId(customerId);
    repository.deletePreferencesByCustomerId(customerId);
    repository.deleteCustomerById(customerId);
  }
}
