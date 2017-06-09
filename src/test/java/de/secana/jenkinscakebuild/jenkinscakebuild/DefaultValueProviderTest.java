package de.secana.jenkinscakebuild.jenkinscakebuild;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultValueProviderTest {

    @Test
    public void getBootstrapperScriptName_isLinux_ReturnsShellScript() {
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsLinux()).thenReturn(true);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);

        String bootstrapper = defaultValueProvider.GetBootstrapperScriptName();

        assertEquals("./build.sh", bootstrapper);
    }

    @Test
    public void getBootstrapperScriptName_isWindows_ReturnsPSScript() {
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsWindows()).thenReturn(true);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);

        String bootstrapper = defaultValueProvider.GetBootstrapperScriptName();

        assertEquals("build.ps1", bootstrapper);
    }

    @Test
    public void getBootstrapperScriptName_isUnknown_ReturnsShellScript() {
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsLinux()).thenReturn(false);
        when(osChecker.IsWindows()).thenReturn(false);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);

        String bootstrapper = defaultValueProvider.GetBootstrapperScriptName();

        assertEquals("./build.sh", bootstrapper);
    }

    @Test
    public void getTargetParameter_isLinux_ReturnsShellParam() {
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsLinux()).thenReturn(true);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);

        String targetParameter = defaultValueProvider.GetTargetParameter();

        assertEquals("--target", targetParameter);
    }

    @Test
    public void getTargetParameter_isWindows_ReturnsPSParam() {
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsWindows()).thenReturn(true);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);

        String targetParameter = defaultValueProvider.GetTargetParameter();

        assertEquals("-Target", targetParameter);
    }

    @Test
    public void getTargetParameter_isUnknown_ReturnsShellParam() {
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsWindows()).thenReturn(false);
        when(osChecker.IsLinux()).thenReturn(false);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);

        String targetParameter = defaultValueProvider.GetTargetParameter();

        assertEquals("--target", targetParameter);
    }

}