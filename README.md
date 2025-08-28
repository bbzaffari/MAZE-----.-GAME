
# MAZE GAME 

This project implements a simple maze game in Java using text-based input and output. It uses a 2D map defined in `mapa.txt` and allows the user to interact with it via console.

## Requirements

- Java JDK 17 or newer
- `mapa.txt` file in the same directory

## Setup (Ubuntu / WSL2)

Install Java:

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk
````

(Optional) Set environment variables:

```bash
JAVA_HOME=$(dirname "$(dirname "$(readlink -f "$(which java)")")")
echo "export JAVA_HOME=$JAVA_HOME" >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

## Compile

```bash
javac *.java
```

## Run

```bash
java Jogo mapa.txt
```

## File structure

* `Jogo.java`: main class
* `mapa.txt`: text map file
* Other `.java` files: maze elements, rendering logic, movement validation, etc.


