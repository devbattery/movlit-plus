# MovLit (Movie & Literature) - 사용자 맞춤 영화, 책 추천과 채팅 제공 서비스

## 🕸️ 시스템 아키텍처

<img src="https://github.com/user-attachments/assets/96b372dd-52ee-4481-96d1-fba414443c4a" alt="MovLit System Architecture Diagram">

## 📄 프로젝트 개요

* **통합 콘텐츠 제공:** 영화와 도서 정보를 통합하여 검색하고, 사용자 취향에 맞는 맞춤 데이터를 제공합니다.
* **실시간 상호작용:** 실시간 데이터 처리 기술(WebSocket, SSE, Redis Pub/Sub)을 활용하여 사용자 간의 원활한 상호작용(채팅, 알림 등)을 지원합니다.

## 🧑🏻‍💻 프로젝트 인원 및 기간

* **개발 인원**: 4명 (Frontend 및 Backend 개발 동시 담당)
* **프로젝트 기간**: `2024.12.17 ~ 2025.02.13` (총 58일)

## 🎥 핵심 기능

<details>
<summary><h3>✅ API 문서화 (Spring REST Docs & Rest Assured)</h3></summary>

- Rest Assured를 이용한 API 테스트 코드 작성을 기반으로 Spring REST Docs와 연동하여 API 문서를 자동화했습니다.

  <img src="https://github.com/user-attachments/assets/8577372b-633b-4e48-bda3-54d269e5a6cb" style="width: 600px" alt="API Documentation Example">

</details>

<details>
<summary><h3>👤 회원 관리 (가입, 일반 로그인, OAuth2 로그인)</h3></summary>

- **회원가입:** 이메일, 비밀번호, 닉네임 기반의 자체 회원가입 기능을 제공합니다.

  <img src="https://github.com/user-attachments/assets/82e9f940-3f51-43bb-bef0-bf4246dbbb7a" style="width: 600px" alt="Signup Form">

<hr/>

- **로그인:** 자체 계정 또는 소셜 계정(OAuth2)을 통한 로그인을 지원합니다.

  <img src="https://github.com/user-attachments/assets/4935e3f9-1f4f-40af-95a9-4fe5a8b272da" style="width: 600px" alt="Login Form">

<hr/>

- **OAuth2 로그인:** Google, Kakao 등 소셜 로그인을 지원하여 사용자 편의성을 높였습니다.

  <img src="https://github.com/user-attachments/assets/34d361fb-f748-4d2e-850f-7a76dc0a4153" style="width: 600px" alt="OAuth2 Login Example">

</details>

<details>
<summary><h3>🏠 메인 화면 - 카테고리별 영화/도서 목록 제공</h3></summary>

- 사용자의 선호도와 최신 트렌드를 반영하여 영화 및 도서 콘텐츠 목록을 카테고리별로 제공합니다.
- **메인 화면 (영화):**

  <img src="https://github.com/user-attachments/assets/c86c38e8-4812-427e-950f-42fdb85cdd11" style="width: 600px" alt="Main Screen - Movies">

<hr/>

- **메인 화면 (도서):**

  <img src="https://github.com/user-attachments/assets/99e01ff5-b105-4c0b-98cd-224ee0070b02" style="width: 600px" alt="Main Screen - Books">

</details>

<details>
<summary><h3>ℹ️ 상세 화면 - 평점, 코멘트, 찜, 유사 콘텐츠 추천</h3></summary>

- **콘텐츠 상세 정보:** 각 영화/도서의 상세 정보, 사용자 평점 및 코멘트, 관련 콘텐츠(다른 책/영화) 목록을 제공합니다.
- **도서 상세 정보:**

  <img src="https://github.com/user-attachments/assets/a9a6668b-0632-4445-b92f-703b880e30e1" style="width: 600px" alt="Book Detail Screen">

<hr/>

- **평점 및 코멘트:** 사용자는 콘텐츠에 대한 평점과 코멘트를 남기고 조회할 수 있습니다.

  <img src="https://github.com/user-attachments/assets/93140632-99de-4166-b4ef-384bfa25bb73" style="width: 600px" alt="Rating and Comments Form (Book)">

<hr/>

- **영화 상세 정보:**

  <img src="https://github.com/user-attachments/assets/9ac75e7f-9782-46cd-a63b-8cf710e015a1" style="width: 600px" alt="Movie Detail Screen">

<hr/>

- **평점 및 코멘트:**

  <img src="https://github.com/user-attachments/assets/79539d4b-16ed-473a-aeae-4dc15e737690" style="width: 600px" alt="Rating and Comments Form (Movie)">

</details>

<details>
<summary><h3>🔍 통합 검색 - 다양한 조건 검색 및 유사 콘텐츠 제공</h3></summary>

- 콘텐츠의 제목, 장르, 카테고리, 배우/작가, 감독/출판사 등 다양한 기준으로 통합 검색 기능을 제공하며, 검색 결과와 함께 유사한 콘텐츠를 추천합니다.

  <img src="https://github.com/user-attachments/assets/2e66d8b0-9f04-4ecb-bdb7-ffe42a01bcfa" style="width:600px" alt="Integrated Search Results">

</details>

<details>
<summary><h3>👤 프로필 페이지 - 팔로우, 정보 수정, 맞춤 콘텐츠</h3></summary>

- 사용자는 다른 사용자를 팔로우/언팔로우하고, 자신의 프로필 정보(닉네임, 프로필 사진 등)를 수정할 수 있습니다.
- 사용자의 활동(찜 목록, 평점 등)을 기반으로 맞춤형 콘텐츠를 추천받을 수 있습니다.

  <img src="https://github.com/user-attachments/assets/8f440bfd-d64c-4e43-8329-5502c0b0b062" style="width:600px" alt="User Profile Page">

</details>

<details>
<summary><h3>💬 1:1 채팅 (DM) - 사용자 간 실시간 다이렉트 메시지</h3></summary>

- **채팅방 생성:** 다른 사용자의 프로필 페이지에서 DM 보내기 버튼을 통해 1:1 채팅방을 생성하고 메시지를 전송할 수 있습니다.

  <img src="https://github.com/user-attachments/assets/b3c1e0c9-b985-4b24-af52-5c1cde01ae55" style="width:600px" alt="Starting a 1:1 Chat">

<hr/>

- **실시간 프로필 업데이트:** 채팅 중 상대방의 프로필 사진이 변경되면 실시간으로 채팅방에 반영됩니다.

  <img src="https://github.com/user-attachments/assets/7cd08d50-8ec1-4f38-88dc-35eb4c67bdf7" style="width:600px" alt="Real-time Profile Update in Chat">

<hr/>

- **새 메시지 알림:** 1:1 채팅 메시지를 받으면 실시간 알림(SSE)을 통해 사용자에게 알려줍니다.

  <img src="https://github.com/user-attachments/assets/505fea8c-7cbd-4eac-b7e1-a35a73763797" style="width:600px" alt="1:1 Chat Notification">

</details>

<details>
<summary><h3>👥 그룹 채팅 - 콘텐츠 기반 그룹 채팅 및 실시간 교류</h3></summary>

- **채팅방 검색 및 생성/참여:** 특정 영화나 책에 대한 그룹 채팅방을 검색하여 참여하거나, 없는 경우 직접 생성할 수 있습니다.

  <img src="https://github.com/user-attachments/assets/f24799ba-e133-4824-9858-7fa50ed64abd" style="width:600px" alt="Group Chat Search and Creation">

<hr/>

- **실시간 그룹 채팅:** WebSocket을 통해 그룹 채팅방 내 사용자들과 실시간으로 의견을 교환할 수 있습니다.

  <img src="https://github.com/user-attachments/assets/48cbec83-32e2-480d-88d7-71ef0cf99c47" style="width:600px" alt="Real-time Group Chat">

<hr/>

- **실시간 프로필 업데이트:** 그룹 채팅 중 사용자의 프로필 정보(사진, 닉네임)가 변경되면 실시간으로 반영됩니다.

  <img src="https://github.com/user-attachments/assets/65d61216-23da-4f51-952e-437f7cbf5adb" style="width:600px" alt="Real-time Profile Update in Group Chat">

<hr/>

- **채팅방 나가기:** 참여 중인 그룹 채팅방에서 나갈 수 있습니다.

  <img src="https://github.com/user-attachments/assets/62f28b53-feb7-4f45-81fa-6bbc37848107" style="width:600px" alt="Leaving Group Chat">

</details>

<details>
<summary><h3>🔔 사용자 알림 (SSE) - 주요 이벤트 실시간 알림</h3></summary>

- SSE(Server-Sent Events)를 활용하여 팔로우, 찜한 콘텐츠의 그룹챗 생성, 새 메시지(1:1, 그룹) 수신 등 주요 이벤트 발생 시 사용자에게 실시간 알림을 제공합니다 (브라우저 알림 및 페이지
  내 알림 목록).

  <img src="https://github.com/user-attachments/assets/cb18199a-8ed0-49eb-8515-8cda7d2e0503" style="width:600px" alt="Real-time Notifications List">

</details>

## 💎 핵심 구현 (Technical Highlights)

<details>
<summary><h3> 1. 그룹 채팅방 생성 동시성 제어 (Redis Queue & Worker Thread)</h3></summary>

**문제점:** 특정 콘텐츠(영화/책)당 하나의 그룹 채팅방만 허용하는 정책에서, 인기 콘텐츠의 경우 다수 사용자가 동시에 채팅방 생성을 요청하면 여러 개의 채팅방이 생성될 수 있는 동시성 문제가 발생합니다.

**해결 방안:** Redis의 List를 Queue로 활용하고 별도의 Worker Thread Pool을 사용하여 채팅방 생성 요청을 비동기적으로 순차 처리합니다.

**주요 처리 과정:**

1. **요청 큐잉:** 채팅방 생성 요청이 들어오면, 해당 콘텐츠 ID를 Key로 하는 Redis List에 요청 정보(사용자 ID 등)를 `leftPush`합니다.
2. **비동기 처리 (Worker):** 별도의 Worker 스레드가 주기적으로 `rightPop` (Blocking 방식: `brpop`)을 호출하여 Redis Queue에서 가장 오래된 요청을 하나씩
   가져옵니다.
3. **작업 위임:** 가져온 요청 정보를 바탕으로 실제 채팅방 생성 로직(DB 확인 및 생성)을 수행합니다. `ThreadPoolExecutor`와 `Callable`을 사용하여 비동기 실행 및 결과(
   Future) 관리를 합니다.
4. **타임아웃 및 예외 처리:** `Future.get()`에 타임아웃(예: 30초)을 설정하여 작업 지연을 방지하고, `InterruptedException`, `ExecutionException`,
   `TimeoutException` 발생 시 적절한 예외(`GroupChatroomCreationWhenWorkingException`)를 던져 문제를 인지시킵니다.

**기대 효과:** 한 번에 하나의 스레드만 특정 콘텐츠의 채팅방 생성 로직에 접근하게 하여 동시성 문제를 해결하고, 요청 처리를 비동기화하여 웹 서버의 부하를 줄입니다.

**관련 이미지:**

<img width="1210" alt="Worker Class Code Snippet" src="https://github.com/user-attachments/assets/d569a81d-b1e3-43f2-b296-551111229f1d">

<img width="1416" alt="Queue Processing Logic" src="https://github.com/user-attachments/assets/507953fb-8d63-4a84-a6ef-5efe3373c2ac">

</details>

<details>
<summary><h3> 2. 실시간 메시지 전송 아키텍처 (WebSocket & Redis Pub/Sub)</h3></summary>

**목표:** 사용자 간의 채팅 메시지를 실시간으로 전달합니다.

**구현:** WebSocket 연결을 통해 클라이언트와 서버 간의 양방향 통신 채널을 유지하고, Redis의 Pub/Sub 기능을 메시지 브로커로 활용합니다.

**메시지 흐름:**

1. **발행 (Publish):** 클라이언트가 WebSocket을 통해 메시지를 보내면, 해당 메시지를 수신한 서버 인스턴스는 메시지를 가공 후 특정 Redis 토픽(채팅방 ID 기반)으로 발행합니다. (사용
   토픽 예: 채팅 메시지, 채팅방 정보 업데이트, 사용자 상태 변경 등)
2. **구독 (Subscribe):** 모든 서버 인스턴스는 관련 Redis 토픽들을 구독(Subscribe)하고 있습니다.
3. **전파 (Broadcast):** 메시지가 발행되면, Redis는 이를 구독 중인 모든 서버 인스턴스에게 전달합니다. 각 서버 인스턴스는 전달받은 메시지를 자신이 관리하는 WebSocket 클라이언트 중 해당
   채팅방에 참여 중인 클라이언트에게 전송합니다.

**장점:** 서버 인스턴스가 여러 대로 확장(Scale-out)되어도 Redis Pub/Sub을 통해 모든 클라이언트에게 메시지를 안정적으로 전파할 수 있습니다.

**관련 이미지:**

<img width="1152" alt="Redis Pub/Sub Messaging Flow Diagram" src="https://github.com/user-attachments/assets/74068e47-3d14-404f-bee0-3e8b18454f67">

</details>

<details>
<summary><h3> 3. 채팅 메시지 영구 저장 및 처리 (Redis Stream & MongoDB)</h3></summary>

**목표:** 실시간 메시지 전송(Pub/Sub)과 별개로, 모든 채팅 메시지를 안정적으로 영구 저장하고 후처리(DB 저장)합니다.

**구현:** Redis Stream을 메시지 큐로 사용하고, 별도의 Consumer Group이 메시지를 비동기적으로 처리하여 MongoDB에 저장합니다.

**처리 과정:**

1. **메시지 전송 (Pub/Sub):** 사용자가 메시지를 보내면, 우선 Redis Pub/Sub을 통해 다른 사용자들에게 실시간으로 전달됩니다 (UI 업데이트 목적).
2. **스트림 추가 (XADD):** 동시에, 해당 메시지 데이터를 Redis Stream에 추가합니다 (`XADD` 명령어).
3. **비동기 처리 (Consumer Group):** 별도의 Consumer Group에 속한 Consumer(Listener)들이 Stream에서 메시지를 읽어옵니다 (`XREADGROUP`). Consumer
   Group을 사용하면 여러 Consumer가 메시지를 중복 없이 분산 처리할 수 있습니다.
4. **영구 저장 (MongoDB):** Consumer는 읽어온 메시지를 MongoDB에 저장합니다.
5. **처리 확인 (XACK):** 메시지 처리가 성공적으로 완료되면 `XACK` 명령어를 통해 Redis Stream에 해당 메시지가 처리되었음을 알립니다. 이를 통해 메시지 유실 방지 및 장애 발생 시 재처리
   기반을 마련합니다.

**Redis Stream 사용 이유:**

* **메시지 신뢰성:** `ACK` 메커니즘을 통해 메시지 처리 보장을 지원합니다.
* **Consumer Group:** 여러 Consumer가 병렬로 메시지를 처리하여 처리량을 높이고, 특정 Consumer 장애 시 다른 Consumer가 이어서 처리할 수 있습니다.
* **비동기 처리:** 메시지 저장 로직을 별도의 Consumer Thread/Process에서 비동기적으로 처리하여, 실시간 메시지 전송 경로의 성능 영향을 최소화하고 시스템 확장을 용이하게 합니다.
* **관심사 분리:** 실시간 전송(Pub/Sub)과 영구 저장(Stream) 파이프라인을 분리하여 시스템 복잡도를 관리합니다.

**관련 이미지:**

<img width="1278" alt="Redis Stream Message Processing Flow Diagram" src="https://github.com/user-attachments/assets/2ce8800a-0103-4925-b980-9cef729352e2">

<img width="1416" alt="Redis Stream Consumer Code Snippet" src="https://github.com/user-attachments/assets/844591d9-6366-466c-9e32-4ae4db3a5166">

<img width="1369" alt="Stream Listener Implementation Snippet" src="https://github.com/user-attachments/assets/75b70d01-740e-44d2-92ad-a8086af1454f">

</details>

<details>
<summary><h3> 4. 그룹 채팅방 멤버 정보 캐싱 (Redis Cache)</h3></summary>

**문제점:** 그룹 채팅방 입장 시 또는 채팅 중 멤버 목록 조회가 빈번하게 발생하여 RDB에 부하를 줄 수 있습니다.

**해결 방안:** Redis를 캐시 저장소로 사용하여 그룹 채팅방의 멤버 정보를 캐싱합니다.

**처리 과정:**

1. **캐시 조회:** 멤버 정보 요청 시, 먼저 Redis에서 해당 채팅방의 멤버 목록 캐시 데이터를 조회합니다.
2. **Cache Hit:** 캐시 데이터가 존재하면 즉시 반환합니다.
3. **Cache Miss:** 캐시 데이터가 없으면 RDB에서 멤버 목록을 조회합니다.
4. **캐시 저장:** RDB에서 조회한 데이터를 Redis에 저장(캐싱)한 후, 클라이언트에게 반환합니다. 이후 동일한 요청은 캐시에서 처리됩니다.

**기대 효과:** DB 조회 빈도를 줄여 RDB 부하를 감소시키고, 멤버 목록 조회 응답 속도를 향상시킵니다.

**관련 이미지:**

<img width="1245" alt="Group Chat Member Caching Flow Diagram" src="https://github.com/user-attachments/assets/9224e9c3-6ddc-4601-ba88-5ba781928a63">

</details>

<details>
<summary><h3> 5. 캐싱을 통한 성능 개선 확인</h3></summary>

- 멤버 정보 조회 시 RDB 직접 조회는 평균 수십 밀리초(ms)가 소요되었으나, Redis 캐시를 적용한 후 평균 한 자릿수 밀리초로 응답 시간이 단축되어 성능 개선 효과를 확인했습니다.

**관련 이미지:**

<img width="517" alt="Performance Comparison: DB vs Cache" src="https://github.com/user-attachments/assets/92afb98c-d425-4f75-a63a-bd2c924df450">

</details>

<details>
<summary><h3> 6. 1:1 채팅방 목록 캐싱 (Redis Cache)</h3></summary>

**배경:** 사용자의 1:1 채팅방 목록은 자주 변경되지 않는 데이터(주로 추가만 발생)이므로 캐싱에 적합합니다.

**구현:** 그룹 채팅방 멤버 캐싱과 유사하게, 사용자의 1:1 채팅방 목록을 Redis에 캐싱하여 조회 성능을 향상시킵니다.

**처리 과정 (조회 시):**

1. Redis 캐시에서 해당 사용자의 채팅방 목록 조회 시도.
2. Cache Hit 시 즉시 반환.
3. Cache Miss 시 RDB에서 조회 후 Redis에 캐싱하고 반환.

**관련 이미지:**

<img width="1283" alt="1:1 Chat List Caching Flow Diagram" src="https://github.com/user-attachments/assets/57862678-d57d-410b-ba94-8de8d910d513">

</details>

<details>
<summary><h3> 7. 1:1 채팅방 생성 시 캐시 업데이트</h3></summary>

**시나리오:** 사용자가 상대방에게 처음으로 1:1 메시지를 보낼 때 채팅방이 생성됩니다.

**처리 과정:**

1. 최초 메시지 전송 요청 시, 1:1 채팅방 정보를 RDB에 저장합니다.
2. 동시에, 해당 사용자의 채팅방 목록 Redis 캐시를 갱신(무효화 또는 추가)합니다.
3. Redis Pub/Sub을 통해 채팅방 생성 및 새 메시지 도착 이벤트를 발행하여, 관련 클라이언트(본인 및 상대방)의 UI가 업데이트되도록 합니다.

**관련 이미지:**

<img width="1106" alt="1:1 Chat Cache Update on Creation Flow Diagram" src="https://github.com/user-attachments/assets/0ba0f62c-e8ec-4d0c-bf4b-613c4ba35df0">

</details>

<details>
<summary><h3> 8. 실시간 멤버 정보 변경과 캐시 동기화 (Event-Driven)</h3></summary>

**문제점:** 사용자가 프로필 정보(예: 닉네임, 프로필 사진)를 변경했을 때, 참여 중인 채팅방(1:1, 그룹)에 실시간으로 반영되어야 하며, 캐시 데이터와의 정합성도 유지해야 합니다.

**해결 방안:** Spring Application Event 또는 메시지 큐(Kafka, RabbitMQ 등, 여기서는 Redis Pub/Sub 활용 가능)를 사용하여 정보 변경 이벤트를 발행하고, 이를 구독하여
캐시 업데이트 및 실시간 전파를 수행합니다.

**처리 과정:**

1. **이벤트 발행:** 사용자가 프로필 정보를 업데이트하면, 관련 서비스 로직에서 '멤버 정보 변경 이벤트'를 발행합니다. 이 이벤트에는 변경된 사용자 ID와 정보가 포함됩니다.
2. **캐시 업데이트:** 이벤트 리스너(또는 메시지 구독자)가 이벤트를 수신하여, 해당 사용자가 속한 모든 채팅방의 멤버 정보 캐시(그룹 채팅방 멤버 목록, 1:1 채팅방 정보 등)를 찾아 업데이트합니다.
3. **실시간 전파 (Pub/Sub):** 캐시 업데이트 후, Redis Pub/Sub을 통해 '멤버 정보 변경' 토픽으로 메시지를 발행합니다.
4. **클라이언트 업데이트:** 각 서버 인스턴스의 Redis 구독자는 이 메시지를 받아, WebSocket을 통해 해당 채팅방에 접속 중인 클라이언트들에게 변경된 멤버 정보를 전송하여 UI를 실시간으로
   업데이트합니다.

**관련 이미지:**

<img width="1154" alt="Real-time Member Info Update and Cache Sync Flow Diagram" src="https://github.com/user-attachments/assets/b15a3b4a-3ef3-41d6-855b-4dd7e36b2c86">

</details>

<details>
<summary><h3> 9. Redis 캐시 업데이트 로직 상세</h3></summary>

- 멤버 정보 변경 이벤트를 처리하여 Redis 캐시를 업데이트하는 구체적인 로직 예시입니다.

**처리 순서:**

1. **기존 캐시 로드:** 업데이트해야 할 채팅방의 멤버 목록 캐시를 Redis에서 조회합니다. (Cache Miss 시 DB 조회 후 캐싱)
2. **업데이트 대상 식별:** 이벤트로부터 전달받은 변경된 멤버 정보(예: `UpdatedMemberDto`)를 생성합니다.
3. **캐시 내 멤버 정보 수정:** 로드한 캐시된 멤버 목록(List 또는 Set 형태)에서 변경된 멤버와 동일한 ID를 가진 항목을 찾아 새로운 정보로 교체(또는 업데이트)합니다. (
   `modifyCachedMember` 함수 예시)
4. **캐시 저장 및 전파:** 수정된 전체 멤버 목록을 다시 Redis에 저장(`updateCachedMembers` 함수 예시)하고, 변경 사실을 Pub/Sub을 통해 전파합니다.

**관련 이미지 (코드 스니펫):**

<img width="1416" alt="Cache Update Event Listener Code" src="https://github.com/user-attachments/assets/1c9f88ae-4be0-4898-99cb-d12959f51093">

<img width="1420" alt="Modify Cached Member Logic" src="https://github.com/user-attachments/assets/9f63be9f-2f14-47c8-97ff-460507c42320">

<img width="1289" alt="Update Cached Members and Publish Logic" src="https://github.com/user-attachments/assets/488efecd-da65-4d64-8ffd-43d9bd2167bd">

</details>

<details>
<summary><h3> 10. 실시간 알림 구현 (Server-Sent Events - SSE)</h3></summary>

**목표:** 사용자에게 특정 이벤트 발생 시 브라우저 또는 페이지 내에서 즉각적인 피드백을 제공합니다.

**구현:** SSE 기술을 사용하여 서버에서 클라이언트로 단방향 데이터 푸시를 구현합니다.

**알림 대상 이벤트:**

* 새로운 팔로워 발생
* 내가 찜한 콘텐츠의 그룹 채팅방 생성
* 새로운 1:1 채팅 메시지 수신
* 참여 중인 그룹 채팅방에 새 메시지 수신

**처리 과정:**

1. **연결 수립:** 클라이언트가 서버의 SSE 엔드포인트로 연결을 요청하면, 서버는 해당 클라이언트를 위한 `SseEmitter` 객체를 생성하고 관리 목록에 추가합니다 (`addEmitter` 로직).
2. **연결 유지 (Heartbeat):** 연결 유실을 방지하기 위해 주기적으로 (예: 30초마다) 더미 데이터(heartbeat)를 클라이언트로 전송합니다.
3. **이벤트 발생 및 전송:** 알림 대상 이벤트가 발생하면, 해당 이벤트를 수신해야 할 사용자의 `SseEmitter`를 찾아 알림 데이터를 전송(`emitter.send()`)합니다. 데이터는 특정 이벤트
   이름과 함께 전송될 수 있습니다.

**관련 이미지:**

- **브라우저 알림 예시:**

  <img width="562" alt="Browser Notification Example" src="https://github.com/user-attachments/assets/8b3dd225-1ffb-44f8-a83a-c0717f56697c">

- **페이지 내 알림 목록 예시:**

  <img width="460" alt="In-Page Notification List Example" src="https://github.com/user-attachments/assets/f6fda653-4981-4b9e-a4ae-df8331111b3b">

</details>

<details>
<summary><h3> 11. 다중 서버 환경에서의 SSE 알림 전파 (Redis Pub/Sub 활용)</h3></summary>

**문제점:** SSE는 클라이언트가 연결된 특정 서버 인스턴스와의 1:1 관계입니다. 로드밸런서를 사용하는 다중 서버 환경에서는, 이벤트가 발생한 서버 인스턴스에 해당 알림을 받아야 할 사용자의 SSE 연결이 없을
수 있습니다.

**해결 방안:** Redis Pub/Sub을 브로드캐스팅 메커니즘으로 활용하여, 어떤 서버 인스턴스에서 이벤트가 발생하든 모든 서버 인스턴스가 알림을 인지하고 자신이 관리하는 클라이언트에게 전달할 수 있도록
합니다.

**처리 과정:**

1. **이벤트 발생 및 발행:** 알림 이벤트가 특정 서버 인스턴스(A)에서 발생하면, 해당 서버는 알림 내용을 Redis의 특정 알림 토픽(예: `notification-channel`)으로 발행(Publish)
   합니다. 이 메시지에는 대상 사용자 ID와 알림 내용이 포함됩니다.
2. **구독 및 수신:** 모든 서버 인스턴스(A, B, C...)는 해당 알림 토픽을 구독(Subscribe)하고 있습니다. Redis는 발행된 메시지를 모든 구독자(모든 서버 인스턴스)에게 전달합니다.
3. **대상자 확인 및 전송:** 각 서버 인스턴스는 수신한 알림 메시지의 대상 사용자 ID를 확인합니다. 만약 해당 사용자의 `SseEmitter` 객체를 자신이 관리하고 있다면, 그 Emitter를 통해
   클라이언트에게 알림 데이터를 전송합니다.

**기대 효과:** 서버 확장성에 관계없이 모든 사용자에게 안정적으로 실시간 SSE 알림을 전달할 수 있습니다.

**관련 이미지:**

<img width="1055" alt="SSE Notification Broadcast using Redis Pub/Sub Diagram" src="https://github.com/user-attachments/assets/e4e1fecc-992e-4842-801d-c502e7572ef2">

</details>

## 🛠️ 기술 스택 (Tech Stack)

| 구분                 | 기술명                  | 설명                                                                   |
|:-------------------|:---------------------|:---------------------------------------------------------------------|
| **Frontend**       | React (Vite)         | JavaScript 라이브러리 기반의 사용자 인터페이스(UI) 구축                                |
|                    |                      |                                                                      |
| **Backend**        | Spring Boot 3.x      | Java 기반의 웹 애플리케이션 백엔드 프레임워크                                          |
|                    | Spring Data JPA      | JPA(Java Persistence API) 기반 데이터 액세스 및 관리                            |
|                    | Spring Security      | 인증(Authentication) 및 인가(Authorization) 처리 (JWT, OAuth2.0 활용)         |
|                    |                      |                                                                      |
| **Test & Doc**     | Rest Assured 5.x     | Java 기반 REST API 테스트 자동화 라이브러리                                       |
|                    | Spring REST Docs 3.x | API 테스트 코드를 기반으로 API 문서 자동 생성                                        |
|                    |                      |                                                                      |
| **DB & Cache**     | MySQL 8.0            | 사용자, 콘텐츠, 채팅방 등 정형 데이터 저장 (RDBMS)                                    |
|                    | MongoDB 5.0          | 채팅 메시지, 알림 등 비정형/대용량 데이터 저장 (NoSQL Document DB)                      |
|                    | Elasticsearch 8.x    | 콘텐츠 검색 및 추천 기능 구현을 위한 검색 엔진                                          |
|                    | Redis 7.0            | 인메모리 데이터 저장소: Caching, Pub/Sub, Queue(List), Stream 활용               |
|                    |                      |                                                                      |
| **Build & Deploy** | Docker               | 애플리케이션(React, Spring Boot) 및 인프라(Redis) 컨테이너화                        |
|                    | GitHub Actions       | CI/CD 파이프라인 구축: 코드 변경 감지, 자동 빌드, 테스트, 배포 자동화                         |
|                    | AWS                  | 클라우드 배포 환경: EC2(서버), RDS(MySQL), ELB(로드밸런싱), Route 53(DNS), ACM(SSL) |

## 🧑‍🤝‍🧑 멤버

| 팀장                                                                                                                    | 팀원                                                                                                                      | 팀원                                                                                                          | 팀원                                                                                                                              |
|-----------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| <a href="https://github.com/devbattery"><img src = "https://avatars.githubusercontent.com/devbattery" width="120px;"> | <a href="https://github.com/minyoongi96"><img src = "https://avatars.githubusercontent.com/minyoongi96" width="120px;"> | <a href="https://github.com/G1Huh"><img src = "https://avatars.githubusercontent.com/G1Huh" width="120px;"> | <a href="https://github.com/minjikimkim2222"><img src = "https://avatars.githubusercontent.com/minjikimkim2222" width="120px;"> |
| [정원준](https://github.com/devbattery)                                                                                  | [민윤기](https://github.com/minyoongi96)                                                                                   | [허지원](https://github.com/G1Huh)                                                                             | [김민지](https://github.com/minjikimkim2222)                                                                                       |

## 📉 ERD (Entity-Relationship Diagram)

* 주요 도메인(영화, 책, 회원)에 대한 데이터베이스 관계 모델입니다.

### 영화 (Movie Domain)

![Movie ERD](https://github.com/user-attachments/assets/47596b32-ffbc-4eca-9602-f6a0e1da11ba)

### 책 (Book Domain)

![Book ERD](https://github.com/user-attachments/assets/08851cee-5ca8-49f4-bdd6-68ff47ac417c)

### 회원 (Member Domain)

![Member ERD](https://github.com/user-attachments/assets/44b74d85-a6e2-45a8-999f-b331a4df03db)