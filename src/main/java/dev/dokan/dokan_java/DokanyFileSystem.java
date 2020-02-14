package dev.dokan.dokan_java;

import dev.dokan.dokan_java.constants.microsoft.FileSystemFlag;
import dev.dokan.dokan_java.structure.EnumIntegerSet;


public class DokanyFileSystem {

    public static final int DEFAULT_MAX_COMPONENT_LENGTH = 256;
    public static final String DEFAULT_FS_NAME = "NTFS";

    private final int maxComponentLength;
    private final String fileSystemName;
    private final EnumIntegerSet<FileSystemFlag> fileSystemFeatures;
    private final boolean usesKernelFlagsAndCodes;


    public DokanyFileSystem(int maxComponentLength, String fileSystemName, EnumIntegerSet<FileSystemFlag> fileSystemFeatures, boolean usesKernelFlagsAndCodes) {
        this.maxComponentLength = maxComponentLength;
        this.fileSystemName = fileSystemName;
        this.fileSystemFeatures = fileSystemFeatures;
        this.usesKernelFlagsAndCodes = usesKernelFlagsAndCodes;
    }

    public DokanyFileSystem(EnumIntegerSet<FileSystemFlag> fileSystemFeatures, boolean usesKernelFlagsAndCodes) {
        this.maxComponentLength = DEFAULT_MAX_COMPONENT_LENGTH;
        this.fileSystemName = DEFAULT_FS_NAME;
        this.fileSystemFeatures = fileSystemFeatures;
        this.usesKernelFlagsAndCodes = usesKernelFlagsAndCodes;
    }

    public int getMaxComponentLength() {
        return maxComponentLength;
    }

    public String getFileSystemName() {
        return fileSystemName;
    }

    public EnumIntegerSet<FileSystemFlag> getFileSystemFeatures() {
        return fileSystemFeatures;
    }

    public boolean isUsesKernelFlagsAndCodes() {
        return usesKernelFlagsAndCodes;
    }
}
