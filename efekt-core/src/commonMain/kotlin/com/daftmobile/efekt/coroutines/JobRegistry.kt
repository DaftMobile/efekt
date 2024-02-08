package com.daftmobile.efekt.coroutines

import com.daftmobile.efekt.StateContext
import kotlinx.coroutines.Job

public interface JobRegistry : StateContext.Element {

    public override val key: Key get() = Key

    public fun register(token: Any, job: Job)

    public fun get(token: Any): Job?

    public fun remove(token: Any): Job?

    public companion object Key : StateContext.Key<JobRegistry>
}

public fun defaultJobRegistry(): JobRegistry = JobRegistryImpl()

public val StateContext.jobRegistry: JobRegistry get() = get(JobRegistry)

public fun JobRegistry.cancel(token: Any) {
    remove(token)?.cancel()
}

private class JobRegistryImpl : JobRegistry {

    private val jobsMap = mutableMapOf<Any, Job>()

    override fun register(token: Any, job: Job) {
        jobsMap[token] = job
    }

    override fun get(token: Any): Job? = jobsMap[token]

    override fun remove(token: Any) = jobsMap.remove(token)
}
