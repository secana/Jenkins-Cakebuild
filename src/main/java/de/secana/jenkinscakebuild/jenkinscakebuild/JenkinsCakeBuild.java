package de.secana.jenkinscakebuild.jenkinscakebuild;
import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
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
public class JenkinsCakeBuild extends Builder implements SimpleBuildStep {

    private String bootstrapperScipt;
    private String cakeScript;
    private String target;
    private String arguments;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public JenkinsCakeBuild(String bootstrapperScipt, String cakeScript, String target, String arguments) {

        DefaultValueProvider defaultValueProvider = new DefaultValueProvider(new OSChecker());
        SetParameters(bootstrapperScipt, cakeScript, target, arguments, defaultValueProvider);
    }

    public void SetParameters(
            String bootstrapperScipt,
            String cakeScript,
            String target,
            String arguments,
            DefaultValueProvider defaultValueProvider)
    {
        this.arguments = arguments;

        if(target != null)
        {
            this.target = defaultValueProvider.GetTargetParameter() + " " + target;
        }

        if(bootstrapperScipt == null) {
            this.bootstrapperScipt = defaultValueProvider.GetBootstrapperScriptName();
        }
        else {
            this.bootstrapperScipt = bootstrapperScipt;
        }

        if(cakeScript == null) {
            this.cakeScript = defaultValueProvider.GetCakeScriptName();
        }
        else {
            this.cakeScript = cakeScript;
        }
    }

    /**
     * We'll use this from the {@code config.jelly}.
     */
    public String getBootstrapperScipt() {
        return bootstrapperScipt;
    }

    public String getCakeScript() { return cakeScript; }

    public String getTarget() { return target; }

    public String getArguments() { return arguments; }

    @Override
    public void perform(Run<?,?> build, FilePath workspace, Launcher launcher, TaskListener listener) {
        // This is where you 'build' the project.
        // Since this is a dummy, we just say 'hello world' and call that a build.

        listener.getLogger().println("Bootstrapper script: " + bootstrapperScipt);
        listener.getLogger().println("Cake script: " + cakeScript);
        listener.getLogger().println("Target: " + target);
        listener.getLogger().println("Arguments: " + arguments);
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
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use {@code transient}.
         */
        private boolean useFrench;

        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user. 
         */
        public FormValidation doCheckName(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a name");
            if (value.length() < 4)
                return FormValidation.warning("Isn't the name too short?");
            return FormValidation.ok();
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
            useFrench = formData.getBoolean("useFrench");
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }

        /**
         * This method returns true if the global configuration says we should speak French.
         *
         * The method name is bit awkward because global.jelly calls this method to determine
         * the initial state of the checkbox by the naming convention.
         */
        public boolean getUseFrench() {
            return useFrench;
        }
    }
}

