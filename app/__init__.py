# --------------------------------------------------------------------------
# FastAPI Application을 생성하는 모듈입니다.
#
# @author bnbong bbbong9@gmail.com
# --------------------------------------------------------------------------
import logging

from setuptools_scm import get_version

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager

from app.core.middlewares import ExceptionMiddleware
from app.db.database import engine, Base
from app.routers import router
from app.core.settings import AppSettings
from app.utils.documents import add_description_at_api_tags
from app.helper.logging import init_logger as _init_logger


try:
    __version__ = get_version(
        root="../../..", relative_to=__file__
    )  # git version (dev version)
except LookupError:
    __version__ = "1.0.0"  # production version


logger = logging.getLogger(__name__)


def init_logger(app_settings: AppSettings) -> None:
    _init_logger(f"fastapi-backend@{__version__}", app_settings)


@asynccontextmanager
async def lifespan(app: FastAPI):
    try:
        logger.info("Application startup")
        logger.info("Create connection and setting up database")
        async with engine.begin() as conn:
            logger.info("DB connected with url %s", engine.url)
            await conn.run_sync(Base.metadata.create_all)
        yield
    finally:
        logger.info("Application shutdown")


def create_app(app_settings: AppSettings) -> FastAPI:
    # TODO: root_path에 맞춰 OpenAPI 문서 렌더링 경로 수정
    if not app_settings.DEBUG_MODE:
        root_path = "/course-mate"
    else:
        root_path = ""

    app = FastAPI(
        root_path=root_path,
        title="CourseMate API",
        description="외국인을 위한 한국 여행 컨텐츠 공유 및 상호 교류 지원 플랫폼, CourseMate의 API입니다.",
        version=__version__,
        lifespan=lifespan,
    )

    origins = ["*"]

    app.add_middleware(
        CORSMiddleware,
        allow_origins=origins,
        allow_methods=["*"],
        allow_headers=["*"],
    )
    app.add_middleware(ExceptionMiddleware, logger="app_error_logger")

    app.include_router(router)

    add_description_at_api_tags(app)

    return app
