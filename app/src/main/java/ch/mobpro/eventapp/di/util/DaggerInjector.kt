package ch.mobpro.eventapp.di.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import ch.mobpro.eventapp.base.BaseApplication
import ch.mobpro.eventapp.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjection


object DaggerInjector {

    fun injectAll(application: BaseApplication) {
        DaggerApplicationComponent.builder()
            .application(application)
            .build().inject(application)
        application
            .registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    injectComponents(activity)
                }

                override fun onActivityStarted(activity: Activity) {

                }

                override fun onActivityResumed(activity: Activity) {

                }

                override fun onActivityPaused(activity: Activity) {

                }

                override fun onActivityStopped(activity: Activity) {

                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

                }

                override fun onActivityDestroyed(activity: Activity) {

                }
            })
    }

    private fun injectComponents(activity: Activity) {
        if (activity is Injectable) {
            AndroidInjection.inject(activity)
        }
//        (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
//                object : FragmentManager.FragmentLifecycleCallbacks() {
//                    override fun onFragmentCreated(fm: FragmentManager?, f: Fragment?,
//                                                   savedInstanceState: Bundle?) {
//                        if (f is Injectable) {
//                            AndroidSupportInjection.inject(f)
//                        }
//                    }
//                }, true)
    }
}