# RestaurantCustomerManagement
### レストラン常連客管理システム

___
## サービス概要
このプロジェクトは、レストランの常連客情報を管理・分析するためのシステムです。  
レストラン経営者が使用することを想定しており、常連客との関係を維持・強化するために役立つ機能を提供します。

___

## 作成背景
JavaやSpring Bootの学習成果を形にし、レストラン経営者が顧客管理を効率化できるようなツールを開発することを目指しました。  
実務で必要とされる以下の技術やツールを活用しています：

- **REST APIの設計と実装**：顧客データのCRUD操作をサポート
- **自動テスト**：JUnitを使用して単体テストを実装
- **AWSを使用したデプロイ**：クラウド環境でのシステム運用

___ 
## 主な使用技術

### バックエンド

![badge](https://img.shields.io/badge/language-Java_21-%23007396)
![badge](https://img.shields.io/badge/SpringBoot-3.4.0-%236DB33F?logo=spring)

### インフラ・DB

![badge](https://img.shields.io/badge/AWS-EC2-FF9900?logo=amazonec2&labelColor=cccccc)
![badge](https://img.shields.io/badge/AWS-RDS-527FFF?logo=amazonrds&labelColor=cccccc)
![badge](https://img.shields.io/badge/AWS-ALB-8C4FFF?logo=awselasticloadbalancing&labelColor=cccccc)
![badge](https://img.shields.io/badge/MySQL-%234479A1?logo=mysql&logoColor=white)

### 使用ツール

![badge](https://img.shields.io/badge/MyBatis-%23DC382D?logoColor=white)
![badge](https://img.shields.io/badge/Junit5-%2325A162?logo=junit5&logoColor=white)
![badge](https://img.shields.io/badge/Postman-%23FF6C37?logo=postman&logoColor=white)
![badge](https://img.shields.io/badge/GitHub-%23181717?logo=github&logoColor=white)
![badge](https://img.shields.io/badge/GitHub_Actions-%232088FF?logo=githubactions&logoColor=white)
![badge](https://img.shields.io/badge/-intellij%20IDEA-000.svg?logo=intellij-idea&style=flat)


___
## 機能一覧

| 機能          | 詳細                                                                 |
|-------------|----------------------------------------------------------------------|
| **顧客情報の登録** | 氏名、連絡先、来店回数、好みなどの情報を登録します。                     |
| **顧客の条件検索** | 氏名や来店回数などの条件を指定して該当する顧客情報を検索します。           |
| **来店履歴の管理** | 顧客ごとの来店履歴を登録し、分析に役立てます。                         |
| **顧客情報の更新** | 登録済みの顧客情報を変更します。                               
| **顧客情報の削除** | 顧客IDを指定し、不要な顧客情報を削除します。

## 設計書

---

### ER図
```mermaid
erDiagram
    CUSTOMER {
        int id PK "AUTO_INCREMENT"
        varchar(100) name "NOT NULL"
        varchar(100) furigana "NOT NULL"
        enum gender "('男性', '女性', 'その他') DEFAULT 'その他'"
        varchar(15) phone_number
        varchar(100) email
        date birthday
        text address
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    }
    VISIT_RECORDS {
        int id PK "AUTO_INCREMENT"
        int customer_id FK "NOT NULL"
        date visit_date "NOT NULL"
        decimal total_spent "(10, 2)"
        text notes
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    }
    PREFERENCES {
        int id PK "AUTO_INCREMENT"
        int customer_id FK "NOT NULL"
        text preference "NOT NULL"
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    }

    CUSTOMER ||--o{ VISIT_RECORDS: "has visits"
    CUSTOMER ||--o{ PREFERENCES: "has preferences"

```
### 説明
- **CUSTOMER**: 顧客の基本情報を管理するテーブル。
- **VISIT_RECORDS**: 顧客の訪問履歴を管理するテーブル。`customer_id`は`CUSTOMER`テーブルの外部キー。
- **PREFERENCES**: 顧客の好みや特記事項を管理するテーブル。`customer_id`は`CUSTOMER`テーブルの外部キー。
- 外部キー制約 (`ON DELETE CASCADE`) を表現するために関連を `||--o{` としています。


### URL設計

| HTTP<br/>メソッド | URL                                  | 処理内容          | 
|---------------|--------------------------------------|---------------|
| POST          | /customers                           | 顧客情報の作成       |
| GET           | /customerList                        | 顧客情報の全件検索     | 
| GET           | /customers/{customerId}              | 指定したIDの顧客情報の取得 |
| GET           | /customers            　　      　      | 顧客情報の条件検索     |
| GET           | /customers/{customerId}/preferences  | 顧客の好み情報の取得    |
| GET           | /customers/{customerId}/visitRecords | 顧客の訪問履歴の取得    |
| PUT           | /customers                           | 顧客情報の更新       |
|DELETE         | /customers/{id}                      | 指定したIDの顧客情報の論理削除 | 


### シーケンス図

```mermaid
sequenceDiagram
    actor User as レストラン経営者（システムユーザー）
    participant API as Spring Bootアプリケーション(API)
    participant DB as データベース

%% 顧客情報の取得
    User ->>+ API: GET /customers
    API ->> API: 顧客情報を検索
    API ->> DB: 検索条件に基づき顧客情報を取得
    DB -->> API: 顧客情報のリスト
    API ->> API: 顧客情報のレスポンス形式への変換
    API -->> User: HTTP 200 OK<br>顧客情報のリスト

%% 特定の顧客情報の取得
    User ->>+ API: GET /customers/{customerId}
    API ->> API: 顧客IDを使って情報を取得
    API ->> DB: 指定したIDの顧客情報を検索
    DB -->> API: 顧客情報
    API ->> API: 顧客情報のレスポンス形式への変換
    API -->> User: HTTP 200 OK<br>顧客情報

%% 顧客情報の登録
    User ->>+ API: POST /customers
    API ->> API: リクエストデータのバリデーション
    API ->> DB: INSERT INTO CUSTOMERS (氏名、連絡先、来店回数、好みなど)
    DB -->> API: 作成された顧客データ（IDを含む）
    API ->> API: 顧客データのレスポンス形式への変換
    API -->> User: HTTP 201 Created<br>作成された顧客情報を返却

%% 顧客情報の更新
    User ->>+ API: PUT /customers/{customerId}
    API ->> API: 更新データのバリデーション
    API ->> DB: UPDATE CUSTOMERS SET (更新情報) WHERE ID = {customerId}
    DB -->> API: 更新結果
    API ->> API: 更新結果のレスポンス形式への変換
    API -->> User: HTTP 200 OK<br>更新された顧客情報を返却

%% 顧客情報の削除
    User ->>+ API: DELETE /customers/{customerId}
    API ->> API: 顧客IDの確認
    API ->> DB: DELETE FROM CUSTOMERS WHERE ID = {customerId}
    DB -->> API: 削除結果
    API ->> API: 削除結果のレスポンス形式への変換
    API -->> User: HTTP 200 OK<br>削除成功

```


### 説明

---

### 1. **顧客情報の取得**

- **アクター（ユーザー）**:
    - レストラン経営者（システムユーザー）が、条件に基づいて顧客情報を取得するために、`GET /customers` APIを呼び出します。

- **API**:
    - APIはリクエストを受け取ると、顧客情報を検索する処理を開始します。検索条件に基づいて、データベースから顧客情報を取得します。

- **データベース**:
    - データベースは、顧客情報を検索し、その結果をAPIに返します。

- **API**:
    - APIは受け取った顧客情報をレスポンス形式（例えばJSON）に変換します。

- **アクター（ユーザー）**:
    - APIが顧客情報のリストを返すと、レストラン経営者はHTTPステータス`200 OK`とともに顧客情報を受け取ります。

---

### 2. **特定の顧客情報の取得**

- **アクター（ユーザー）**:
    - レストラン経営者は、特定の顧客情報を取得するために、`GET /customers/{customerId}` APIを呼び出します。ここで、`customerId`は取得したい顧客のIDです。

- **API**:
    - APIは`customerId`を使って、該当する顧客情報を取得します。

- **データベース**:
    - データベースは指定された`customerId`に一致する顧客情報を検索し、APIに返却します。

- **API**:
    - APIは取得した顧客情報をレスポンス形式に変換します。

- **アクター（ユーザー）**:
    - レストラン経営者は、特定の顧客情報とともにHTTPステータス`200 OK`を受け取ります。

---

### 3. **顧客情報の登録**

- **アクター（ユーザー）**:
    - レストラン経営者は、新しい顧客情報を登録するために、`POST /customers` APIを呼び出します。リクエストには顧客の氏名、連絡先、来店回数、好みなどが含まれます。

- **API**:
    - APIは受け取ったリクエストデータのバリデーションを行い、正しい形式であればデータベースに顧客情報を挿入します。

- **データベース**:
    - データベースは、新しい顧客情報を`CUSTOMERS`テーブルに挿入し、作成された顧客データ（顧客IDを含む）をAPIに返却します。

- **API**:
    - APIは挿入された顧客データをレスポンス形式に変換し、作成された顧客情報をレストラン経営者に返します。

- **アクター（ユーザー）**:
    - レストラン経営者は、HTTPステータス`201 Created`とともに、新しい顧客情報を受け取ります。このレスポンスにより、顧客情報が正常に作成されたことが確認できます。

---

### 4. **顧客情報の更新**

- **アクター（ユーザー）**:
    - レストラン経営者は、特定の顧客情報を更新するために、`PUT /customers/{customerId}` APIを呼び出します。`customerId`を指定し、更新したい情報をリクエストボディに含めます。

- **API**:
    - APIはリクエストデータのバリデーションを行い、正当な更新内容であればデータベースに反映させます。

- **データベース**:
    - データベースは、`customerId`に該当する顧客の情報を更新し、その結果をAPIに返却します。

- **API**:
    - APIは更新結果をレスポンス形式に変換し、更新された顧客情報を返します。

- **アクター（ユーザー）**:
    - レストラン経営者は、HTTPステータス`200 OK`とともに、更新された顧客情報を受け取ります。このレスポンスにより、顧客情報の更新が成功したことが確認できます。

---

### 5. **顧客情報の削除**

- **アクター（ユーザー）**:
    - レストラン経営者は、特定の顧客情報を削除するために、`DELETE /customers/{customerId}` APIを呼び出します。`customerId`を指定して、削除対象の顧客情報を指示します。

- **API**:
    - APIは、指定された`customerId`が有効かどうか確認し、その顧客情報をデータベースから削除します。

- **データベース**:
    - データベースは、指定された顧客情報を`CUSTOMERS`テーブルから削除し、その結果をAPIに返却します。

- **API**:
    - APIは削除結果をレスポンス形式に変換し、削除が成功した旨をレストラン経営者に返します。

- **アクター（ユーザー）**:
    - レストラン経営者は、HTTPステータス`200 OK`とともに、削除が成功したことを確認します。

---


### インフラ構成図

![](src/main/resources/images/infrastructure-diagram.drawio.svg)

___
## 力を入れたところ

1. **REST APIの設計と実装**
    - 顧客データの登録、検索、更新、削除といったCRUD機能をシンプルで直感的なAPIエンドポイントとして設計。
    - 特にURL設計において、操作内容が一目で理解できる構造にこだわりました。

2. **実用性を重視したデータベース設計**
    - 実際の顧客管理に必要な項目（氏名、連絡先、来店履歴、好みなど）を網羅的にカバーするER図を作成。
    - 外部キー制約やカスケード削除機能を組み込むことで、データ整合性を確保。

3. **クラウド環境へのデプロイ**
    - AWS（EC2, RDS, ALB）を利用した実務に近い環境を構築。
    - セキュリティグループやデプロイ手順を詳細に設計し、安定した運用が可能なシステムを実現。

4. **自動テストの導入**
    - JUnitを活用して単体テストを充実させ、コードの品質向上を追求。
    - テスト駆動開発（TDD）の考え方を取り入れ、バグの早期発見と迅速な修正を可能にしました。

5. **チーム開発を想定したツール活用**
    - GitHub ActionsによるCI/CDパイプラインの構築で、自動テスト・ビルド・デプロイを実現。
    - コードレビューの効率化を意識して、Postmanを活用。

---
## 今後の展望

1. **機能の拡張**
    - 現在は基本的な顧客管理機能に留まっていますが、以下の追加機能を検討中：
        - 顧客の購買パターンに基づいたレコメンド機能。
        - 来店頻度や好みをもとにしたカスタムプロモーションの通知機能。

2. **UI/UXの改善**
    - 現在はバックエンド中心の設計ですが、今後はフロントエンド（ReactやVue.js）を活用してユーザーフレンドリーな画面を提供。
    - レスポンシブデザインを採用し、スマートフォンやタブレットでの利用を可能にします。

3. **セキュリティの強化**
    - 顧客データを扱うため、セキュリティ対策をさらに強化。
        - JWT（JSON Web Token）を用いた認証・認可機能の実装。
        - SQLインジェクションやXSS攻撃への対策の徹底。

4. **オープンソース化**
    - このプロジェクトをGitHubでオープンソース化し、他の開発者からのフィードバックや貢献を受けられる仕組みを構築。
    - コミュニティを通じた成長を目指します。  