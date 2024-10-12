package com.vector.svg2vectorandroid;

import com.android.ide.common.vectordrawable.Svg2Vector;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import static java.nio.file.FileVisitResult.CONTINUE;

public class SvgFilesProcessor {

	private final Path sourceSvgPath;
	private final Path destinationVectorPath;
	private final String extension;
	private final String prefix;

	public SvgFilesProcessor(String sourceSvgDirectory, String destinationVectorDirectory, String extension, String prefix) {
		this.sourceSvgPath = Paths.get(sourceSvgDirectory);
		this.destinationVectorPath = Paths.get(destinationVectorDirectory != null ? destinationVectorDirectory : "./dist");
		this.extension = extension != null ? extension : "xml";
		this.prefix = prefix != null ? prefix : "";
	}

	public void process(){
		try{
			EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
			//check first if source is a directory
			if(Files.isDirectory(sourceSvgPath)){
				Files.walkFileTree(sourceSvgPath, options, Integer.MAX_VALUE, new FileVisitor<>() {

					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)  {
						// Skip folder which is processing svg to xml
						if(dir.equals(destinationVectorPath)){
							return FileVisitResult.SKIP_SUBTREE;
						}

						try {
							Path newDirectory = destinationVectorPath.resolve(sourceSvgPath.relativize(dir));

							if(!Files.exists(newDirectory)) {
								Files.createDirectory(newDirectory);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

						return CONTINUE;
					}


					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						try {
							convertToVector(file, destinationVectorPath.resolve(sourceSvgPath.relativize(file)));
						} catch (SAXException e) {
							e.printStackTrace();
						}
						return CONTINUE;
					}


					public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
						return CONTINUE;
					}
				});
			} else {
				System.out.println("Source is not a directory");
			}

		} catch (Exception e){
			e.printStackTrace();
		}

	}

	private void convertToVector(Path source, Path target) throws IOException, SAXException {
		// convert only if it is .svg
		if (source.getFileName().toString().endsWith(".svg")) {
			File targetFile = getTargetFile(target, extension, prefix);
			FileOutputStream fous = new FileOutputStream(targetFile);
			try {
				Svg2Vector.parseSvgToXml(source, fous);
			} catch (Exception e) {
				System.err.println("Cannot convert file: " + source.getFileName());
			}

		} else {
			// only alert about non-dotfiles
			if (!source.getFileName().toString().startsWith(".")) {
				System.out.println("Skipping file as its not svg: " + source.getFileName().toString());
			}
		}
	}

	private File getTargetFile(Path target, String extension, String prefix){
		String svgFilePath =  target.toFile().getAbsolutePath();
		String fileName = target.toFile().getName().replaceFirst("[.][^.]+$", "");
		StringBuilder svgBaseFile = new StringBuilder();
		int index = svgFilePath.lastIndexOf(".");
		if(index != -1){
			String subStr = svgFilePath.substring(0, index);
			svgBaseFile.append(subStr);
		}
		svgBaseFile.append(".");
		svgBaseFile.append(extension);

		String fullFile = svgBaseFile.toString().replace(fileName, (prefix + fileName).toLowerCase().replaceAll("-", "_"));
		return new File(fullFile);
	}

	private static void copyDir(String src, String dest, boolean overwrite) {
    	try {
        	Files.walk(Paths.get(src)).forEach(a -> {
				Path b = Paths.get(dest, a.toString().substring(src.length()));
				try {
					if (!a.toString().equals(src))
						Files.copy(a, b, overwrite ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			//permission issue
			e.printStackTrace();
		}
	}

}
