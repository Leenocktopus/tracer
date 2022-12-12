import React, {useEffect, useState} from 'react';
import {Button, Modal, Switch} from "antd";
import Spinner from "../util/spinner";
import {changeAlert, deleteAlertById, findAlertById, findAllLabels} from "../util/api-calls";
import {useErrorUpdate} from "../util/ErrorContext";
import Input from "antd/es/input/Input";
import {CloseCircleFilled, PlusSquareOutlined} from "@ant-design/icons";
import cloneDeep from 'lodash/cloneDeep';
import AlertEvents from "./alert-events";

const AlertCard = ({alertId, fetchAlerts, setUrlParams}) => {
    const [alert, setAlert] = useState("")
    const [originalAlert, setOriginalAlert] = useState("")
    const [isLoading, setLoading] = useState(false)
    const [labelsLoading, setLabelsLoading] = useState(false)
    const [labels, setLabels] = useState("")
    const [deleteLoading, setDeleteLoading] = useState(false)
    const [updateLoading, setUpdateLoading] = useState(false)
    const [contactsErrors, setContactsErrors] = useState([false])
    const setError = useErrorUpdate()

    const isValidEmail = (value) => value.match(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)

    useEffect(() => {
        if (alert){
            const status = Array(alert.contacts.length).fill(false)
            alert.contacts
                .forEach((contact, index) => {
                    if (contact && status[index] === false) {
                        status[index] = !isValidEmail(contact) || !(contact.length < 100);
                        const indexOf = alert.contacts.indexOf(contact);
                        if (indexOf !== index) {
                            status[index] = true
                            status[indexOf] = true
                        }
                    }
                })
            setContactsErrors(status)
        }
    }, [alert])


    const fetchAlert = async () => {
        setLoading(true)

        const response = await findAlertById(alertId)
        if (response && response.error) {
            setError()
        } else {
            const alert = {
                ...response,
                contacts: response.contacts.map(contact => contact.email)
            }
            setAlert(alert)
            setOriginalAlert(alert)
            setLoading(false)
        }
    }

    const deleteAlert = async () => {
        setDeleteLoading(true)

        const response = await deleteAlertById(alertId)
        if (response && response.error) {
            setError()
            setDeleteLoading(false)
        } else {
            fetchAlerts()
            setUrlParams({alertId: ""})
            setDeleteLoading(false)
        }
    }

    const updateAlert = async () => {
        setUpdateLoading(true)

        const response = await changeAlert( alertId,
            {
                contacts: alert.contacts.map(contact => ({email: contact})),
                labels: alert.labels.map(label => label.id)
            }
        )
        if (response && response.error) {
            setError()
            setUpdateLoading(false)
        } else {
            fetchAlert()
            setUpdateLoading(false)
            Modal.info(
                {
                    title: "Налаштування успішно оновлено.",
                    zIndex: 2000
                }
            )
        }
    }

    const fetchLabels = async () => {
        setLabelsLoading(true)

        const response = await findAllLabels()
        if (response && response.error) {
            setError()
        } else {
            setLabels(response.content)
            setLabelsLoading(false)
        }
    }

    useEffect(() => {
        fetchLabels()
    }, [])

    useEffect(() => {
        fetchAlert()
    }, [alertId])

    const setValue = (value) => {
        setAlert({...alert, ...value})
    }

    const changeContact = (e, index) => {
        const contactsCopy = cloneDeep(alert.contacts)
        contactsCopy[index] = e.target.value
        setValue({contacts: contactsCopy})
    }

    const removeContact = (index) => {
        const contactsCopy = cloneDeep(alert.contacts)
        contactsCopy.splice(index, 1)
        setValue({contacts: contactsCopy})
    }

    const changeLabels = (checked, label) => {
        const labelsCopy = cloneDeep(alert.labels)
        if (checked){
            setValue({labels: [...labelsCopy, label]})
        } else {
            setValue({labels: labelsCopy.filter(l => l.id !== label.id)})
        }
    }

    const reset = () => {
        setAlert(originalAlert)
    }

    const renderAlertCard = () => (
        <div>
        <div className="alert-card">
            <div className="alert-header-container">
                <div className="alert-name">{alert.name}</div>
                <div className="delete-container">
                    <Button
                        type={"primary"}
                        loading={deleteLoading}
                        onClick={deleteAlert}
                    >
                        Видалити
                    </Button>
                </div>
            </div>
            <div className="alert-content-container">
                <div className="section-header">Контакти</div>
                {alert.contacts.map((contact, index) => (
                    <>
                        <Input
                            key={index}
                            placeholder="Enter an email"
                            status={contactsErrors[index] && "error"}
                            value={contact}
                            onChange={(e) => changeContact(e, index)}
                        />
                        <CloseCircleFilled onClick={() => removeContact(index)}/>
                        <br/>
                    </>
                ))}
                <div className="add-button-container">
                    <Button
                        disabled={alert.contacts.length > 9}
                        type={"primary"}
                        icon={<PlusSquareOutlined/>}
                        onClick={() => setValue({contacts: [...alert.contacts, undefined]})}
                    >
                        Додати
                    </Button>
                </div>
                <div className="section-header">Мітки</div>
                <div className="label-menu-container">
                    {labelsLoading ? <Spinner size={40} height={"50px"}/>
                        : labels && labels.map((label) => (
                        <div key={label.id}>
                            <Switch
                                checked={alert.labels.find((activeTest) => activeTest.id === label.id) && true}
                                onChange={(checked) => changeLabels(checked, label)}
                            />
                            <span className="label-title">{label.label}</span>
                        </div>
                    ))}
                </div>
            </div>
            <div className="menu-buttons">
                <Button
                    onClick={reset}>
                    Очистити зміни
                </Button>
                <Button
                    type={"primary"}
                    loading={updateLoading}
                    onClick={updateAlert}
                >
                    Зберегти
                </Button>
            </div>
        </div>
            <AlertEvents alertId={alertId}/>
        </div>
    );

    return (
        <div>
            {isLoading ? <Spinner size={40} height={"100px"}/> : alert && renderAlertCard()}
        </div>
    );
}

export default AlertCard
