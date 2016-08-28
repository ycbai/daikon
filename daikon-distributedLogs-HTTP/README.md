# Talend Daikon Distributed Logs

This project is a POC. Its aim is to discover Zipkin and tracing in a distributed environment.

## Prerequisites
Installed on the machine : 
- gradle
- elasticsearch 2 (started on default port)

## Contents

This repository contains the source files of shared libraries for all products. 
it contains :
* zipkin-query-service : collect the traces and ui is available on port 8080
* producer : the API to call to generate traces - start on port 8081
* consumer : the API called by the producer - start on port 8082

## How to
1) Go to zipkin-query-service
./zipkin.sh

it will launch the zipkin server

2) start producer and consumer
3) make POST producer http://localhost:8081
4) Observe the results in zipkin

## Support

No support for the moment


## Contributing

We welcome contributions of all kinds from anyone.

Using the bug tracker [Talend bugtracker](http://jira.talendforge.org/) is the best channel for bug reports, feature requests and submitting pull requests.

Feel free to share your Talend components on [Talend Exchange](http://www.talendforge.org/exchange).

## License

Copyright (c) 2006-2016 Talend

Licensed under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt)

