# Dockerfile to build container for unit testing.
#
# To build the image, run the following from this directory:
#   docker build -t testing .
#
# To run the tests, use
#   docker run testing
#

FROM openjdk:11

# Install Apache Ant
RUN apt-get update && apt-get install -y ant

ENV USER root

RUN mkdir /root/beast-outercore
WORKDIR /root/beast-outercore

ADD . ./

CMD ant travis
