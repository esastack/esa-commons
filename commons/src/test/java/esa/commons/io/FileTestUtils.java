package esa.commons.io;

import sun.security.action.GetPropertyAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.AccessController;
import java.security.SecureRandom;

class FileTestUtils {
    private static final File tmpdir = new File(AccessController
            .doPrivileged(new GetPropertyAction("java.io.tmpdir")), "esa-commons-test-temp");
    private static volatile boolean TEM_DIR_INIT = false;

    // file name generation
    private static final SecureRandom random = new SecureRandom();

    static File newTemp(String dirPrefix, String fileName) throws IOException {
        if (!TEM_DIR_INIT) {
            initTmpDir();
        }
        File dir = new File(tmpdir, dirPrefix + "_" + randomLong());
        if (dir.exists()) {
            //Ensure that the generated file must not exist
            return newTemp(dirPrefix, fileName);
        }
        File file = new File(dir, fileName);
        file.deleteOnExit();
        dir.deleteOnExit();
        return file;
    }

    private static synchronized void initTmpDir() throws IOException {
        if (!TEM_DIR_INIT) {
            if (!tmpdir.exists()) {
                Files.createDirectories(tmpdir.toPath());
                tmpdir.deleteOnExit();
            }
            TEM_DIR_INIT = true;
        }
    }

    private static String randomLong() {
        long n = random.nextLong();
        if (n == Long.MIN_VALUE) {
            n = 0;      // corner case
        } else {
            n = Math.abs(n);
        }
        return Long.toString(n);
    }

}
