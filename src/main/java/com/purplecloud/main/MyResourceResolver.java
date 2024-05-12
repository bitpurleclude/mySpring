package com.purplecloud.main;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
/**
 * 用于获取对应包下所有类
 */
public class MyResourceResolver {
    String basePackage;
    List<String> classes;

    public MyResourceResolver(String basePackage) {
        this.basePackage = basePackage;
        this.classes = scanClass();
    }

    private List<String> scanClass(){
        List<String> classNames;
        String packagePath = basePackage.replace('.', '/');
        URL packageUrl = getClass().getClassLoader().getResource(packagePath);
        String packageFolderPath = packageUrl.getPath();
        Path packageFolder = Paths.get(packageFolderPath.substring(1));
        try {
            classNames=getAllClass(packageFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classNames;
    }

    private List<String> getAllClass(Path packageFolder) throws IOException {
        List<String> className = new ArrayList<>();
        Files.walkFileTree(packageFolder, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file.toAbsolutePath());
                Path fileName = file.getFileName();
                if (file.getFileName().toString().endsWith(".class")) {
                    Path relativize = packageFolder.relativize(file);
                    System.out.println(basePackage + "." + packageFolder.relativize(file).toString().replace(".class", "").replace("\\", "."));
                    className.add(basePackage + "." + packageFolder.relativize(file).toString().replace(".class", "").replace("\\", "."));
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.err.println(exc);
                return FileVisitResult.CONTINUE;
            }
        });
        return className;
    }

    /**
     * 获取
     *
     * @return basePackage
     */
    public String getBasePackage() {
        return basePackage;
    }

    /**
     * 设置
     *
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 获取
     *
     * @return classes
     */
    public List<String> getClasses() {
        return classes;
    }

    /**
     * 设置
     *
     */
    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    /**
     */
    public String toString() {
        return "MyResourceResolver{basePackage = " + basePackage + ", classes = " + classes + "}";
    }
}
