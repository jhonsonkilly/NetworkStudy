package com.xhb.component.volleyplus;

import java.io.File;

/**
 * Created by wei on 2021/2/18 11:33 AM
 */
public class DiskCache implements Cache {

    private FileSupplier mFileSupplier;

    private static final int DEFAULT_MAX_LENGTH = 5 * 1024 * 104;

    private int mMaxLength;

    public DiskCache(FileSupplier fileSupplier, int maxLength) {
        mFileSupplier = fileSupplier;
        mMaxLength = maxLength;
    }

    public DiskCache(FileSupplier fileSupplier) {
        this(fileSupplier, DEFAULT_MAX_LENGTH);
    }

    public DiskCache(final File file, int maxLength) {
        mFileSupplier = new FileSupplier() {
            @Override
            public File get() {
                return file;
            }
        };
        mMaxLength = maxLength;
    }

    public DiskCache(File file) {
        this(file, DEFAULT_MAX_LENGTH);
    }


    interface FileSupplier {
        File get();
    }


}
