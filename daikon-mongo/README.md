Service mongo
==============

This module provides
* mongo utilities which allow to create custom converters
* a mongo repo which allow storing "pending records" documents

Mongo utilities
------------------------------------
`MongoCustomConverter` class let developers define custom converters for mongo, which are used by the provided configuration in `RepoConfiguration`

Pending record repository
------------------------------------
A pending record document represent a kafka message which could not be sent to a kafka broker, and which is waiting for been sent again
`PendingRecordRepository` implements basic operations on pending records

