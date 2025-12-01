VNM.class: VNM.jjt
	jjtree VNM.jjt
	javacc AST/VNM.jj
	javac -classpath .:./AST  AST/*.java
	javac -classpath .:./AST  *.java
