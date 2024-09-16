# Include variables from the .envrc file
include .envrc

# ==================================================================================== #
# HELPERS
# ==================================================================================== #

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

# ==================================================================================== #
# BUILD
# ==================================================================================== #

## build: builds project modules
.PHONY: build
build:
	@echo "Start building dls components"
	@${mvn_home} -T 1C clean install
	@echo "Finished building dls components"

## clean: clean the Java artifacts built
.PHONY: clean
clean:
	@echo "Start cleaning dls component"
	@${mvn_home} -T 1C clean
	@echo "Finished cleaning dls components"

# ==================================================================================== #
# CONTAINER IMAGES
# ==================================================================================== #

## containerize: builds container images with Java components
.PHONY: containerize
containerize: build
	@echo "Start preparing docker context"
	@cp sublock/target/sublock-0.0.1-SNAPSHOT.jar docker/sublock.jar
	@cp api/target/api-0.0.1-SNAPSHOT.jar docker/api.jar
	@echo "Finished preparing docker context"
	
	@echo "Start building sublock container image"
	@docker build -t sublock.commonjava:0.0.1 -f ./docker/Dockerfile.common ./docker
	@docker build -t dls.sublock:0.0.1 -f ./docker/Dockerfile.sublock ./docker
	@docker build -t dls.api:0.0.1 -f ./docker/Dockerfile.api ./docker
	@echo "Finished building sublock container images"

	@echo "Start cleaning up after container images are built"
	@rm ./docker/sublock.jar
	@rm ./docker/api.jar
	@echo "Finished cleaning up after container images are built"

# ==================================================================================== #
# LOCAL SETUP
# ==================================================================================== #

## start-locally: starts the dls system locally using docker compose
.PHONY: start-locally
start-locally: stop-locally containerize
	@echo "Start dls locally"
	@docker compose --profile core -f ./docker/docker-compose.yml  up -d 
	@echo "Started dls locally. You can access it at http://localhost:8081"

## start-locally-with-logging: starts the dls system locally using docker compose and logging components
.PHONY: start-locally-with-logging
start-locally-with-logging: stop-locally containerize
	@echo "Start dls locally with logging components"
	@docker compose -f ./docker/docker-compose.yml --profile core --profile logging up -d 
	@echo "Started dls locally with logging components. You can access it at http://localhost:8081"

## stop-locally: stops the dls system locally
.PHONY: stop-locally
stop-locally:
	@echo "Stop locally running dls"
	@docker compose -f ./docker/docker-compose.yml --profile core --profile logging down
	@echo "Stopped locally running dls"

## run-smoke-tests: executes smoke tests against locally running system
.PHONY: run-smoke-tests
run-smoke-tests: start-locally
	@echo "Run smoke tests"
	@java -jar ./itests/target/itests-0.0.1-SNAPSHOT.jar
	@echo "Finished running smoke tests"
