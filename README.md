Solution.java - The code
sample.txt - dataset I tested with

Approach:
Since the condition was that the dataset is too large to be brought completely into memory, I used the k-way merge algorithm.
I divided the file into chunks of 100k lines, created temp files(sorted on Source IP), then merged the temp files into a single output.txt file.

output.txt now contains the sorted IP addresses.
I then used a priority queue to find the 5 IPs that occured max number of times.

To run the code, I used Eclipse IDE. Make sure sample.txt is in the same directory as source code.

