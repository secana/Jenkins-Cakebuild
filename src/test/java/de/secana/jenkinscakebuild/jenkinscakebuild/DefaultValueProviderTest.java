package de.secana.jenkinscakebuild.jenkinscakebuild;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class DefaultValueProviderTest {
    @Test
    public void getCakeScriptName() {
        OSChecker osChecker = new OSChecker();
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);

        String scriptName = defaultValueProvider.GetCakeScriptName();

        assertEquals("build.cake", scriptName);
    }

    @Test
    public void getBootstrapperScriptName() throws Exception {
    }

    @Test
    public void getTargetParameter() throws Exception {
    }

}