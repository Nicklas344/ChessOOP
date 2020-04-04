@echo off

:: Compile code.
javac -cp src src/chess/Main.java -d bin

:: Run code.
java -cp bin chess.Main
