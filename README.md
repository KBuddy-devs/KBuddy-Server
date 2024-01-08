# CourseMate-Server

CourseMate의 API 서버 입니다.

@author bnbong bbbong9@gmail.com

## API 문서
링크 추가 예정

## 개발 Stack
- Language : Python 3.11.1
- Framework : FastAPI + Pydantic & SQLAlchemy
- Database : PostgreSQL + asyncpg(Async Driver) + Alembic(Migration, 환경 추가 예정)

## 개발 환경 설정
다음 명령어를 통해 개발 환경을 세팅하는 스크립트를 실행하여 개발 환경을 세팅합니다.
```bash
$ bash ./scripts/setup.sh
```

## Code Convention
- 개발 환경 세팅 시 Python 3.11.1 버전이 필수로 설치되어 있어야 합니다.
- 모든 소스 코드의 스타일은 [PEP 8](https://peps.python.org/pep-0008/)을 준수해야 합니다.
- 모든 소스 코드는 Black Formatter를 통해 포맷팅 되어야 합니다.
> Formatting 적용 방법
> ```bash
> # venv 가상 환경이 활성화되어 있고 필요 의존성이 모두 설치된 상태에서
> $ black .
> ```
