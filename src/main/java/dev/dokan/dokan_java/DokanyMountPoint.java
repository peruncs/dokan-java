package dev.dokan.dokan_java;

import com.sun.jna.WString;
import dev.dokan.dokan_java.constants.dokany.MountError;
import dev.dokan.dokan_java.constants.dokany.MountOption;
import dev.dokan.dokan_java.structure.DokanOptions;
import dev.dokan.dokan_java.structure.EnumIntegerSet;

import java.io.Closeable;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a file system mount point.
 */
public class DokanyMountPoint implements Closeable {

    protected final DokanyFileSystem fileSystem;
    protected final Path mountPoint;
    protected final String volumeName;
    protected final int volumeSerialNumber;
    protected final DokanOptions dokanOptions;
    protected final AtomicBoolean mounted;
    protected final DokanyOperations dokanyOperations;

    /**
     * Constructor.
     *
     * @param dokanOptions       options.
     * @param volumeName         volume name.
     * @param volumeSerialNumber serial number.
     * @param dokanyOperations   operations.
     */
    public DokanyMountPoint(DokanyFileSystem fileSystem, DokanOptions dokanOptions, String volumeName, int volumeSerialNumber, DokanyOperations dokanyOperations) {
        this.fileSystem = fileSystem;
        this.dokanOptions = dokanOptions;
        this.mountPoint = Path.of(dokanOptions.MountPoint.toString());
        this.volumeName = volumeName;
        this.volumeSerialNumber = volumeSerialNumber;
        this.mounted = new AtomicBoolean(false);
        this.dokanyOperations = dokanyOperations;
    }

    public DokanyMountPoint(DokanyFileSystem fileSystem, DokanOptions dokanOptions, String volumeName, int volumeSerialNumber) {
        this.fileSystem = fileSystem;
        this.dokanOptions = dokanOptions;
        this.mountPoint = Path.of(dokanOptions.MountPoint.toString());
        this.volumeName = volumeName;
        this.volumeSerialNumber = volumeSerialNumber;
        this.mounted = new AtomicBoolean(false);
        this.dokanyOperations = new DokanyOperations();
    }

    /**
     * An additional constructor for easy mounting with a lot of default values.
     *
     * @param mountPoint       where to mount .
     * @param mountOptions     options.
     * @param dokanyOperations operations.
     */
    public DokanyMountPoint(DokanyFileSystem fileSystem, Path mountPoint, EnumIntegerSet<MountOption> mountOptions, DokanyOperations dokanyOperations) {
        this.fileSystem = fileSystem;
        this.dokanOptions = new DokanOptions(mountPoint.toString(), (short) 5, mountOptions, null, 3000, 4096, 512);
        this.mountPoint = Path.of(dokanOptions.MountPoint.toString());
        this.volumeName = "DOKAN";
        this.volumeSerialNumber = 30975;
        this.mounted = new AtomicBoolean(false);
        this.dokanyOperations = new DokanyOperations();
    }

    public DokanyMountPoint(DokanyFileSystem fileSystem, Path mountPoint, EnumIntegerSet<MountOption> mountOptions) {
        this.fileSystem = fileSystem;
        this.dokanOptions = new DokanOptions(mountPoint.toString(), (short) 5, mountOptions, null, 3000, 4096, 512);
        this.mountPoint = Path.of(dokanOptions.MountPoint.toString());
        this.volumeName = "DOKAN";
        this.volumeSerialNumber = 30975;
        this.mounted = new AtomicBoolean(false);
        this.dokanyOperations = new DokanyOperations();
    }

    public synchronized CompletableFuture<Void> mountAsync() {
        return CompletableFuture
                .runAsync(() -> {
                    int mountStatus = NativeMethods.DokanMain(dokanOptions, dokanyOperations);
                    MountError error = MountError.fromInt(mountStatus);
                    if (error == MountError.SUCCESS) {
                        mounted.set(true);
                    } else {
                        mounted.set(false);
                        throw new DokanyException("Negative result of mount operation. Code" + mountStatus + " -- " + MountError.fromInt(mountStatus).getDescription(), mountStatus);
                    }
                });
    }


    public synchronized void mount() {
        int mountStatus = NativeMethods.DokanMain(dokanOptions, dokanyOperations);
        MountError error = MountError.fromInt(mountStatus);
        if (error == MountError.SUCCESS) {
            mounted.set(true);
        } else {
            mounted.set(false);
            throw new DokanyException("Negative result of mount operation. Code" + mountStatus + " -- " + MountError.fromInt(mountStatus).getDescription(), mountStatus);
        }
    }

//    public synchronized void mount(boolean blocking) {
//
//        try {
//            int mountStatus;
//
////            if (DokanyUtils.canHandleShutdownHooks()) {
////                Runtime.getRuntime().addShutdownHook(new Thread(this::close));
////            }
//
//            if (blocking) {
//                mountStatus = NativeMethods.DokanMain(dokanOptions, dokanyOperations);
//            } else {
//                try {
//                    mountStatus = CompletableFuture
//                            .supplyAsync(() -> NativeMethods.DokanMain(dokanOptions, dokanyOperations))
//                            .get(dokanOptions.Timeout, TimeUnit.MILLISECONDS);
//                } catch (TimeoutException e) {
//                    // ok
//                    mountStatus = 0;
//                }
//                isMounted.set(true);
//            }
//            if (mountStatus < 0) {
//                throw new DokanyException("Negative result of mount operation. Code" + mountStatus + " -- " + MountError.fromInt(mountStatus).getDescription(), mountStatus);
//            }
//        } catch (Exception e) {
//            throw new DokanyException("Unable to mount filesystem.", e);
//        }
//    }





    @Override
    public synchronized void close() {
//        if (!volumeIsStillMounted()) {
//            running.set(false);
//        }

        if (mounted.get()) {
            if (NativeMethods.DokanRemoveMountPoint(new WString(mountPoint.toAbsolutePath().toString()))) {
                mounted.set(false);
            } else {
                throw new DokanyException("Unmount " + volumeName + "(" + mountPoint + ") failed. Try again, shut down JVM or use dokanctl.exe to unmount manually.");
            }
        }
    }

    public boolean isMounted() {
        return mounted.get();
    }

//    private boolean volumeIsStillMounted() {
//        char[] mntPtCharArray = mountPoint.toAbsolutePath().toString().toCharArray();
//        LongByReference length = new LongByReference();
//        Pointer startOfList = NativeMethods.DokanGetMountPointList(false, length);
//        List<DokanControl> list = DokanControl.getDokanControlList(startOfList, length.getValue());
//        // It is not enough for the entry.DokanyMountPoint to contain the actual mount point. It also has to ends afterwards.
//        boolean mountPointInList = list.stream().anyMatch(entry ->
//                Arrays.equals(entry.MountPoint, 12, 12 + mntPtCharArray.length, mntPtCharArray, 0, mntPtCharArray.length)
//                && (entry.MountPoint.length == 12 + mntPtCharArray.length || entry.MountPoint[12 + mntPtCharArray.length] == '\0'));
//        NativeMethods.DokanReleaseMountPointList(startOfList);
//        return mountPointInList;
//    }


}
