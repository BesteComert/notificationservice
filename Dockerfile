FROM maven:3.8-adoptopenjdk-11

WORKDIR /usr/src/app
COPY . /usr/src/app

RUN mvn package

ENV PORT 9094
EXPOSE $PORT
CMD [ "sh", "-c", "mvn -Dserver.port=${PORT} spring-boot:run" ]