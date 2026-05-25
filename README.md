# Honey Stock - 꿀 재고 및 판매 관리 시스템

Spring Boot 기반의 꿀 상품 재고/판매 관리 웹 애플리케이션입니다.  
상품, 창고, 재고, 판매 내역을 관리하고 대시보드에서 주요 현황을 확인할 수 있도록 구현했습니다.

## 주요 기능

### 대시보드
- 상품 수, 창고 수, 재고 항목 수, 판매 건수 조회
- 재고 부족 상품 수 표시
- 최근 판매 내역 5건 조회

### 상품 관리
- 상품 등록
- 상품 목록 조회
- 상품 수정
- 상품 삭제

### 창고 관리
- 창고 등록
- 창고 목록 조회
- 창고 수정
- 창고 삭제

### 재고 관리
- 상품별/창고별 재고 등록
- 재고 목록 조회
- 상품명, 창고명, 지역 검색
- 재고 수량 수정
- 재고 상태 표시
  - 품절
  - 부족
  - 정상
- 재고 부족 목록 조회

### 판매 관리
- 판매 등록
- 판매 등록 시 재고 자동 차감
- 재고 부족 시 판매 방지
- 판매 취소 시 재고 복구
- 판매 내역 조회
- 날짜별 판매 조회
- 상품명, 창고명, 지역, 메모 검색
- 총 판매수량 및 총 매출 표시

## 기술 스택

- Java 21
- Spring Boot
- Spring Data JPA
- Thymeleaf
- H2 Database
- Gradle
- Bootstrap 5
- Docker

## 프로젝트 구조

```text
src/main/java/com/example/honey_stock
├── controller
├── entity
├── repository
└── service
주요 구현 포인트
Controller, Service, Repository 계층 분리 시작
Spring Data JPA 메서드 쿼리를 활용한 검색 기능 구현
판매 등록 시 현재 재고를 검증하여 음수 재고 방지
판매 취소 시 기존 판매 수량만큼 재고 복구
대시보드를 통해 관리자가 주요 현황을 한눈에 확인 가능
Bootstrap을 활용해 반응형 UI 구성
실행 방법
./gradlew bootRun

Windows PowerShell:

.\gradlew bootRun

접속 주소:

http://localhost:8080
향후 개선 예정
상품별 판매 통계 그래프
월별 매출 통계
엑셀 다운로드
로그인 및 관리자 권한
재고 입출고 이력 관리
