package id.ac.ugm.sv.trpl.glucosemonitoring.rule

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.schedulers.ExecutorScheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.TimeUnit

/**
 * A class that changes the default schedulers for RxJava to immediate for immediate execution in
 * unit tests. This will also make sure the RxJava usage in the code being tested gets run
 * synchronously, which will make it much easier to write the unit tests.
 */
class RxImmediateSchedulerRule : TestRule {
    
    private val immediateScheduler: Scheduler = object : Scheduler() {
        
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            // Hack to prevent stack-overflows in unit tests when scheduling with a delay
            return super.scheduleDirect(run, 0, unit)
        }
        
        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Runnable::run, true, true)
        }
        
    }
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { immediateScheduler }
                RxJavaPlugins.setInitComputationSchedulerHandler { immediateScheduler }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { immediateScheduler }
                RxJavaPlugins.setInitSingleSchedulerHandler { immediateScheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }
                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
    
}