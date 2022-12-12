import React, {useContext, useState} from "react";
import {Button, Modal} from "antd";
import {CloseCircleOutlined} from "@ant-design/icons"

const ErrorUpdateContext = React.createContext()

export const useErrorUpdate = () => {
    return useContext(ErrorUpdateContext)
}

const defaultTitle = "Щось пішло не так"
const defaultErrorText = "Будь ласка, спробуйте пізніше"
export const ErrorProvider = ({children}) => {
    const [error, setError] = useState(false)
    const [title, setTitle] = useState(false)
    const [errorText, setErrorText] = useState(false)

    const toggle = (responseError) => {
        if (!error) {
            setError(true)
            setTitle(responseError && responseError.title ? responseError.title : defaultTitle)
            setErrorText(responseError && responseError.errorText ? responseError.errorText : defaultErrorText)
        }
    }
    return (
        <ErrorUpdateContext.Provider value={toggle}>
            <>
                {children}
                <Modal
                    width={"416px"}
                    zIndex={2000}
                    visible={error}
                    closable={false}
                    className={"ant-modal-confirm ant-modal-confirm-error error-modal"}
                    footer={null}
                    onOk={() => setError(false)}
                >
                    <div className="ant-modal-confirm-body-wrapper">
                        <CloseCircleOutlined color={"red"}/>
                        <div className="ant-modal-confirm-body">
                            <span className="ant-modal-confirm-title">{title}</span>
                            <div className="ant-modal-confirm-content">
                                <div>{errorText}</div>
                            </div>
                        </div>
                        <div className="ant-modal-confirm-btns">
                            <Button
                                type={"primary"}
                                onClick={() => setError(false)}
                            >
                                <span>OK</span>
                            </Button>
                        </div>
                    </div>
                </Modal>
            </>
        </ErrorUpdateContext.Provider>
    )
}