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

## containerize-sublock: builds container images with Java components
.PHONY: containerize-sublock
containerize-sublock: build
	@echo "Start preparing docker context"
	@cp sublock/target/sublock-0.0.1-SNAPSHOT.jar docker/sublock.jar
	@echo "Finished preparing docker context"
	
	@echo "Start building sublock container image"
	@docker build -t sublock.commonjava:0.0.1 -f ./docker/Dockerfile.common ./docker
	@docker build -t dls.sublock:0.0.1 -f ./docker/Dockerfile.sublock ./docker
	@echo "Finished building sublock container images"

	@echo "Start cleaning up after container images are built"
	@rm ./docker/sublock.jar
	@echo "Finished cleaning up after container images are built"

# ==================================================================================== #
# LOCAL SETUP
# ==================================================================================== #

## start-sublock-locally: starts the sublock system locally using docker compose
.PHONY: start-sublock-locally
start-sublock-locally: stop-sublock-locally containerize-sublock
	@echo "Start sublock locally"
	@docker compose -f ./docker/docker-compose.yml up -d 
	@echo "Started sublock locally. You can access it at http://localhost:8081"

## stop-sublock-locally: stops the sublock system locally
.PHONY: stop-sublock-locally
stop-sublock-locally:
	@echo "Stop locally running sublock"
	@docker compose -f ./docker/docker-compose.yml down
	@echo "Stopped locally running sublock"

