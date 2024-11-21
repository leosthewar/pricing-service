IMAGE_NAME:=leosdev/price-service
IMAGE_TAG:=latest

default:
	cat ./Makefile
dist:
	mvn clean package -DskipTests
test:
	mvn test
image:
	docker build -t $(IMAGE_NAME):$(IMAGE_TAG) .
run:
	docker run -p 8080:8080 $(DOCKER_PARAMS) $(IMAGE_NAME):$(IMAGE_TAG)
up: dist image run