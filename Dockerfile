FROM maven:3.8.7-eclipse-temurin-19 as builder

WORKDIR /ml2wf-build

COPY ./ml2wf/ .

RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests # tmp disable tests

# RUN mvn clean package -DskipTests

FROM eclipse-temurin:19 as runner

WORKDIR /ml2wf-runner

COPY --from=builder /ml2wf-build/app/target/ml2wf.jar .

COPY wait-for-it.sh /ml2wf-runner/wait-for-it.sh

COPY wait-for-them.sh /ml2wf-runner/wait-for-them.sh

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=neo4j","-jar","ml2wf.jar"]
