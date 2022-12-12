package com.leandoer.tracer.repository

import com.leandoer.tracer.model.entity.Trace
import com.leandoer.tracer.repository.projection.CountStatistics
import com.leandoer.tracer.repository.projection.Distribution
import com.leandoer.tracer.repository.projection.LabelDistribution
import com.leandoer.tracer.repository.projection.Rate
import com.leandoer.tracer.repository.projection.TestStatistics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TraceRepository : JpaRepository<Trace, Long>, JpaSpecificationExecutor<Trace> {

    @Query(
        value = """
        with series as (
            select generate_series(
                    :start_timestamp,
                    :end_timestamp,
                    (interval '1' minute) * :interval
                       ) AS range
        ), series_with_end_date as (
            select 
                range as start_timestamp,
                lead(range) over (order by range) as end_timestamp
            from series
        ), series_with_middle as (
            select 
                start_timestamp,
                end_timestamp,
                start_timestamp + (end_timestamp - start_timestamp) / 2 as middle_timestamp
            from series_with_end_date
            where end_timestamp is not null
        ), results as (
        select 
            name as label, 
            middle_timestamp as timestamp, 
            count(t.id)
        from tracer.applications a
        full outer join series_with_middle s on true
        left join tracer.traces t
            on t.generated_at >= start_timestamp
            and t.generated_at < end_timestamp
            and a.id = t.application_id
        group by 1,2
        
        union all 
    
        select 
            'all', 
            middle_timestamp, 
            count(t.id)
        from tracer.traces t
        right join series_with_middle
            on t.generated_at >= start_timestamp
            and t.generated_at < end_timestamp
        group by 1,2
        )
        select * from results 
        order by timestamp
        """,
        nativeQuery = true
    )
    fun getTracesRateByApplication(
        @Param("start_timestamp") startTimestamp: LocalDateTime,
        @Param("end_timestamp") endTimestamp: LocalDateTime,
        @Param("interval") interval: Int,
    ): List<Rate>


    @Query(
        value = """
        with series as (
            select generate_series(
                          :start_timestamp,
                          :end_timestamp,
                           (interval '1' minute) * :interval
                       ) AS range
        ), series_with_end_date as (
            select
                range as start_timestamp,
                lead(range) over (order by range) as end_timestamp
            from series
        ), series_with_middle as (
            select
                start_timestamp,
                end_timestamp,
                start_timestamp + (end_timestamp - start_timestamp) / 2 as middle_timestamp
            from series_with_end_date
            where end_timestamp is not null
        ), results as (
            select
                name as label,
                middle_timestamp as timestamp,
                count(ae.id)
            from tracer.applications a
                     full outer join series_with_middle s on true
                     left join tracer.alert_events ae
                     inner join tracer.traces t on ae.trace_id = t.id
                               on t.generated_at >= start_timestamp
                                   and t.generated_at < end_timestamp
                                   and a.id = t.application_id
            group by 1,2
        
            union all
        
            select
                'all',
                middle_timestamp,
                count(ae.id)
            from tracer.alert_events ae
                     inner join tracer.traces t on ae.trace_id = t.id
                     right join series_with_middle
                                on t.generated_at >= start_timestamp
                                    and t.generated_at < end_timestamp
            group by 1,2
        )
        select * from results
        order by timestamp
        """, nativeQuery = true
    )
    fun getAlertEventsRateByApplication(
        @Param("start_timestamp") startTimestamp: LocalDateTime,
        @Param("end_timestamp") endTimestamp: LocalDateTime,
        @Param("interval") interval: Int,
    ): List<Rate>

    @Query(
        value = """
        with series as (
            select generate_series(
                    :start_timestamp,
                    :end_timestamp,
                    (interval '1' minute) * :interval
                       ) AS range
        ), series_with_end_date as (
            select 
                range as start_timestamp,
                lead(range) over (order by range) as end_timestamp
            from series
        ), series_with_middle as (
            select 
                start_timestamp,
                end_timestamp,
                start_timestamp + (end_timestamp - start_timestamp) / 2 as middle_timestamp
            from series_with_end_date
            where end_timestamp is not null
        ), results as (
        select 
            l.label as label, 
            middle_timestamp as timestamp, 
            count(t.id)
        from tracer.labels l
        full outer join series_with_middle s on true
        left join tracer.label_trace lt 
            on l.id = lt.label_id 
        left join tracer.traces t
            on t.generated_at >= start_timestamp
            and t.generated_at < end_timestamp
            and lt.trace_id = t.id
        group by 1,2
        
        union all 
    
        select 
            'all', 
            middle_timestamp, 
            count(t.id)
        from tracer.traces t
        right join series_with_middle
            on t.generated_at >= start_timestamp
            and t.generated_at < end_timestamp
        group by 1,2
        )
        select * from results 
        order by timestamp
    """,
        nativeQuery = true
    )
    fun getTracesRateByLabel(
        @Param("start_timestamp") startTimestamp: LocalDateTime,
        @Param("end_timestamp") endTimestamp: LocalDateTime,
        @Param("interval") interval: Int,
    ): List<Rate>

    @Query(
        value = """
        with series as (
            select generate_series(
                           :start_timestamp,
                           :end_timestamp,
                           (interval '1' minute) * :interval
                       ) AS range
        ), series_with_end_date as (
            select
                range as start_timestamp,
                lead(range) over (order by range) as end_timestamp
            from series
        ), series_with_middle as (
            select
                start_timestamp,
                end_timestamp,
                start_timestamp + (end_timestamp - start_timestamp) / 2 as middle_timestamp
            from series_with_end_date
            where end_timestamp is not null
        ), results as (
            select
                l.label as label,
                middle_timestamp as timestamp,
                count(ae.id)
            from tracer.labels l
                     full outer join series_with_middle s on true
                     left join tracer.label_trace lt
                               on l.id = lt.label_id
                     left join tracer.alert_events ae
                     inner join tracer.traces t on ae.trace_id = t.id
                               on t.generated_at >= start_timestamp
                                   and t.generated_at < end_timestamp
                                   and lt.trace_id = t.id
            group by 1,2
        
            union all
        
            select
                'all',
                middle_timestamp,
                count(ae.id)
            from tracer.alert_events ae
                     inner join tracer.traces t on ae.trace_id = t.id
                     right join series_with_middle
                                on t.generated_at >= start_timestamp
                                    and t.generated_at < end_timestamp
            group by 1,2
        )
        select * from results
        order by timestamp
        """,
        nativeQuery = true
    )
    fun getAlertEventsRateByLabel(
        @Param("start_timestamp") startTimestamp: LocalDateTime,
        @Param("end_timestamp") endTimestamp: LocalDateTime,
        @Param("interval") interval: Int,
    ): List<Rate>

    @Query(
        value = """
        select 
            a.name as application,
            round(cast(count(*) as decimal)/sum(count(*)) OVER ()*100, 2) as percentage
        from tracer.traces
        join tracer.applications a on a.id = traces.application_id
        where tracer.traces.generated_at >= :start_timestamp
        and tracer.traces.generated_at < :end_timestamp 
        group by 1
        """,
        nativeQuery = true
    )
    fun getTracesDistribution(
        @Param("start_timestamp") startTimestamp: LocalDateTime,
        @Param("end_timestamp") endTimestamp: LocalDateTime
    ): List<Distribution>

    @Query(
        value = """
        select
            a.name as application,
            round(cast(count(*) as decimal)/sum(count(*)) OVER ()*100, 2) as percentage
        from tracer.alert_events ae
        inner join tracer.traces t on ae.trace_id = t.id
        join tracer.applications a on a.id = t.application_id
        where t.generated_at >= :start_timestamp
        and t.generated_at < :end_timestamp
        group by 1
        """,
        nativeQuery = true
    )
    fun getAlertEventsDistribution(
        @Param("start_timestamp") startTimestamp: LocalDateTime,
        @Param("end_timestamp") endTimestamp: LocalDateTime
    ): List<Distribution>


    @Query(
        value = """
        with total_applications as (
            select count(*) as applications
            from tracer.applications
        ), total_labels as (
            select count(*) as labels
            from tracer.labels
        ), total_traces as (
            select count(*) as traces
            from tracer.traces
        ), total_alerts as (
            select count(*) as alerts
            from tracer.alerts
        ), total_alert_events as (
            select count(*) as alertEvents
            from tracer.alert_events
        ), new_alert_events as (
            select count(*) as alertEventsNew
            from tracer.alert_events
            where tracer.alert_events.status='NEW'
        ), resolved_alert_events as (
            select count(*) as alertEventsResolved
            from tracer.alert_events
            where tracer.alert_events.status='RESOLVED'
        )
        select
            total_applications.applications,
            total_labels.labels,
            total_traces.traces,
            total_alerts.alerts,
            total_alert_events.alertEvents,
            new_alert_events.alertEventsNew,
            resolved_alert_events.alertEventsResolved
        from total_applications, 
             total_labels,
             total_traces,
             total_alerts,
             total_alert_events,
             new_alert_events,
             resolved_alert_events
        """,
        nativeQuery = true
    )
    fun getCountStatistics(): CountStatistics


    @Query(
        value = """
        select 
            t.name as testType,
            t.type,
            count(distinct tr.trace_id) as count
        from tracer.tests t
        left join tracer.test_runs tr
            on t.id = tr.test_id
        where not t.parametrized
        group by 1,2
        order by count desc
        """,
        nativeQuery = true
    )
    fun getTestStatistics(): List<TestStatistics>

    @Query(
        value = """
        with min_step as (
        select  min(cast(value as decimal))                                  as min_value,
                abs(max(cast(value as decimal)) - min(cast(value as decimal))) / 100 as step
        from tracer.traces t
            join tracer.label_trace lt on t.id = lt.trace_id
            join tracer.labels l on l.id = lt.label_id
        where l.id = :id
            and value !~ '^[0-1]+$'
        ), series as (
        select (select min_value from min_step) + generate_series(0, 99, 1) * (select step from min_step) as starting,
               (select min_value from min_step) + generate_series(1, 100, 1) * (select step from min_step) as ending
        )
        select starting as start, ending as "end", count(t.id) as count
        from series
            left join tracer.traces t on cast(value as decimal) between series.starting and series.ending
            left join tracer.label_trace lt on t.id = lt.trace_id
            left join tracer.labels l on l.id = lt.label_id
        where l.id = :id
        and value !~ '^[0-1]+$'
        group by 1,2
        order by starting
        """,
        nativeQuery = true
    )
    fun getLabelDistribution(@Param("id") labelId: Int): List<LabelDistribution>


    @Query(
        value = """
        with mapping AS (
            select CASE
               WHEN (CAST(value as decimal)) between :st and :ed THEN 1
               else 0
               END VAL
            from tracer.traces
             inner join tracer.label_trace on traces.id = label_trace.trace_id
             inner join tracer.labels on label_trace.label_id = labels.id
            where labels.id = :label
                and traces.value !~ '^[0-1]+$'
            ORDER BY generated_at
)
SELECT string_agg(CAST(mapping.VAL as varchar), '') FROM mapping
        """,
        nativeQuery = true
    )
    fun getBinarySequence(
        @Param("st") min: Long,
        @Param("ed") max: Long,
        @Param("label") label: Int
    ): String

}