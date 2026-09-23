# PTPMHDV Shop - Ung dung Web ban thiet bi dien tu

Kien truc **Microservices** + **API Gateway**, xay dung bang Spring Boot 3 / Spring Cloud 2023,
gom 8 module Maven:

| Module | Vai tro | Cong |
|---|---|---|
| `common` | Thu vien dung chung (JWT, xu ly loi) - khong chay rieng | - |
| `eureka-server` | Service Discovery (Netflix Eureka) | 8761 |
| `api-gateway` | Cong vao duy nhat, dinh tuyen toi cac service | 8080 |
| `user-service` | Dang ky / dang nhap / JWT / ho so nguoi dung | 8081 |
| `product-service` | San pham & danh muc | 8082 |
| `order-service` | Gio hang & don hang (goi Feign toi product-service, payment-service) | 8083 |
| `payment-service` | Thanh toan (gia lap) & thong bao | 8084 |
| `frontend` | Giao dien nguoi dung (HTML/CSS/JS thuan) | 8090 |

Moi service dung mot **database MySQL rieng** (dung mo hinh "database per service": `user_db`,
`product_db`, `order_db`, `payment_db`), tu dong tao database + bang va nap du lieu mau khi khoi
dong lan dau (nho `ddl-auto: update` cua Hibernate va tham so `createDatabaseIfNotExist=true`).
Cac file SQL xuat san tu database that nam trong thu muc [`database/`](database/) (xem
[database/README.md](database/README.md)) - dung de nop bao cao hoac xem truoc schema trong MySQL
Workbench.

## Yeu cau

- JDK 17+
- IntelliJ IDEA (Community hoac Ultimate deu duoc). IntelliJ da kem san Maven nen khong bat buoc
  cai Maven rieng.
- **MySQL Server 8.0 dang chay tai `localhost:3306`** (vi du qua MySQL Workbench / MySQL
  Installer). Tai khoan mac dinh dung trong project: user `root`, mat khau `12345678` - neu may
  ban dat mat khau MySQL khac, xem muc "Cau hinh mat khau MySQL" ben duoi.

## Cach mo va chay trong IntelliJ IDEA

1. **File > Open...** chon thu muc `C:\ptpmhdv` (thu muc chua file `pom.xml` goc). IntelliJ se tu
   nhan day la du an Maven multi-module va tai (import) toan bo 8 module.
2. Doi cho thanh Maven ben phai import xong (lan dau se tai thu vien tu Maven Central, can mang
   internet, mat vai phut).
3. Chay lan luot theo **dung thu tu sau** (moi service la mot ung dung Spring Boot doc lap - bam
   nut Run ▶ tren class `xxxApplication.java` cua tung module, hoac tao Run Configuration rieng):

   | Thu tu | Class can chay |
   |---|---|
   | 1 | `eureka-server/.../EurekaServerApplication` |
   | 2 | `user-service/.../UserServiceApplication` |
   | 3 | `product-service/.../ProductServiceApplication` |
   | 4 | `payment-service/.../PaymentServiceApplication` |
   | 5 | `order-service/.../OrderServiceApplication` |
   | 6 | `api-gateway/.../ApiGatewayApplication` |
   | 7 | `frontend/.../FrontendApplication` |

   Nen doi khoang 5-10s giua buoc 1 va cac buoc con lai de Eureka khoi dong xong. Tu buoc 2-6 co
   the chay gan nhu dong thoi (moi service tu dang ky vao Eureka khi khoi dong).

4. Mo trinh duyet vao **http://localhost:8090** de su dung ung dung.
5. (Tuy chon) Xem trang dieu khien Eureka tai **http://localhost:8761** de kiem tra cac service
   da dang ky day du (USER-SERVICE, PRODUCT-SERVICE, ORDER-SERVICE, PAYMENT-SERVICE,
   API-GATEWAY).

> Meo: co the tao mot **Compound Run Configuration** trong IntelliJ (Run > Edit Configurations >
> + > Compound) gom ca 7 configuration tren de bam Run mot lan la khoi dong toan bo he thong.

## Cau hinh mat khau MySQL

Neu mat khau tai khoan `root` MySQL cua ban khac `12345678`, sua truc tiep trong file
`application.yml` cua **4 service** (`user-service`, `product-service`, `order-service`,
`payment-service`), dong `password: ${DB_PASSWORD:12345678}` - thay `12345678` bang mat khau
thuc te. Hoac khong sua code: dat bien moi truong `DB_PASSWORD` truoc khi chay (trong IntelliJ:
Run Configuration > Environment variables > them `DB_PASSWORD=mat_khau_cua_ban`).

Cac database (`user_db`, `product_db`, `order_db`, `payment_db`) se **tu duoc tao** khi service
tuong ung ket noi lan dau - khong can tao thu cong trong MySQL Workbench (tru khi ban muon chay
san file SQL trong thu muc `database/` - xem [database/README.md](database/README.md)).

## Tai khoan demo (duoc tao san khi user-service khoi dong lan dau)

| Tai khoan | Mat khau | Vai tro |
|---|---|---|
| `admin` | `admin123` | ADMIN - vao duoc trang `/admin.html` de quan ly san pham, danh muc, don hang |
| `khachhang` | `123456` | USER - mua hang binh thuong |

Ban cung co the bam **Dang ky** tren giao dien de tao tai khoan moi.

## Luong nghiep vu chinh

1. Nguoi dung duyet san pham (tim kiem, loc theo danh muc) tren trang chu - khong can dang nhap.
2. Dang nhap / dang ky -> nhan JWT, luu trong `localStorage` cua trinh duyet.
3. Them san pham vao gio hang (`order-service` goi sang `product-service` de kiem tra ton kho).
4. Thanh toan (`checkout`): `order-service` tao don hang, tru ton kho ben `product-service`, goi
   `payment-service` de ghi nhan thanh toan (gia lap - COD thi cho "Cho xu ly", cac hinh thuc
   khac tinh la thanh cong ngay) va sinh thong bao cho nguoi dung.
5. Xem lich su don hang & thong bao tai `/orders.html` va `/notifications.html`.
6. Quan tri vien (`admin`) quan ly san pham/danh muc/don hang tai `/admin.html`.

## Kien truc bao mat

- `user-service` phat hanh **JWT (HS256)** khi dang nhap/dang ky thanh cong.
- Tat ca service dung chung mot **khoa bi mat** (`jwt.secret` trong `application.yml` cua tung
  service) de tu xac thuc token ma khong can goi nguoc ve `user-service` (mo hinh stateless).
  Day la don gian hoa hop ly cho pham vi do an; trong he thong thuc te co the thay bang
  RSA public/private key hoac mot Auth Server rieng (OAuth2/OIDC).
- `order-service` khi goi Feign sang `product-service`/`payment-service` se **chuyen tiep header
  `Authorization`** cua nguoi dung dang dang nhap, de cac service do xac dinh dung nguoi dang thuc
  hien hanh dong.
- Endpoint noi bo `/api/products/internal/**` (product-service goi boi order-service khi checkout)
  duoc mo cong khai trong pham vi mang noi bo (khong yeu cau JWT) vi day la thao tac he thong
  (tru kho), khong gan voi quyen cua mot nguoi dung cu the.

## Xu ly su co thuong gap

- **"Khong the ket noi toi API Gateway"** khi mo trang web: kiem tra da chay du
  `eureka-server` + 4 service backend + `api-gateway` va cho vai giay de dang ky xong voi Eureka
  (xem tai http://localhost:8761).
- **Cong (port) bi trung**: neu may ban dang co ung dung khac dung cong 8080-8090 hoac 8761, doi
  cong trong file `application.yml` tuong ung roi cap nhat lai route trong
  `api-gateway/src/main/resources/application.yml`.
- **Loi ket noi MySQL** (`Communications link failure`, `Access denied for user 'root'`...): kiem
  tra MySQL Server dang chay (Windows: Services > `MySQL80` phai o trang thai Running) va mat
  khau trong `application.yml` khop voi mat khau `root` that su cua ban (xem muc "Cau hinh mat
  khau MySQL" o tren).
- **Xem du lieu truc tiep**: mo MySQL Workbench, ket noi `localhost:3306` bang tai khoan `root`,
  se thay 4 database `user_db`, `product_db`, `order_db`, `payment_db` sau khi cac service da
  chay it nhat mot lan.
