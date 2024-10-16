package com.example.plugins

import com.example.task.table.TaskTable
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobExecutionContext
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Job that will be executed to delete all task that have been deleted by the user for
 * more than 30 days.
 */
class DeleteTasksJob : Job {
    override fun execute(p0: JobExecutionContext?) {
        val thirtyDayAgo = LocalDateTime.now().minusDays(30)
        transaction {
            TaskTable
                .deleteWhere {
                    dateDeleted lessEq thirtyDayAgo
                }
        }
    }
}

fun Application.configureTasks() {
    // Creating the scheduler.
    val scheduler = StdSchedulerFactory.getDefaultScheduler()

    // Creating the job.
    val job = JobBuilder.newJob(DeleteTasksJob::class.java)
        .withIdentity("task_delete", "tasks")
        .build()

    // Creating the trigger.
    val trigger = TriggerBuilder.newTrigger()
        .withIdentity("task_delete_trigger", "tasks")
        .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))
        .build()

    // Scheduling the job and starting
    scheduler.scheduleJob(job, trigger)
    scheduler.start()

    // Stopping the tasks when the server stops.
    environment.monitor.subscribe(ApplicationStopping) {
        scheduler.shutdown()
    }
}


