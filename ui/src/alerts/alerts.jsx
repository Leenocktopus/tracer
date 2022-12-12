import React, {useEffect, useState} from 'react';
import {Card} from "antd";
import './alerts.scss'
import NewAlertModal from "./new-alert-modal";
import {findAllAlerts} from "../util/api-calls";
import {useErrorUpdate} from "../util/ErrorContext";
import Spinner from "../util/spinner";
import {Menu} from "antd/lib";
import useUrlState from "@ahooksjs/use-url-state";
import AlertCard from "./alert-card";


const Alerts = () => {
    const [alerts, setAlerts] = useState([])
    const [isLoading, setLoading] = useState(false)
    const [urlParams, setUrlParams] = useUrlState({alertId: ""})
    const setError = useErrorUpdate()

    const fetchAlerts = async () => {
        setLoading(true)

        const response = await findAllAlerts()
        if (response && response.error) {
            setError()
        } else {
            setAlerts(response.content)
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchAlerts()
    }, [])

    useEffect(() => {
        if (alerts.length > 0 && !urlParams.alertId) {
            setUrlParams({alertId: alerts[0].id})
        }
    }, [alerts])

    const onClick = (e) => {
        setUrlParams({alertId: e.key});
    }

    const reloadAlerts = async (id) => {
        setUrlParams({alertId: id})
        await fetchAlerts()
    }

    const items = alerts.map(alert => ({key: alert.id, label: alert.name}))

    return (
        <div className="alert-container">
            <Card className="alerts-card">
                <div className="title-container">
                    <span className="title">Оповіщення</span>
                    <NewAlertModal reloadAlerts={reloadAlerts}/>
                </div>
                {isLoading ? <Spinner size={30} height={"100px"}/> :
                    <Menu onClick={onClick} selectedKeys={[urlParams.alertId]} mode="vertical" items={items}/>}
            </Card>
            <Card className={"data-card"}>
                {isLoading ? <Spinner size={40} height={"100px"}/> : urlParams.alertId &&
                    <AlertCard fetchAlerts={fetchAlerts} setUrlParams={setUrlParams} alertId={urlParams.alertId}/>}
            </Card>
        </div>
    );
}

export default Alerts
