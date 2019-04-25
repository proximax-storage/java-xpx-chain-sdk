#!/bin/bash

java -jar swagger-codegen-cli.jar generate \
   -i swagger.yaml \
   -l java \
   -o output \
   --api-package io.nem.sdk.api.swagger \
   --model-package io.nem.sdk.infrastructure.model
