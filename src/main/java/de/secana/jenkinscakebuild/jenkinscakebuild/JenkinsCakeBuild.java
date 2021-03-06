package de.secana.jenkinscakebuild.jenkinscakebuild;
import hudson.Extension;
import hudson.Launcher;
import hudson.Proc;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link JenkinsCakeBuild} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #bootstrapperScipt})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform} method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class JenkinsCakeBuild extends Builder {

    private String bootstrapperScipt;
    private String target;
    private String arguments;


    private String intenralBoostrapperScript;
    private String internalTarget;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public JenkinsCakeBuild(String bootstrapperScipt, String target, String arguments) {

        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(new OSChecker());
        setParameters(bootstrapperScipt, target, arguments, defaultValueProvider);
    }

    public void setParameters(
            String bootstrapperScipt,
            String target,
            String arguments,
            DefaultValueProvider defaultValueProvider)
    {
        this.arguments = arguments;
        this.target = target;

        if(!isEmptyOrNull(target))
            this.internalTarget = defaultValueProvider.GetTargetParameter() + " " + target;
        else
            this.internalTarget = null;

        if(isEmptyOrNull(bootstrapperScipt)) {
            this.bootstrapperScipt = defaultValueProvider.GetBootstrapperScriptName();
        }
        else {
            this.bootstrapperScipt = bootstrapperScipt;
        }
    }

    private boolean isEmptyOrNull(String s){
        if(s == null)
            return true;

        if(s.trim().isEmpty())
            return true;

        return false;
    }

    public String buildCakeCommand(){
        String format = String.format("%s %s %s", nullToEmpty(bootstrapperScipt), nullToEmpty(internalTarget), nullToEmpty(arguments));
        return format.trim();
    }

    private String nullToEmpty(String s){
        if(isEmptyOrNull(s))
            return "";

        return s;
    }

    /**
     * We'll use this from the {@code config.jelly}.
     */
    public String getBootstrapperScipt() {
        return bootstrapperScipt;
    }

    public String getTarget() { return target; }

    public String getArguments() { return arguments; }

    public String getInternalTarget() { return internalTarget; }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        // This is where you 'build' the project.

        listener.getLogger().println("Bootstrapper script: " + nullToEmpty(bootstrapperScipt));
        listener.getLogger().println("Target: " + nullToEmpty(target));
        listener.getLogger().println("Arguments: " + nullToEmpty(arguments));

        String command = buildCakeCommand();

        listener.getLogger().println("Command: " + command);

        try {
            Proc proc = launcher.launch(command, build.getEnvVars(), listener.getLogger(), build.getProject().getWorkspace());
            int exitCode = proc.join();
            return exitCode == 0;
        } catch (IOException e) {
            e.printStackTrace();
            listener.getLogger().println("IOException !");
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            listener.getLogger().println("InterruptedException!");
            return false;
        }

    }


    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link JenkinsCakeBuild}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See {@code src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly}
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }


        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Cake Build";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }
    }
}

