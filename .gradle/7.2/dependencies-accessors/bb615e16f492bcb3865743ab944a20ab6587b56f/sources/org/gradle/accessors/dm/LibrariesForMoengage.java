package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `moengage` extension.
*/
@NonNullApi
public class LibrariesForMoengage extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(providers, config);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForMoengage(DefaultVersionCatalog config, ProviderFactory providers) {
        super(config, providers);
    }

        /**
         * Creates a dependency provider for cardsCore (com.moengage:cards-core)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getCardsCore() { return create("cardsCore"); }

        /**
         * Creates a dependency provider for cardsUi (com.moengage:cards-ui)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getCardsUi() { return create("cardsUi"); }

        /**
         * Creates a dependency provider for core (com.moengage:moe-android-sdk)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getCore() { return create("core"); }

        /**
         * Creates a dependency provider for deviceTrigger (com.moengage:realtime-trigger)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getDeviceTrigger() { return create("deviceTrigger"); }

        /**
         * Creates a dependency provider for geofence (com.moengage:geofence)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getGeofence() { return create("geofence"); }

        /**
         * Creates a dependency provider for inapp (com.moengage:inapp)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getInapp() { return create("inapp"); }

        /**
         * Creates a dependency provider for inboxCore (com.moengage:inbox-core)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getInboxCore() { return create("inboxCore"); }

        /**
         * Creates a dependency provider for inboxUi (com.moengage:inbox-ui)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getInboxUi() { return create("inboxUi"); }

        /**
         * Creates a dependency provider for integrationVerifier (com.moengage:integration-verifier)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getIntegrationVerifier() { return create("integrationVerifier"); }

        /**
         * Creates a dependency provider for moengageSegmentIntegration (com.moengage:moengage-segment-integration)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getMoengageSegmentIntegration() { return create("moengageSegmentIntegration"); }

        /**
         * Creates a dependency provider for pushAmp (com.moengage:push-amp)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getPushAmp() { return create("pushAmp"); }

        /**
         * Creates a dependency provider for pushAmpPlus (com.moengage:push-amp-plus)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getPushAmpPlus() { return create("pushAmpPlus"); }

        /**
         * Creates a dependency provider for pushKit (com.moengage:hms-pushkit)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getPushKit() { return create("pushKit"); }

        /**
         * Creates a dependency provider for richNotification (com.moengage:rich-notification)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getRichNotification() { return create("richNotification"); }

        /**
         * Creates a dependency provider for security (com.moengage:security)
         * This dependency was declared in catalog com.moengage:android-dependency-catalog:2.7.7
         */
        public Provider<MinimalExternalModuleDependency> getSecurity() { return create("security"); }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a dependency bundle provider for all which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.moengage:moe-android-sdk</li>
             *    <li>com.moengage:cards-core</li>
             *    <li>com.moengage:cards-ui</li>
             *    <li>com.moengage:geofence</li>
             *    <li>com.moengage:inapp</li>
             *    <li>com.moengage:inbox-ui</li>
             *    <li>com.moengage:hms-pushkit</li>
             *    <li>com.moengage:push-amp</li>
             *    <li>com.moengage:push-amp-plus</li>
             *    <li>com.moengage:realtime-trigger</li>
             *    <li>com.moengage:rich-notification</li>
             *    <li>com.moengage:security</li>
             *    <li>com.moengage:integration-verifier</li>
             * </ul>
             * This bundle was declared in catalog com.moengage:android-dependency-catalog:2.7.7
             */
            public Provider<ExternalModuleDependencyBundle> getAll() { return createBundle("all"); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
