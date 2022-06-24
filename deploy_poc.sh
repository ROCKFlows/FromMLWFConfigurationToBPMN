#!/bin/bash

echo "Compiling project..."
cd ml2wf
mvn clean package -Dtest='com.ml2wf.v3.**' -DfailIfNoTests=false
echo "Done."

echo "Building docker images..."
cd ..
docker compose build
echo "Done."

echo "Deploying infrastructure..."
docker compose up -d
echo "Done."
echo "Waiting 10 seconds to make sure everything is ready..."
sleep 10
echo "Done."

echo "Importing feature model into arangodb..."
curl -X POST "http://localhost:8080/fm/" -H  "accept: application/json" -H  "Content-Type: application/xml" -d '@BPMN-Models/light_model_sample.xml'
echo "\nDone."

echo "To visualize the imported feature model:"
echo "   - go to http://localhost:8529/"
echo "   - username=root password=ml2wf_password"
echo "   - choose ml2wf-database"
echo "   - in collections, the FeatureModelTask collection contains the imported tasks"
echo "   - to create the graph for visualization, go to graphs > Add graph, select a name and select input suggestions"
