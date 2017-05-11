package awvillager.gradle.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class ZipUtil {

	public static void copyToZip(File fromDir_, File zipFilePath_) throws URISyntaxException, IOException {

		//FileをPathに変換
		Path fromDir = Paths.get(fromDir_.getAbsolutePath());
		Path zipFilePath = Paths.get(zipFilePath_.getAbsolutePath());

		Files.deleteIfExists(zipFilePath);
		final String zipRootDirName = fromDir.getFileName().toString();

		URI zipUri = new URI("jar", zipFilePath.toUri().toString(), null);
		Map<String, Object> env = new HashMap<>();
		env.put("create", "true");

		try (FileSystem fs = FileSystems.newFileSystem(zipUri, env, ClassLoader.getSystemClassLoader())) {
			Files.walkFileTree(fromDir, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Path relative = fromDir.relativize(file);
					Path zipFile = fs.getPath(zipRootDirName, relative.toString());
					Path parent = zipFile.getParent();
					if (parent != null) {
						Files.createDirectories(parent);
					}
					Files.copy(file, zipFile, StandardCopyOption.COPY_ATTRIBUTES);

					return FileVisitResult.CONTINUE;
				}
			});
		}
	}

}
