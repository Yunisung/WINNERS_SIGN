# 위너스사인( Winners Sign )

위너스사인은 안드로이드용 계약 관리 애플리케이션 입니다. Android Gradle Plugin을 사용해 빌드되며, 최소 SDK 14부터 대상 SDK 35까지 지원하도록 구성되어 있습니다.

## 주요 구성
- **모듈**: `app` 모듈 하나로 구성되어 있으며, 패키지 네임스페이스는 `contract.hee.bukook`입니다.
- **언어 및 빌드 설정**: Java 11 소스/타겟 호환성으로 설정되어 있으며, `compileSdkVersion` 35를 사용합니다.
- **빌드 타입**: `debug`와 `release` 빌드 타입을 제공합니다. `release` 빌드는 ProGuard 기본 설정 파일과 `proguard-rules.pro`를 사용하며 사전 설정된 서명 구성을 참조합니다.
- **주요 의존성**:
  - AndroidX AppCompat, Material Components, ConstraintLayout
  - Retrofit2와 Gson 컨버터, OkHttp 및 로깅 인터셉터
  - Lombok(compileOnly/annotationProcessor), Gson, PhotoView
  - 이메일 발송 관련 `activation.jar`, `additionnal.jar`, `mail.jar` 라이브러리

## 기술 스택
- **플랫폼/언어**: Android, Java 11
- **네트워크**: Retrofit2 + OkHttp(로깅 인터셉터 포함), Gson 직렬화
- **UI**: AndroidX AppCompat, Material Components, ConstraintLayout, PhotoView
- **빌드/관리**: Gradle(Wrapper 포함), ProGuard/R8, Lombok(컴파일 시간)

## 아키텍처
- **단일 모듈 구성**: `app` 모듈 하나로 구성되어 있으며, 네임스페이스별 패키지(`find`, `contract`, `mbinfo` 등)로 기능을 분리했습니다.
- **레이어 구성**:
  - **네트워크 계층**: `contract.hee.bukook.db` 패키지에서 OkHttp와 Retrofit 기반 API 호출을 정의하고, Gson을 사용해 요청/응답을 직렬화합니다.
  - **데이터 모델**: `contract.hee.bukook.bean` 패키지의 DTO/엔티티 클래스가 서버 응답을 매핑합니다.
  - **화면/기능**: `contract.hee.bukook.contract`, `find`, `mbinfo`, `join` 등 기능별 패키지에서 Activity 및 View 관련 로직을 관리합니다.
- **서버 통신 흐름**: UI에서 데이터 요청 → `db` 패키지의 API 호출 → 비동기 콜백에서 결과 처리 및 UI 업데이트.

## 빌드 방법
Gradle 래퍼 스크립트를 사용해 프로젝트를 빌드하거나 테스트할 수 있습니다.

```bash
./gradlew assembleDebug    # 디버그 빌드 생성
./gradlew test             # 단위 테스트 실행
```