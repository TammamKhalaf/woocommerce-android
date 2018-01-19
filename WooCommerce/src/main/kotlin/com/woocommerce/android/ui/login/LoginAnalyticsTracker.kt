package com.woocommerce.android.ui.login

import com.woocommerce.android.analytics.AnalyticsTracker
import org.wordpress.android.fluxc.store.AccountStore
import org.wordpress.android.fluxc.store.SiteStore
import org.wordpress.android.login.LoginAnalyticsListener
import java.util.HashMap
import javax.inject.Singleton

@Singleton
class LoginAnalyticsTracker : LoginAnalyticsListener {
    override fun trackAnalyticsSignIn(accountStore: AccountStore, siteStore: SiteStore, isWpcomLogin: Boolean) {
        refreshMetadata(accountStore, siteStore)
        val properties = HashMap<String, Boolean>()
        properties.put("dotcom_user", isWpcomLogin) // checkstyle ignore
        AnalyticsTracker.track(AnalyticsTracker.Stat.SIGNED_IN, properties)
        if (!isWpcomLogin) {
            AnalyticsTracker.track(AnalyticsTracker.Stat.ADDED_SELF_HOSTED_SITE)
        }
    }

    override fun trackEmailFormViewed() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_EMAIL_FORM_VIEWED)
    }

    override fun trackInsertedInvalidUrl() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_INSERTED_INVALID_URL)
    }

    override fun trackLoginFailed(errorContext: String, errorType: String, errorDescription: String) {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_FAILED, errorContext, errorType, errorDescription)
    }

    override fun trackMagicLinkFailed(properties: Map<String, *>) {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_MAGIC_LINK_FAILED, properties)
    }

    override fun trackMagicLinkOpenEmailClientViewed() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_MAGIC_LINK_OPEN_EMAIL_CLIENT_VIEWED)
    }

    override fun trackMagicLinkRequested() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_MAGIC_LINK_REQUESTED)
    }

    override fun trackMagicLinkRequestFormViewed() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_MAGIC_LINK_REQUEST_FORM_VIEWED)
    }

    override fun trackPasswordFormViewed() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_PASSWORD_FORM_VIEWED)
    }

    override fun trackSocialAccountsNeedConnecting() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_SOCIAL_ACCOUNTS_NEED_CONNECTING)
    }

    override fun trackSocialButtonClick() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_SOCIAL_BUTTON_CLICK)
    }

    override fun trackSocialButtonFailure() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_SOCIAL_BUTTON_FAILURE)
    }

    override fun trackSocialConnectFailure() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_SOCIAL_CONNECT_FAILURE)
    }

    override fun trackSocialConnectSuccess() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_SOCIAL_CONNECT_SUCCESS)
    }

    override fun trackSocialErrorUnknownUser() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_SOCIAL_ERROR_UNKNOWN_USER)
    }

    override fun trackSocialFailure(errorContext: String, errorType: String, errorDescription: String) {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_SOCIAL_FAILURE, errorContext, errorType, errorDescription)
    }

    override fun trackTwoFactorFormViewed() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_TWO_FACTOR_FORM_VIEWED)
    }

    override fun trackUrlFormViewed() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_URL_FORM_VIEWED)
    }

    override fun trackUrlHelpScreenViewed() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_URL_HELP_SCREEN_VIEWED)
    }

    override fun trackUsernamePasswordFormViewed() {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_USERNAME_PASSWORD_FORM_VIEWED)
    }

    override fun trackWpComBackgroundServiceUpdate(properties: Map<String, *>) {
        AnalyticsTracker.track(AnalyticsTracker.Stat.LOGIN_WPCOM_BACKGROUND_SERVICE_UPDATE, properties)
    }

    private fun refreshMetadata(accountStore: AccountStore, siteStore: SiteStore) {
        // TODO
    }

    /**
     * @return true if the siteStore has sites accessed via the WPCom Rest API that are not WPCom sites. This only
     * counts Jetpack sites connected via WPCom Rest API. If there are Jetpack sites in the site store and they're
     * all accessed via XMLRPC, this method returns false.
     */
    private fun isJetpackUser(siteStore: SiteStore): Boolean =
            siteStore.sitesAccessedViaWPComRestCount - siteStore.wpComSitesCount > 0
}
