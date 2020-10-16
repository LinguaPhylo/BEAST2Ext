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

# Install and configure VNC server
RUN apt-get update && apt-get install -y tightvncserver twm
ENV DISPLAY :0
ENV USER root
RUN mkdir /root/.vnc
RUN echo password | vncpasswd -f > /root/.vnc/passwd
RUN chmod 600 /root/.vnc/passwd

# Ant build fails if the repo dir isn't named beast-outcore
RUN mkdir /root/beast-outcore
WORKDIR /root/beast-outcore

ADD . ./

CMD vncserver $DISPLAY -geometry 1920x1080; ant travis
