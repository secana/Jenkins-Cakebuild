package de.secana.jenkinscakebuild.jenkinscakebuild;

/**
 * Created by hausi on 07.06.17.
 */
public interface IDefaultValueProvider {
    String GetCakeScriptName();

    String GetBootstrapperScriptName();

    String GetTargetParameter();
}
