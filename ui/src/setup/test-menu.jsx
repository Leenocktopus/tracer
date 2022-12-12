import React, {useEffect, useState} from 'react';
import './setup.scss'
import Spinner from "../util/spinner";
import {Button, Modal, Switch, Tag} from "antd";
import {useErrorUpdate} from "../util/ErrorContext";
import {changeLabel, findAllTests} from "../util/api-calls";


const TestMenu = ({label, setLabel}) => {
    const [isLoading, setLoading] = useState(false)
    const [updateLoading, setUpdateLoading] = useState(false)
    const [tests, setTests] = useState([])
    const [activeTests, setActiveTests] = useState(label.tests.map(test => test.id))
    const setError = useErrorUpdate()

    const fetchTests = async () => {
        setLoading(true)

        const response = await findAllTests({parametrized: false})
        if (response && response.error) {
            setError()
        } else {
            setTests(response.content)
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchTests()
    }, [])

    useEffect(() => {
        label.tests.map(test => test.id) 
    }, [label])


    const updateActiveTests = (checked, id) => {
        if (checked) {
            setActiveTests([...activeTests, id])
        } else {
            setActiveTests(activeTests.filter(test => test !== id))
        }
    }

    const reset = () => {
        setActiveTests(label.tests.map(test => test.id))
    }

    const updateLabel = async () => {
        setUpdateLoading(true)

        const response = await changeLabel(label.id, {tests: activeTests})
        if (response && response.error) {
            setError()
            setUpdateLoading(false)
        } else {
            setLabel(prev => ({...response, alerts: prev.alerts}))
            setUpdateLoading(false)
            Modal.info(
                {
                    title: "Налаштування успішно оновлено.",
                    zIndex: 2000
                }
            )
        }
    }


    const renderLabelCard = () => (
        <div className="label-menu">
            <div className="section-header">Тести</div>
            <div className="test-menu-container">
                {tests && activeTests && tests.map((test) => (
                    <div key={test.id}>
                        <Switch
                            checked={activeTests.find((activeTest) => activeTest === test.id) && true}
                            onChange={(checked) => updateActiveTests(checked, test.id)}

                        />
                        <span className="test-title">{test.title}</span>
                    </div>
                ))}
            </div>
            <div className="section-header">Оповіщення</div>
            <div className={"alert-menu-container"}>
                {label.alerts.length > 0 ?
                    <span>
                        Ця мітка асоційована з наступними оповіщеннями:&nbsp;
                        <span>
                            {label.alerts.map(alert => (
                                <Tag
                                    className={"alert-tags"}
                                    key={alert.id}
                                    onClick={() => window.open(`/alerts?alertId=${alert.id}`, "_blank")}
                                >
                                    {alert.name}
                                </Tag>))}
                        </span>
                        Ви можете натиснути на оповіщення, щоб переглянути їх або змінити.
                    </span>
                    :
                    <span>Мітка не асоційована з жодним оповіщенням.</span>
                }
            </div>
            <div className="menu-buttons">
                <Button onClick={reset}>
                    Очистити зміни
                </Button>
                <Button
                    type={"primary"}
                    loading={updateLoading}
                    onClick={updateLabel}
                >
                    Зберегти
                </Button>
            </div>
        </div>
    )
    return (
        <div>
            {isLoading ? <Spinner size={40} height={"100px"}/> : renderLabelCard()}
        </div>
    );
}

export default TestMenu
