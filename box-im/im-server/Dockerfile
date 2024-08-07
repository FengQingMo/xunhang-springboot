FROM openjdk:8
# 维护者信息
MAINTAINER FengQingMo
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /app

COPY target/*.jar /app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app.jar", "--server.port=8090"]