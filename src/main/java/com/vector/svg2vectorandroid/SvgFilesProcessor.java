package com.vector.svg2vectorandroid;

import com.android.ide.common.vectordrawable.Svg2Vector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by ravi on 18/12/17.
 */

public class SvgFilesProcessor {
	
	private Path sourceSvgPath;
	private Path destinationVectorPath;
	private String extension;
	private String extensionSuffix;
	
	public SvgFilesProcessor(String sourceSvgDirectory) {
		this(sourceSvgDirectory, sourceSvgDirectory+"/ProcessedSVG", "xml", "");
	}
	
	public SvgFilesProcessor(String sourceSvgDirectory, String destinationVectorDirectory) {
		this(sourceSvgDirectory, destinationVectorDirectory, "xml", "");
	}

	public SvgFilesProcessor(String sourceSvgDirectory, String destinationVectorDirectory, String extension,
                             String extensionSuffix) {
		this.sourceSvgPath = Paths.get(sourceSvgDirectory);
		this.destinationVectorPath = Paths.get(destinationVectorDirectory);
		this.extension = extension;
		this.extensionSuffix = extensionSuffix;
	}
	
	public void process(){
		try{
			EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
			//check first if source is a directory
			if(Files.isDirectory(sourceSvgPath)){
				Files.walkFileTree(sourceSvgPath, options, Integer.MAX_VALUE, new FileVisitor<Path>() {
		
					public FileVisitResult postVisitDirectory(Path dir,
							IOException exc) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					public FileVisitResult preVisitDirectory(Path dir,
							BasicFileAttributes attrs)  {
						// Skip folder which is processing svgs to xml
						if(dir.equals(destinationVectorPath)){
							return FileVisitResult.SKIP_SUBTREE;
						}
						
						CopyOption[] opt = new CopyOption[]{COPY_ATTRIBUTES, REPLACE_EXISTING};
						Path newDirectory = destinationVectorPath.resolve(sourceSvgPath.relativize(dir));
						try{
							Files.copy(dir, newDirectory,opt);
						} catch(FileAlreadyExistsException ex){
							System.out.println("FileAlreadyExistsException "+ex.toString());
						} catch(IOException x){
							return FileVisitResult.SKIP_SUBTREE;
						}
						return CONTINUE;
					}

		
					public FileVisitResult visitFile(Path file,
							BasicFileAttributes attrs) throws IOException {
						convertToVector(file, destinationVectorPath.resolve(sourceSvgPath.relativize(file)));
						return CONTINUE;
					}


					public FileVisitResult visitFileFailed(Path file,
							IOException exc) throws IOException {
						return CONTINUE;
					}
				});
			} else {
				System.out.println("source not a directory");
			}
			
		} catch (IOException e){
			System.out.println("IOException "+e.getMessage());
		}
		
	}
	
	private void convertToVector(Path source, Path target) throws IOException{
		// convert only if it is .svg
		if(source.getFileName().toString().endsWith(".svg")){
			File targetFile = getFileWithXMlExtension(target, extension, extensionSuffix);
			FileOutputStream fous = new FileOutputStream(targetFile);
			Svg2Vector.parseSvgToXml(source.toFile(), fous);
		} else {
			System.out.println("Skipping file as its not svg "+source.getFileName().toString());
		}
    }
	
	private File getFileWithXMlExtension(Path target, String extension, String extensionSuffix){
		String svgFilePath =  target.toFile().getAbsolutePath();
		StringBuilder svgBaseFile = new StringBuilder();
		int index = svgFilePath.lastIndexOf(".");
		if(index != -1){
			String subStr = svgFilePath.substring(0, index);
			svgBaseFile.append(subStr);
		}
		svgBaseFile.append(null != extensionSuffix ? extensionSuffix : "");
		svgBaseFile.append(".");
		svgBaseFile.append(extension);
		return new File(svgBaseFile.toString());	
	}

}
