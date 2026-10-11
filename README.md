# Báo cáo: Khai báo GitHub Secrets và cấu hình Đăng nhập Container Registry

## 1. Mục tiêu
- Tạo tệp mã hóa an toàn (**Repository Secret**) lưu trữ Personal Access Token trên GitHub với tên biến `REGISTRY_TOKEN`.
- Cấu hình bước xác thực tự động vào **GitHub Container Registry (`ghcr.io`)** thông qua action chuẩn `docker/login-action@v3`.
- Đảm bảo tính bảo mật, không hardcode mật khẩu hoặc token cá nhân trong file cấu hình CI.

---

## 2. Quy trình thiết lập GitHub Secrets

### Bước 1: Khởi tạo Personal Access Token (Classic)
1. Truy cập: `GitHub Settings` -> `Developer Settings` -> `Personal access tokens` -> `Tokens (classic)`.
2. Tạo token mới với quyền hạn tối thiểu cần thiết:
   - `write:packages` (Cho phép tải lên container image vào GitHub Container Registry).
   - `read:packages` (Tự động cấp kèm).

---

### Bước 2: Thiết lập Repository Secrets
1. Vào repository của dự án trên GitHub: `Settings` -> `Secrets and variables` -> `Actions`.
2. Chọn **New repository secret**:
   - **Name**: `REGISTRY_TOKEN`
   - **Secret**: Dán token vừa tạo ở Bước 1.
3. Nhấp chọn **Add secret**.

```plaintext
+--------------------------------------------------------------------------+
| Repository Secrets (Settings -> Secrets and variables -> Actions)        |
+--------------------------------------------------------------------------+
| Name            | Added / Updated          | Environment                 |
| REGISTRY_TOKEN  | Just now                 | Repository-level Secret     |
+--------------------------------------------------------------------------+
```

---

## 3. Cấu hình luồng CI (`.github/workflows/ci.yml`)

```yaml
name: CI Workflow with Registry Authentication

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    name: Build & Test with Gradle
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Setup Java JDK 17 with Gradle Cache
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: 'gradle'

      - name: Make gradlew executable
        run: chmod +x ./gradlew

      - name: Run Unit Tests
        run: ./gradlew test --no-daemon

      - name: Log in to GitHub Container Registry
        uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.REGISTRY_TOKEN }}
```

---

## 4. Kết quả kiểm tra & Minh chứng

### 4.1. Nhật ký thực thi của bước `docker/login-action@v3`
Khi push code lên GitHub, mở chi tiết tab Actions:

```plaintext
Run docker/login-action@v3
  with:
    registry: ghcr.io
    username: dinhthanh143
    password: ***
    logout: true
Logging into ghcr.io...
Login Succeeded
```

---

## 5. Kết luận
- Bước đăng nhập xác thực thành công với thông báo `Login Succeeded`.
- Bí mật `REGISTRY_TOKEN` được GitHub tự động mask (hiển thị dưới dạng `***`), bảo vệ an toàn danh tính và bảo mật hệ thống.
