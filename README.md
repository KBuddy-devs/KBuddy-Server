# KBuddy-Server

K-Buddy의 API 서버 입니다.

## API 문서
링크 추가 예정 (외부 링크 첨부 예정)

## 개발 Stack
- Language : JAVA 17 openjdk 17.0.9
- Framework : SpringBoot + JPA + Gradle
- Database : PostgreSQL + Redis (Cache)

## 개발 환경 설정

<details>
<summary><b>Code Convention XML 파일 IDE에 적용하는 방법</b></summary>
<div markdown="1">

![setting_1.png](.github%2Fsetting_1.png)

`Settings -> Editor -> Code Style -> Java` 를 선택합니다.

![setting_2.png](.github%2Fsetting_2.png)

탭 상단의 설정 버튼을 누르고 `Import Scheme -> IntelliJ IDEA code style XML` 을 선택한 후

![setting_3.png](.github%2Fsetting_3.png)

K-Buddy_Backend 프로젝트의 `rule-config` 폴더에 있는 `naver-intellij-formatter.xml` 파일을 선택합니다.


![setting_4.png](.github%2Fsetting_4.png)

XML 파일을 IDE가 성공적으로 읽었다면 위와 같은 알림이 나타나며, Apply 버튼을 눌러 설정을 적용합니다.

</div>
</details>

## Code Convention
- 개발 환경 세팅 시 JAVA 17 버전이 필수로 설치되어 있어야 합니다.
- 코드 컨벤션은 [NAVER Intellij 코딩 컨벤션](https://github.com/naver/hackday-conventions-java/blob/master/rule-config/naver-intellij-formatter.xml)을 따릅니다.
> Formatting 적용 방법
> ```bash
> # Windows
> $ CTRL + ALT + L
> 
> # MacOS
> $ Option + Command + L
> ```
