FROM maven:3.8.6-jdk-11 as builder

WORKDIR /ml2wf-build

COPY ./ml2wf/pom.xml .

COPY ./ml2wf/src/ ./src/

RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests # tmp disable tests

# RUN mvn clean package -DskipTests

FROM openjdk:11 as runner

WORKDIR /ml2wf-runner

COPY --from=builder /ml2wf-build/target/ml2wf.jar .

COPY wait-for-it.sh /ml2wf-runner/wait-for-it.sh

COPY wait-for-them.sh /ml2wf-runner/wait-for-them.sh

EXPOSE 8080

ENTRYPOINT ["java","-jar","ml2wf.jar"]
