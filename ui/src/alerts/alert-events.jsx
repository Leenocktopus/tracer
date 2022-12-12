import React, {useEffect, useState} from 'react';
import {changeAlertEvent, findAllAlertEvents} from "../util/api-calls";
import {useErrorUpdate} from "../util/ErrorContext";
import {Badge, Button, Modal, Table, Tag} from "antd";
import moment from "moment";
import {STATUS_NEW, STATUS_RESOLVED} from "../util/constants";


const AlertEvents = ({alertId}) => {
    const [alertEvents, setAlertEvents] = useState([])
    const [isLoading, setLoading] = useState(false)
    const [isResolveLoading, setResolveLoading] = useState(0)
    const setError = useErrorUpdate()


    useEffect(() => {
        fetchAlertEvents()
    }, [])

    const fetchAlertEvents = async () => {
        setLoading(true)

        const response = await findAllAlertEvents(alertId)
        if (response && response.error) {
            setError()
            setLoading(false)
        } else {
            setAlertEvents(response.content)
            setLoading(false)
        }
    }

    const updateAlertEvent = async (alertEventId) => {
        if (!isResolveLoading) {
            setResolveLoading(alertEventId)
            const response = await changeAlertEvent(alertEventId, {status: STATUS_RESOLVED})
            if (response && response.error) {
                setLoading(false)
                setError()
            } else {
                setResolveLoading(false)
                fetchAlertEvents()
            }
        }
    }


    const columns = [
        {
            title: "Позначка часу", dataIndex: "generatedAt", key: "generatedAt", render: (generatedAt) => (
                <span>
                    {moment(generatedAt).format("yyyy-MM-DD HH:mm:ss")}
                </span>
            )
        },
        {
            title: "Випадкове число", dataIndex: "traceId", key: "trace", render: (trace) => (
                <Tag
                    className={"trace-tags"}
                    onClick={() => window.open(`/traces?traceId=${trace}`, "_blank")}
                >
                    {trace}
                </Tag>
            )
        },
        {
            title: "Причина", dataIndex: "reason", key: "reason"
        },
        {
            title: "Статус", dataIndex: "status", key: "status", render: (status) => (
                <span>
                        {status === STATUS_RESOLVED ?
                            <Badge title={""} count={status} style={{backgroundColor: '#52c41a'}}/> :
                            <Badge title={""} count={status}/>
                        }</span>
            )
        },
        {
            title: '',
            key: 'action',
            render: (record) => (
                record.status === STATUS_NEW &&
                <Button
                    type={"primary"}
                    loading={isResolveLoading === record.id}
                    onClick={() => updateAlertEvent(record.id)}
                >
                    Закрити оповіщення
                </Button>
            ),
        },
    ]

    const renderAlertEvents = () => (
        <Table
            loading={isLoading}
            columns={columns}
            dataSource={alertEvents}
        >
        </Table>

    );

    return (
        <div className="alert-events-card">
            <div className="alert-events-label">Згенеровані оповіщення</div>
            {renderAlertEvents()}
        </div>
    );
}

export default AlertEvents
