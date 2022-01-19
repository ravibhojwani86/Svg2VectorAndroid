# Svg2VectorAndroid

It's really troublesome to import multiple svgs in Android Studio as it is a one by one process and not batch operation.
So if you have to import some 100 svgs, it involves navigating to Project -> New ->Vector Asset, then again browse the svg file you want
to import, then rename if you follow any naming convention and then finally next and finish.

This project helps to batch convert svg files to vector drawable xmls in one shot, with option to provide extension and extension suffix.

Simply pass source directory path to SvgFilesProcessor and call process.

Sample below:

```java
SvgFilesProcessor processor = new SvgFilesProcessor("/Volumes/Development/Features/MySvgs");
processor.process();
```

This will create a new folder "ProcessedSVG" inside source folder.

It uses studio ide class Svg2Vector class implementation ([reference](https://android.googlesource.com/platform/tools/base/+/master/sdk-common/src/main/java/com/android/ide/common/vectordrawable/Svg2Vector.java))
to parse svg and convert to xml file.

If you directly want to use the jar , use as below:

```bash
java -jar Svg2VectorAndroid-1.0.3.jar "SourceDirectoryPath"
```

## Build instructions
To build the project run `./gradlew fatJar` in the project root.
