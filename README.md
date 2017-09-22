# BFSPMiner
The code for the BFSPMiner algorithm as described in "BFSPMiner: An Effective and Efficient Batch-Free Algorithm for Mining Sequential Patterns over Data Streams"
The algorithm is able create sequential patterns out of a data stream. Additionally, it can use those created patterns to predict new events in the stream.

Feel free to add issues if you have suggestions on how to improve the usability, runtime, memory or accuarcy of the algorithm.

# Getting Started
If you want to use the algorithm, feel free to pull the project. Since it is a maven project you can either use it in your IDE, or package it and use the .jar
The code contains a Main class with two different example methods. One method explains how to generate and output sequential patterns. 
The second shows how to use sequential patterns for prediction. Simply choose your method in the code and run the main method to see some results.

# Explaination of methods and classes
I will list the methods here that you are intended to use (the other mehtods are helpers for the code or the example, play around if you like, but they are not designed for easy and intuitve use).

### BFSPMiner
The main algorithm. After you created an object of this you can use it to generate sequential patterns by using the createPatterns method

### Predictor
This class can make use of the _StatusObject_ returned by the _BFSPMiner_. After recieving such an object it will return an array of predictions. 
Additionally it can keep track of the recall, precision and f1 values. Currently this will only be accuarte if you use the prediction in each time stamp, as it will compare the _event_ in the _StatusObject_ with previous predictions to update the score.

### Main
Contains two small examples

### MakeOutput
reads out a _StatusObject_ and prints out a .csv file 

### MakeStreams
a small helper that reads out a .csv file and returns a List of Strings which can be used as a data stream for playing around
