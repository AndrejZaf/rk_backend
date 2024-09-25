# Run Maven wrapper command
cd ..
./mvnw clean install
cd local
docker-compose up -d