# Include variables from the .envrc file
include .envrc

work_dir=$(pwd)

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
	@echo "Start cleaning project"
	@${mvn_home} -T 1C clean
	@echo "Finished cleaning project"

# ==================================================================================== #
# Containers
# ==================================================================================== #

## containerize: build container images containing system components
.PHONY: containerize
containerize: build
	@echo "Start preparing docker context"
	@cp sublock/target/sublock-0.0.1-SNAPSHOT.jar docker/sublock.jar
	@cp api/target/api-0.0.1-SNAPSHOT.jar docker/api.jar
	@echo "Finished preparing docker context"
	
	@echo "Start building dls container images"
	@docker build -t dls.commonjava:0.0.1 -f ./docker/Dockerfile.commonjava ./docker
	@docker build -t dls.sublock:0.0.1 -f ./docker/Dockerfile.sublock ./docker
	@docker build -t dls.api:0.0.1 -f ./docker/Dockerfile.api ./docker
	@echo "Finished building dls container images"

	@echo "Start cleaning up after container images are built"
	@rm ./docker/sublock.jar
	@rm ./docker/api.jar
	@echo "Finished cleaning up after container images are built"
