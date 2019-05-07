#!/bin/bash

java -jar swagger-codegen-cli.jar generate \
   -i swagger.yaml \
   -l java \
   -o output \
   --api-package io.proximax.sdk.api.swagger \
   --model-package io.proximax.sdk.infrastructure.model
