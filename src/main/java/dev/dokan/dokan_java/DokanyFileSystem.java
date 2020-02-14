package dev.dokan.dokan_java;

import dev.dokan.dokan_java.constants.microsoft.FileSystemFlag;
import dev.dokan.dokan_java.structure.EnumIntegerSet;


final public class DokanyFileSystem {

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
        this.maxComponentLength = 256;
        this.fileSystemName = "NTFS";
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
