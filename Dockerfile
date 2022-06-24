FROM openjdk:11

RUN addgroup --system ml2wf && adduser --system --ingroup ml2wf ml2wf

WORKDIR /ml2wf

COPY ./ml2wf/target/ml2wf.jar /ml2wf/ml2wf.jar

RUN chmod 007 ml2wf.jar

USER ml2wf:ml2wf

ENTRYPOINT ["java","-jar","/ml2wf/ml2wf.jar"]
