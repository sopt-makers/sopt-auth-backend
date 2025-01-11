# sopt-auth-backend
> _**Updated at.** 2025/01/11 (Sat)_ <br/>
> _**Updated by.** [@yummygyudon](https://www.github.com/yummygyudon)_

SOPT 회원들이 SOPT makers 프로덕트를 사용하기 위한 **인증**과 **로그인/회원가입 기능**을 지원합니다.

## Tech Stack
- **Language** : Java 21
- **Framework** : Spring Framework
  - Spring Boot 
  - Spring Data JPA 
  - Spring Security (Starter, OAuth 2.0 Resource Server)
  - Java JWT (`jjwt`)
- **Deploy** : Github Actions, AWS S3, AWS ECR, Docker
- **Test** 
  - JUnit5
  - Mockito 
  - Rest Assured
  - Fixture Monkey(1.0.25)
  - Archunit(1.3.0)
- **Client** : OKHttp

<br/>

## Conventions
**협업 프로세스**는 아래와 같습니다.
1. **github issue 생성**
    - 템플릿에 맞춰 Issue 작성
    - Assignees, Label 할당
    - 단, "사소한 변경의 HOTFIX"인 경우, 구체적인 커밋 메시지 작성을 통해 전달
2. **branch 생성**
    - 형식 : `{Issue Tag}/#{Issue Number}` (ex. `feat/#1`)
3. **로컬 작업**
    - **기능단위** 커밋 지향
4. 해당 issue에 대한 작업 완료 시, `dev` 브랜치로 **해당 브랜치 Pull Request 제출**
    - 템플릿에 맞춰 PR 작성
    - Assignees, Label 할당
    - `prod` 브랜치에 대한 PR의 경우, 버전 반영에 영향이 있기 때문에 반드시 적절한 Label을 할당합니다.
5. 리뷰어가 전원 Approve 때까지 리뷰를 주고받으며 기능 수정 반복
6. 리뷰어 전원 Approve 시, merge
    - `feature branch` → `dev` → `prod`

> **프로젝스 소스코드 외** (환경변수, db 필드 및 테이블 수정, 인프라 세팅 등) 수정사항이 있을 경우, <br/>
> 팀원에게 먼저 물어보고 진행하거나, 그러지 못하였더라면 빠르게 전달해야 합니다 (카톡, 슬랙, 디코 등)

<br/>

### 🔖 Issue / Commit Tag
| Tag          | Description                    |
|:-------------|:-------------------------------|
| `[ADD]`      | 주요 기능 관련 코드/파일 추가 |
| `[MODIFY]`   | 주요 기능 관련 코드/파일 수정 |
| `[DEL]`      | 주요 기능 관련 코드/파일 제거 |
| `[CHORE]`    | 주요 기능 **외** 코드/파일 추가 및 수정 |
| `[FEAT]`     | 기능 구현 |
| `[FIX]`      | 기능 버그 및 오류 해결 |
| `[HOTFIX]`   | Issue / QA 과정에서의 급한 버그 및 오류 해결 |
| `[DOCS]`     | README/WIKI 등의 문서 작업           |
| `[REFACTOR]` | 기능/성능/코드 개선 작업                 |

<br/>

### Branch Strategy
기본적으로 "**Git Flow**" 전략을 바탕으로 적용했습니다.
- `main`, `dev`, `feature` 브랜치 구성
- `main` : **production**용 브랜치
    - 실서비스용 ec2(**makers.operation.prod)**로 배포되도록 파이프라인이 구축되어 있습니다
- `dev` : **development**용 브랜치
    - 테스트용 ec2(**makers.operation)**로 배포되도록 파이프라인이 구축되어 있습니다
    - default 브랜치 입니다
- `feature` : **Isuue** 브랜치
    - 각자 이슈에 대한 작업물 브랜치
      - 이슈에 따라 브랜치 Prefix 지정 (feat/fix/modify 등)
      - 형식 = `{이슈 종류}/#{이슈번호}` 
    - `main` 직접적인 PR 불가능 (`dev` PR을 거쳐 merge)

<br/>

## Authors
- 정동규 : [@yummygyudon](https://www.github.com/yummygyudon)
- 김성은 : [@sung-silver](https://www.github.com/sung-silver)
- 강현욱 : [@hyunw9](https://www.github.com/hyunw9)

![Alt](https://repobeats.axiom.co/api/embed/7ecaed933101e79c17cea76035d79ea7afff6565.svg "Repobeats analytics image")

<br/>

