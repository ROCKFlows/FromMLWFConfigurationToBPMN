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

echo "Importing feature model into neo4j..."
curl -X POST "http://localhost:8080/ml2wf/api/v1/fm?versionName=demo" -H  "accept: application/json" -H  "Content-Type: application/xml" -d '@model.xml'
echo -e "\nDone."

echo "To access the UI:"
echo "   - go to http://localhost:4173/"
echo ""
echo "To visualize the imported feature model in Neo4J:"
echo "   - go to http://localhost:7474/browser/"
echo "   - username=neo4j password=ml2wf_password"
