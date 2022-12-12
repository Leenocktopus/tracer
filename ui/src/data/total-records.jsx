import React, {useEffect, useState} from 'react';
import './data.scss'
import {Card, Empty} from "antd/lib";
import axios from "axios";
import Spinner from "../util/spinner";
import {useErrorUpdate} from "../util/ErrorContext";
import {getCountStatistics, getTestStatistics} from "../util/api-calls";


const TotalRecords = () => {
    const [data, setData] = useState("")
    const [isLoading, setLoading] = useState(false)
    const setError = useErrorUpdate()

    const fetchTotalRecords = async () => {
        setLoading(true)
        const response = await getCountStatistics()
        if (response && response.error) {
            setError()
        } else {
            setData(response)
            setLoading(false)
        }
    }
    useEffect(() => {
        fetchTotalRecords()
    }, [])

    const renderData = () => (
        <>
            <div>
                <span className="count-field">{data.applications}</span>
                <span className="count-label">Програм-клієнтів</span>
            </div>
            <div>
                <span className="count-field">{data.labels}</span>
                <span className="count-label">Міток</span>
            </div>
            <div>
                <span className="count-field">{data.traces}</span>
                <span className="count-label">Випадкових чисел</span>
            </div>
            <div>
                <span className="count-field">{data.alerts}</span>
                <span className="count-label">Оповіщень</span>
            </div>
            <div>
                <span className="count-field">{data.alertEvents}</span>
                <span className="count-label">Згенерованих оповіщень</span>
            </div>
            <div>
                <span className="count-field">{data.alertEventsNew}</span>
                <span className="count-label">Нових оповіщень</span>
            </div>
            <div>
                <span className="count-field">{data.alertEventsResolved}</span>
                <span className="count-label">Закритих оповіщень</span>
            </div>
        </>
    )

    return (
        <Card>
            {isLoading ? <Spinner size={30} height={"50px"}/>
                : data ? renderData()
                    : <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}/>}
        </Card>
    );
}

export default TotalRecords