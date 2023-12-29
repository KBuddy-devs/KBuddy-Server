# --------------------------------------------------------------------------
# FastAPI application의 custom middleware들을 정의한 모듈입니다.
#
# @author bnbong bbbong9@gmail.com
# --------------------------------------------------------------------------
import logging

from datetime import datetime

from starlette.requests import Request
from starlette.responses import JSONResponse
from starlette.middleware.base import BaseHTTPMiddleware

from fastapi import FastAPI

from app.helper.exceptions import InternalException, ExceptionSchema


# TODO: 다른 에러도 발견되면 추가.
# --------------------------------------------------------------------------
# ExceptionMiddleware
# --------------------------------------------------------------------------
class ExceptionMiddleware(BaseHTTPMiddleware):
    def __init__(self, app: FastAPI, logger: str):
        super().__init__(app)
        self.logger = logging.getLogger(logger)

    async def dispatch(self, request: Request, call_next):
        try:
            return await call_next(request)
        except InternalException as e:
            response = ExceptionSchema(
                timestamp=datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
                status=e.error_code.value[2],
                error=e.error_code.value[0],
                message=e.message,
                errorCode=e.error_code.value[1],
                path=request.url.path,
            )
            response_data = response.model_dump()
            self.logger.error(response_data)
            return JSONResponse(
                status_code=e.error_code.value[2], content=response_data
            )

        except Exception as e:
            print(e)
            response = ExceptionSchema(
                timestamp=datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
                status=500,
                error="SERVER_ERR",
                message="서버 로직에 알 수 없는 오류가 발생했습니다.",
                errorCode="SEVR-001",
                path=request.url.path,
            )
            response_data = response.model_dump()
            self.logger.error(response_data)
            return JSONResponse(status_code=500, content=response_data)
