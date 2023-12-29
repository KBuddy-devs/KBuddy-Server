"""
setting up packages to make sure our machine read our packages properly.
"""
from os import path

from setuptools import setup, find_packages

working_dir = path.abspath(path.dirname(__file__))

if __name__ in ("__main__", "builtins"):
    setup(
        name="CourseMate API",
        description="외국인을 위한 한국 여행 컨텐츠 공유 및 상호 교류 지원 플랫폼, CourseMate의 API입니다.",
        url="https://github.com/CourseMate-devs/CourseMate-Server",
        author="bnbong",
        author_email="bbbong9@gmail.com",
        packages=find_packages(),
        package_data={},
        python_requires=">=3.11, <3.12",
    )
