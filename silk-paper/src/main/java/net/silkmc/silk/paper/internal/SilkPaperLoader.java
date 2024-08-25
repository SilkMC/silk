package net.silkmc.silk.paper.internal;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class SilkPaperLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        final List<String> deps;
        try (final var stream = SilkPaperLoader.class.getResourceAsStream("/silkDependencies.txt")) {
            if (stream == null) {
                throw new IllegalStateException("Silk dependencies file could not be found," +
                    " the class loader may have been called in an inappropriate context");
            }
            deps = new BufferedReader(new InputStreamReader(stream))
                .lines()
                .map(String::trim)
                .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Silk dependencies file could not be read," +
                " the class loader may have been called in an inappropriate context", e);
        }

        final var resolver = new MavenLibraryResolver();

        for (final var dep : deps) {
            resolver.addDependency(new Dependency(new DefaultArtifact(dep), null));
        }
        resolver.addRepository(new RemoteRepository.Builder("paper", "default", "https://repo.papermc.io/repository/maven-public/").build());

        classpathBuilder.addLibrary(resolver);
    }
}
