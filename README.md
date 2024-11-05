# MS PAINT ESCAPE

### Level 3 Programming Project by Riley Nicholls

This project is assessed against [AS91906](https://www.nzqa.govt.nz/nqfdocs/ncea-resource/achievements/2019/as91906.pdf)

## Project Description

The project involves the programming of a game.

In this game, you must navigate the mapm finding useful items that will allow you
to unlock different locations. You need to find the elevator to the next layer
of the massive city you find yourself in. But it seems you'll need to find
a few tools in order to make your way there alive...


## Source Code

The project is written in [Kotlin](https://kotlinlang.org/) and uses [Swing](https://docs.oracle.com/javase/8/docs/technotes/guides/swing/) for the GUI, themed with [FlatLAF](https://github.com/JFormDesigner/FlatLaf) for a modern look.

The main source file is [Main.kt](src/Main.kt)


## Documentation

Evidence of testing can be found in [testing.md](testing.md)


## Running the Program

You can either clone this whole repo, open it using [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) and run from source; or you can run the compiled program:

1. Install the [Java runtime (JRE)](https://www.java.com/en/download/) installed to run the program.
2. Go to the [out/artifacts](out/artifacts) folder
3. Locate and download the compiled **JAR file** (e.g. FILENAME.jar)
4. Run the following command:
    ```bash
    java -jar FILENAME.jar
    ```