# dls

Distributed Locking Service (dls) is a service which provides locking in distributed environment. 

General rule of thumb is that besides the user-requested, there are a great deal of background activities being executed.

This is why it is crucial to have a mechanism to synchronize those activities when executed over and the same data/set of data.

The main goal of the service is to provide high availability. In other words, the users of the service should be able to acquire locks in almost any conditions - failed nodes, failed network, failed storage, failed availability zones, etc. This is the goal that will guide all of the design principles.

Another important characteristics of distributed systems is scalability. This will be addressed later or in the context of another project.

More information could be found in the [design document](./doc/README.md).
