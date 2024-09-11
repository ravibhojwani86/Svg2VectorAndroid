# Svg2VectorAndroid
It's really troublesome to import multiple svgs in Android Studio as it is a one by one process and not batch operation.
So if you have to import some 100 svgs, it involves navigating to Project -> New ->Vector Asset, then again browse the svg file you want
to import, then rename if you follow any naming convention and then finally next and finish.

This project helps to batch convert svg files to vector drawable xmls in one shot, with an optional other extension.

It uses [studio ide class Svg2Vector](https://android.googlesource.com/platform/tools/base/+/master/sdk-common/src/main/java/com/android/ide/common/vectordrawable/Svg2Vector.java) class implementation to parse svg and convert to xml file.

Simply pass source directory path to SvgFilesProcessor and call process.

## API
```
SvgFilesProcessor processor = new SvgFilesProcessor("./src", "./dist", "xml", "optional-prefix");
processor.process();
```

If you directly want to use the jar:

```
java -jar Svg2VectorAndroid-31.6.0.jar -s ./src -o ./dist -e xml -p "optional-prefix"
```

### Options

| option    | required      | default   | description               |
|---        |---            |---        |---                        |
| s         | true          | N/A       | Source folder             |
| o         | false         | dist      | Destination folder        |
| e         | false         | xml       | extension of new files    |
| p         | false         | ""        | prefix of new files       |

## Build the project

```
./gradlew fatJar
```
This creates `./build/libs/Svg2VectorAndroid-31.6.0.jar`
