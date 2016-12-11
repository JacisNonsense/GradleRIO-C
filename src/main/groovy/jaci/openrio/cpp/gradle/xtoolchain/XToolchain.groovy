package jaci.openrio.cpp.gradle.xtoolchain

import jaci.openrio.cpp.gradle.GradleRIO_C
import org.gradle.internal.os.OperatingSystem
import org.gradle.api.*

interface XToolchainBase {
    boolean canApply(OperatingSystem os)
    Task apply(Project project)
    // WARNING!!!!
    // Do not trust that GradleRIO owns the file given by get_toolchain_root.
    // On Windows and Mac systems, it is installed under ~/.gradle/gradlerioc/toolchain
    // by default, HOWEVER, on Linux systems, it is installed under /usr as packages
    // are installed by apt. DO NOT remove files from this directory.
    // For this reason, no 'clean_frc_toolchain' task is provided.
    File get_toolchain_root()
}

class XToolchain {
    static def url_base = "http://first.wpi.edu/FRC/roborio/toolchains/"

    static File download_file(Project project, String platform, String filename) {
        def dest = new File(GradleRIO_C.getGlobalDirectory(), "cache/${platform}")
        dest.mkdirs()
        return new File(dest, filename)
    }

    static File get_toolchain_extraction_dir(String platform) {
        return new File(GradleRIO_C.getGlobalDirectory(), "toolchain/${platform}").absoluteFile
    }

    static void download_xtoolchain_file(Project project, String platform, String filename) {
        def dlfile = download_file(project, platform, filename)
        if (!dlfile.exists()) {
            new URL(url_base + filename).withInputStream{ i -> dlfile.withOutputStream{ it << i }}
        }
    }
}