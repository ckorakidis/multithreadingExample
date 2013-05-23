Multithreading example
=====================

Simple example of producer consumer problem

There is a producer created, two consumers and a monitor, all on different threads.

The dispatcher is given a map of queues, which contain two ques, one for each of the consumers.

The queues have messages to be consumed.

The producer pushes the messages to a common storage and the consumers consume these messages from the storage and at the end the consumers add the results in the storage.

The dispatcher, based on a given input value, changes a flag in the common storage which informs the cosumers to stop consume.

The consumers continue to consume while the stopping flag is false.

The storage where the consumers consume the messages, are stored in a queue of limited size, where consumers remove items to consume when there are available, and the dispatcher adds items to be consumed when the queue is not full.

The monitor thread consumes the results of the consumers, stored in the common storage and provides log output.

The result is not always the same, since the amount of the items consumed before getting the stop flag, depends on the order will be given to the different threads
