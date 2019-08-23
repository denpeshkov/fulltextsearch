package org.denis.files;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.nio.file.FileVisitResult.CONTINUE;

//Factory class
public class SearchFiles {

	private SearchFiles() {

	}

	static private class Finder extends SimpleFileVisitor<Path> {

		public Iterable<Path> getFiles() {
			return files;
		}

		private final List<Path> files = new ArrayList<>();

		private final PathMatcher matcher;

		private Path dir;

		Finder(String pattern) {
			matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		}

		// Compares the glob pattern against
		// the file or directory name.
		void find(Path file, Path dir) {
			Path name = file.getFileName();
			if (name != null && matcher.matches(name)) {
				files.add(file);
			}
		}

		// Invoke the pattern matching
		// method on each file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			find(file, dir);
			return CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
			//System.out.println(dir);
			this.dir = dir;
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			//exc.printStackTrace();
			return CONTINUE;
		}
	}

	public static Iterable<Path> traverseTree(Path dir, String extension) throws IOException {
		Finder finder = new Finder(extension);
		Files.walkFileTree(dir, finder);
		return finder.getFiles();
	}
}
