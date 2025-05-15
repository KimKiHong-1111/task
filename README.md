# 백엔드 개발 과제 (Java)

**과제 : Spring Boot 기반 JWT 인증/인가 및 AWS 배포**

1️⃣ **Spring Boot**를 이용하여 JWT 인증/인가 로직과 API를 구현한다.

2️⃣ **Junit** 기반의 테스트 코드를 작성한다.

3️⃣ **Swagger** 로 API를 문서화 한다.

4️⃣ 애플리케이션을 **AWS EC2**에 배포하고, 실제 환경에서 실행되도록 구성한다.


**구현 포인트**

- [x]  유저(User) 및 관리자(Admin) 회원가입, 로그인 API를 개발합니다.
- [x]  JWT를 이용하여 Access Token을 발급하고 검증하는 로직을 적용합니다.
- [x]  일반 사용자(User)와 관리자(Admin) 역할(Role)을 구분하여 특정 API 접근을 제한합니다.

**구현 포인트**

- [x]  AWS EC2 인스턴스를 생성하고 기본 환경을 설정합니다. (보안 그룹, JDK 설치 등)
- [x]  다음 두 가지 방법 중 하나를 선택하여 애플리케이션을 배포합니다.
    - 방법 1: 소스코드를 EC2로 가져와서 빌드하기
        - Git을 통해 소스코드를 EC2에 클론합니다.
        - Maven/Gradle을 사용하여 프로젝트를 빌드하고 JAR 파일을 생성합니다.
    - 방법 2: 빌드된 JAR 파일 직접 업로드하기
        - 로컬에서 빌드한 JAR 파일을 EC2 인스턴스로 전송합니다.
- [x]  애플리케이션을 실행하고 외부에서 접근 가능하도록 구성합니다. (java -jar 명령어 사용)
- [x]  (선택 사항) Nginx를 리버스 프록시로 설정하여 요청을 애플리케이션으로 전달합니다.

1. 회원가입 (Sign Up)
URL: /signup

Method: POST

Description: 새로운 사용자를 회원가입시킵니다.

Request Body (JSON):


    {
      "username": "string",      // 필수, 사용자 아이디(이메일 등)
      "password": "string",      // 필수, 비밀번호
      "nickname": "string"       // 필수, 닉네임
    }
Response (201 Created):


    {
      "username": "string",
      "nickname": "string",
      "roles": ["USER"]          // 기본 역할은 USER
    }
Error Responses:
400 Bad Request: 입력값 누락 또는 유효하지 않은 경우
409 Conflict: 이미 존재하는 사용자 아이디

2. 로그인 (Sign In)

URL: /signin

Method: POST

Description: 이메일과 비밀번호로 로그인합니다. 응답 헤더에 JWT 토큰이 포함됩니다.

Request Body (JSON):

    {
      "username": "string",      // 필수, 사용자 아이디
      "password": "string"       // 필수, 비밀번호
    }
   
Response (201 Created):
Body:
    {
      "token": "JWT_ACCESS_TOKEN_STRING"
    }
   
Headers:
Authorization: JWT_ACCESS_TOKEN_STRING

Error Responses:
401 Unauthorized: 인증 실패 (아이디 또는 비밀번호 불일치)

4. 관리자 권한 부여 (Grant Admin Role)
URL: /admin/users/{userId}/roles

Method: PATCH

Description: 특정 사용자에게 관리자 권한을 부여합니다. 이 API는 ADMIN 권한이 있는 사용자만 접근 가능합니다.

Path Parameter:

userId (Long): 권한을 부여할 사용자 ID

Response (200 OK):


    {
      "username": "string",
      "nickname": "string",
      "roles": [
        {"role": "USER"},
        {"role": "ADMIN"}
      ]
    }
Error Responses:

403 Forbidden: 권한 없음 (ADMIN 권한이 없는 경우)

404 Not Found: 해당 사용자 없음
