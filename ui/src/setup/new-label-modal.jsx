import React, {useEffect, useState} from 'react';
import './setup.scss'
import {Button, Modal} from "antd";
import {PlusSquareOutlined} from "@ant-design/icons";
import Input from "antd/es/input/Input";
import {useErrorUpdate} from "../util/ErrorContext";
import {saveLabel} from "../util/api-calls";


const NewLabelModal = ({reloadLabels}) => {
    const [modalState, setModalState] = useState(false)
    const [label, setLabel] = useState("")
    const [inputError, setInputError] = useState(false)
    const [loading, setLoading] = useState(false)
    const setError = useErrorUpdate()

    const isAlphanumeric = (value) => (
        value.match(/^[0-9A-z]*$/)
    )

    useEffect(() => {
        if (isAlphanumeric(label) && label.length < 50) {
            setInputError(false)
        } else {
            setInputError(true)
        }
    }, [label])

    const modalClose = () => {
        setLabel("")
        setInputError(false)
        setModalState(false)
    }

    const createLabel = async () => {
        setLoading(true)

        const response = await saveLabel({label: label})
        if (response && response.error) {
            setError(response)
            setLoading(false)
        } else {
            setLoading(false)
            reloadLabels(response.id)
            modalClose()
        }
    }

    const footer = () => (
        <div className="new-label-modal-footer">
            <Button onClick={() => modalClose()}>
                Відмінити
            </Button>
            <Button
                type="primary"
                onClick={() => createLabel()}
                disabled={!label || inputError}
                loading={loading}
            >
                Зберегти
            </Button>
        </div>
    )

    const renderModal = () => (
        <Modal
            title={`Додати нову мітку:`}
            visible={modalState}
            className="new-label-modal"
            onCancel={modalClose}
            footer={footer()}
            closable
            maskClosable={false}
            destroyOnClose
            width={'420px'}
            zIndex={1100}
        >
            <div className={"modal-content"}>
                <Input
                    status={inputError && "error"}
                    placeholder="Введіть нову мітку"
                    value={label}
                    onChange={(e) => setLabel(e.target.value)}
                />
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

export default NewLabelModal
