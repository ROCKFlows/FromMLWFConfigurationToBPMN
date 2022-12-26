#!/bin/bash

echo "Building docker images..."
docker compose build
echo "Done."

echo "Deploying infrastructure..."
docker compose up -d
echo "Done."
echo "Waiting 10 seconds to make sure everything is ready..."
sleep 10
echo "Done."

echo "Importing feature model into arangodb..."
curl -X POST "http://localhost:8080/ml2wf/api/v1/fm?versionName=demopoc" -H  "accept: application/json" -H  "Content-Type: application/xml" -d '@BPMN-Models/light_model_sample.xml'
echo -e "\nDone."

echo "To visualize the imported feature model:"
echo "   - go to http://localhost:8529/"
echo "   - username=root password=ml2wf_password"
echo "   - choose ml2wf-database"
echo "   - in collections, the FeatureModelTask collection contains the imported tasks"
echo "   - to create the graph for visualization, go to graphs > Add graph, select a name and select input suggestions"
