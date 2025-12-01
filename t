echo "----- Test $1"
java -classpath .:./AST TestVNM Tests/$1.in >Tests/$1.out
diff -b Tests/$1.out Tests/$1.expected
