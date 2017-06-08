package de.secana.jenkinscakebuild.jenkinscakebuild;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JenkinsCakeBuildTest {

    @Test
    public void setParameters_AllNull_SetsMembersAllToDefault() {
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsLinux()).thenReturn(true);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);
        JenkinsCakeBuild jenkinsCakeBuild = new JenkinsCakeBuild(null, null, null, null);

        jenkinsCakeBuild.SetParameters(null, null, null, null, defaultValueProvider);

        assertEquals("build.sh", jenkinsCakeBuild.getBootstrapperScipt());
        assertEquals("build.cake", jenkinsCakeBuild.getCakeScript());
        assertEquals(null, jenkinsCakeBuild.getTarget());
        assertEquals(null, jenkinsCakeBuild.getArguments());
    }

    @Test
    public void setParameters_AllSet_SetsMembersAllToInput() {
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsLinux()).thenReturn(true);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);
        JenkinsCakeBuild jenkinsCakeBuild = new JenkinsCakeBuild(null, null, null, null);

        jenkinsCakeBuild.SetParameters("booty", "cakey", "targy", "argy", defaultValueProvider);

        assertEquals("booty", jenkinsCakeBuild.getBootstrapperScipt());
        assertEquals("cakey", jenkinsCakeBuild.getCakeScript());
        assertEquals("--target targy", jenkinsCakeBuild.getTarget());
        assertEquals("argy", jenkinsCakeBuild.getArguments());
    }

}