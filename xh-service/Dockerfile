FROM openjdk:8
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /app

COPY target/*.jar /app.jar
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app.jar", "--server.port=8081"]
