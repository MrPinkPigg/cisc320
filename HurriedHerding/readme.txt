Aaron Knestaut
Cows.java readme

Because of the use of a hashmap, a built in data struct, each row of input and output is handled
in O(1). The values in the hashmap can be accessed immediately using the keys associated
with their respective map locations. The overall solution has to run in an average of O(c*log(c)+r).
If you break this time complexity down into it's parts, you end up with O(c*log(c)) and O(r). O(r)
comes from reading in the file, where every line has to be read before moving to the next. O(c log(c))
comes from the sorting used when at the end of the program, which is done using timsort, an O(n log n)
sorting algorithm. Together, this creates an O(c*log(c)+r) time complexity program.