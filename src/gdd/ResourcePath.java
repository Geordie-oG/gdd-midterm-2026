package gdd;

import java.nio.file.Files;
import java.nio.file.Path;

/** Resolves project assets when Java is launched from the repo or its workspace. */
public final class ResourcePath {

    private static final String PROJECT_FOLDER = "gdd-midterm-2026";

    private ResourcePath() {
    }

    public static String resolve(String relativePath) {
        Path workingDirectory = Path.of("").toAbsolutePath().normalize();
        Path current = workingDirectory;

        while (current != null) {
            Path direct = current.resolve(relativePath).normalize();
            if (Files.isRegularFile(direct)) {
                return direct.toString();
            }

            Path projectChild = current.resolve(PROJECT_FOLDER).resolve(relativePath).normalize();
            if (Files.isRegularFile(projectChild)) {
                return projectChild.toString();
            }

            Path examWorkspaceChild = current.resolve("Game Dev Exam")
                    .resolve(PROJECT_FOLDER).resolve(relativePath).normalize();
            if (Files.isRegularFile(examWorkspaceChild)) {
                return examWorkspaceChild.toString();
            }

            current = current.getParent();
        }

        // Preserve the requested path so callers still report a useful error.
        return workingDirectory.resolve(relativePath).normalize().toString();
    }
}
