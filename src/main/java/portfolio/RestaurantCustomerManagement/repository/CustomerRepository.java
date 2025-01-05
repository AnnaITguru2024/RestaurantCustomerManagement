package portfolio.RestaurantCustomerManagement.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import portfolio.RestaurantCustomerManagement.data.Customer;
import portfolio.RestaurantCustomerManagement.data.Preferences;
import portfolio.RestaurantCustomerManagement.data.VisitRecords;

import java.util.List;
import portfolio.RestaurantCustomerManagement.domain.CustomerDetail;

/**
 * 顧客、好み、訪問履歴に関連するリポジトリ。
 */
@Repository
@Mapper
public interface CustomerRepository {

  /**
   * 全ての顧客情報を取得します。
   *
   * @return 顧客のリスト
   */
  List<Customer> findAllCustomers();

  /**
   * 特定の顧客IDに基づいて顧客情報を取得します。
   *
   * @param id 顧客ID
   * @return 顧客情報
   */
  Customer findCustomerById(@Param("id") int id);

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
  List<CustomerDetail> findCustomerDetailsByConditions(
      @Param("name") String name,
      @Param("furigana") String furigana,
      @Param("gender") String gender,
      @Param("phoneNumber") String phoneNumber,
      @Param("email") String email,
      @Param("address") String address
  );

  /**
   * 全ての訪問履歴を取得します。
   *
   * @return 訪問履歴のリスト
   */
  List<VisitRecords> findAllVisitRecords();

  /**
   * 特定の顧客IDに関連付けられた訪問履歴を取得します。
   *
   * @param customerId 顧客ID
   * @return 訪問履歴のリスト
   */
  List<VisitRecords> findVisitRecordsByCustomerId(@Param("customerId") int customerId);

  /**
   * 全ての顧客の好み情報を取得します。
   *
   * @return 好みのリスト
   */
  List<Preferences> findAllPreferences();

  /**
   * 特定の顧客IDに関連付けられた好み情報を取得します。
   *
   * @param customerId 顧客ID
   * @return 顧客の好み
   */
  Preferences findPreferencesByCustomerId(@Param("customerId") int customerId);

  /**
   * 新しい顧客を登録します。
   *
   * @param customer 顧客情報
   */
  void registerCustomer(Customer customer);

  /**
   * 新しい訪問履歴を登録します。
   *
   * @param visitRecords 訪問履歴
   */
  void registerVisitRecords(VisitRecords visitRecords);

  /**
   * 新しい好み情報を登録します。
   *
   * @param preferences 顧客の好み
   */
  void registerPreferences(Preferences preferences);

  /**
   * 顧客情報を更新します。
   *
   * @param customer 更新された顧客情報
   */
  void updateCustomer(Customer customer);

  /**
   * 訪問履歴を更新します。
   *
   * @param visitRecords 更新された訪問履歴
   */
  void updateVisitRecords(VisitRecords visitRecords);

  /**
   * 顧客の好みを更新します。
   *
   * @param preferences 更新された好み
   */
  void updatePreferences(Preferences preferences);

  // 顧客が存在するか確認
  boolean existsCustomerById(@Param("customerId") int customerId);

  /**
   * 特定の顧客を削除します。
   *
   * @param customerId 削除する顧客のID
   */
  void deleteCustomerById(@Param("customerId") int customerId);

  /**
   * 特定の顧客IDに関連付けられた訪問履歴を削除します。
   *
   * @param customerId 顧客ID
   */
  void deleteVisitRecordsByCustomerId(@Param("customerId") int customerId);

  /**
   * 特定の顧客IDに関連付けられた好みを削除します。
   *
   * @param customerId 顧客ID
   */
  void deletePreferencesByCustomerId(@Param("customerId") int customerId);
}
