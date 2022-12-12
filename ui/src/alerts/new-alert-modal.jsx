import React, {useEffect, useState} from 'react';
import {Button, Modal} from "antd";
import {PlusSquareOutlined} from "@ant-design/icons";
import Input from "antd/es/input/Input";
import {useErrorUpdate} from "../util/ErrorContext";
import {saveAlert} from "../util/api-calls";

const defaultAlert = {
    name: undefined,
    contacts: [undefined]
}

function NewAlertModal({reloadAlerts}) {
    const [isLoading, setLoading] = useState(false)
    // const [labels, setLabels] = useState(false)
    const [modalState, setModalState] = useState(false)
    const [nameError, setNameError] = useState(false)
    const [contactsErrors, setContactsErrors] = useState([false])
    const [alert, setAlert] = useState(defaultAlert)
    const setError = useErrorUpdate()

    const isAlphanumeric = (value) => value.match(/^[0-9A-z]*$/)
    const isValidEmail = (value) => value.match(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)

    useEffect(() => {
        if (alert.name) {
            if (isAlphanumeric(alert.name) && alert.name.length < 50) {
                setNameError(false)
            } else {
                setNameError(true)
            }
        } else {
            setNameError(false)
        }
    }, [alert])

    useEffect(() => {
        const status = Array(alert.contacts.length).fill(false)
        alert.contacts.forEach((contact, index) => {
            if (contact && status[index] === false) {
                status[index] = !isValidEmail(contact) || !(contact.length < 100);
                const indexOf = alert.contacts.indexOf(contact);
                if (indexOf !== index){
                    status[index] = true
                    status[indexOf] = true
                }
            }
        })
        setContactsErrors(status)
    }, [alert])

    const isSaveButtonEnabled = () => {
        return alert.name && !nameError
            && alert.contacts.filter(contact => contact !== undefined && contact !== '').length > 0
            && contactsErrors.every(value => value === false)
    }

    const createAlert = async () => {
        setLoading(true)

        const response = await saveAlert({
            name: alert.name,
            contacts: alert.contacts.filter(contact => contact !== undefined && contact !== '')
                .map(contact => ({email: contact}))
        })
        if (response && response.error) {
            setError(response)
            setLoading(false)
        } else {
            setLoading(false)
            reloadAlerts(response.id)
            modalClose()
        }
    }


    const setValue = (value) => {
        setAlert({...alert, ...value})
    }

    const changeContact = (e, index) => {
        const contactsCopy = [...alert.contacts]
        contactsCopy[index] = e.target.value
        setValue({contacts: contactsCopy})

    }


    const modalClose = () => {
        setAlert(defaultAlert)
        setModalState(false)
    }

    const footer = () => (
        <div className="new-label-modal-footer">
            <Button onClick={modalClose}>
                Відмінити
            </Button>
            <Button
                type="primary"
                loading={isLoading}
                disabled={!isSaveButtonEnabled()}
                onClick={createAlert}
            >
                Зберегти
            </Button>
        </div>
    )

    const renderModal = () => (
        <Modal
            title={`Додати нове оповіщення:`}
            visible={modalState}
            className="new-alert-modal"
            onCancel={modalClose}
            footer={footer()}
            closable
            maskClosable={false}
            destroyOnClose
            width={'420px'}
            zIndex={1100}
        >
            <div className={"modal-content"}>
                <div className="new-alert-input-field">
                    <div className={"new-alert-label"}>Назва:</div>
                    <Input
                        status={nameError && "error"}
                        placeholder="Введіть назву оповіщення"
                        value={alert.name}
                        onChange={(e) => setValue({name: e.target.value})}
                    />
                </div>
                <div className="new-alert-input-field">
                    <div className={"new-alert-label"}>Контакти:</div>
                    {alert.contacts.map((contact, index) => (
                        <Input key={index}
                               placeholder="Введіть адресу електронної пошти"
                               status={contactsErrors[index] && "error"}
                               value={contact}
                               onChange={(e) => changeContact(e, index)}
                        />
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
                </div>
            </div>
        </Modal>
    )

    return (
        <>
            <Button
                type={"primary"}
                icon={<PlusSquareOutlined/>}
                onClick={() => setModalState(true)}
            >
                Додати
            </Button>
            {renderModal()}
        </>
    );
}

export default NewAlertModal