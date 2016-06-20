# Talend Daikon Distributed Logs

This project is a POC. Its aim is to discover Zipkin and tracing in a distributed environment.

## Prerequisites
Installed on the machine : 
- gradle
- rabbitmq
- elasticsearch (on default port)

## Contents

This repository contains the source files of shared libraries for all products. 
it contains :
* zipkin-query-service : zipkin core - collect the traces and ui is available on port 8080
* eureka-server : communication between services
* message-producer : the API to call to generate traces
* message-consumer : consume the messages send by message-producer
* message-thirdLevel : consume the messages send by message-consumer (in order to have 3 levels of traces)

## Support

No support for the moment


## Contributing

We welcome contributions of all kinds from anyone.

Using the bug tracker [Talend bugtracker](http://jira.talendforge.org/) is the best channel for bug reports, feature requests and submitting pull requests.

Feel free to share your Talend components on [Talend Exchange](http://www.talendforge.org/exchange).

## License

Copyright (c) 2006-2016 Talend

Licensed under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt)

