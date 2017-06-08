package de.secana.jenkinscakebuild.jenkinscakebuild;

public class DefaultValueProvider implements IDefaultValueProvider {

    private final IOSChecker osChecker;

    public DefaultValueProvider(IOSChecker osChecker)
    {
        this.osChecker = osChecker;
    }

    @Override
    public String GetBootstrapperScriptName(){
        if(osChecker.IsLinux())
            return "build.sh";

        if(osChecker.IsWindows())
            return "build.ps1";

        // assume some Unix if not Linux or Windows
        return "build.sh";
    }

    @Override
    public String GetTargetParameter(){
        if(osChecker.IsLinux())
            return "--target";

        if(osChecker.IsWindows())
            return "-Target";

        // assume some Unix if not Linux or Windows
        return "--target";
    }
}
