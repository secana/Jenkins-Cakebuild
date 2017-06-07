package de.secana.jenkinscakebuild.jenkinscakebuild;

import org.apache.commons.lang.SystemUtils;

public class OSChecker implements IOSChecker {
    @Override
    public boolean IsLinux() {
        return SystemUtils.IS_OS_LINUX;
    }

    @Override
    public boolean IsWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    @Override
    public boolean IsUnknown() {
        return !IsLinux() && !IsWindows();
    }
}
