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
        JenkinsCakeBuild jenkinsCakeBuild = new JenkinsCakeBuild(null, null, null);

        jenkinsCakeBuild.setParameters(null,null, null, defaultValueProvider);

        assertEquals("./build.sh", jenkinsCakeBuild.getBootstrapperScipt());
        assertEquals(null, jenkinsCakeBuild.getTarget());
        assertEquals(null, jenkinsCakeBuild.getArguments());
    }

    @Test
    public void setParameters_AllSet_SetsMembersAllToInput() {
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsLinux()).thenReturn(true);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);
        JenkinsCakeBuild jenkinsCakeBuild = new JenkinsCakeBuild(null, null, null);

        jenkinsCakeBuild.setParameters("booty","targy", "argy", defaultValueProvider);

        assertEquals("booty", jenkinsCakeBuild.getBootstrapperScipt());
        assertEquals("--target targy", jenkinsCakeBuild.getTarget());
        assertEquals("argy", jenkinsCakeBuild.getArguments());
    }

    @Test
    public void buildCakeCommand_Input_ReturnsCorrectCommand(){
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsLinux()).thenReturn(true);
        JenkinsCakeBuild jenkinsCakeBuild = new JenkinsCakeBuild("boot.sh", "mytarget", "-foo=\"bar\"");

        String command = jenkinsCakeBuild.buildCakeCommand();

        assertEquals("boot.sh --target mytarget -foo=\"bar\"", command);
    }

    @Test
    public void buildCakeCommand_NoInput_ReturnsCorrectCommand(){
        OSChecker osChecker = mock(OSChecker.class);
        when(osChecker.IsLinux()).thenReturn(true);
        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(osChecker);
        JenkinsCakeBuild jenkinsCakeBuild = new JenkinsCakeBuild(null, null, null);
        jenkinsCakeBuild.setParameters(null, null, null, defaultValueProvider);

        String command = jenkinsCakeBuild.buildCakeCommand();

        assertEquals("./build.sh", command);
    }

}