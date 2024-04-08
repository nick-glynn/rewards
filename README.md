# Rewards Points Calculator

## Description

This is a simple spring boot application that provides a web service endpoint that calculates rewards points for
customers based on the amount of purchases over the last 3 months.

The API endpoint accepts POST requests to /customer/calculate with a list of purchases to be provided as the request
body in JSON format (an example request with a list of purchases is included in the postman directory).

## Functional Implementation

A functional implementation is included in the 'functional' branch.
No external mutable container (ie. ArrayList) is used, instead Sets are used, HashSet is the implementation, and a
custom Collector is defined to create the mutable container (HashSet) for the stream, an accumulator BiConsumer is
defined to add CustomerRewardsResponse objects to the container, and a combiner BiConsumer is defined to merge two
containers together.
It is non-deterministic, allowing for parallel processing of the stream.

## Assumptions

- Purchase amounts and rewards points won't exceed the size limit of an integer even when summed.
- The prior 3-month period is set to be January, February, and March. This is done to isolate the dates as a separate
  concern and simplify data used for testing.
- The data passed in is valid and well-formed.

## How To Run

1. Clone
2. Run mvn spring-boot:run

### Postman Collection for testing

postman/Rewards Calculator.postman_collection.json

import this collection, there is only one request, and run it.

