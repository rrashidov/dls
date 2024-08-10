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
