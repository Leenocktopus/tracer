import React, {useEffect, useState} from 'react';
import './setup.scss'
import Spinner from "../util/spinner";
import {Button} from "antd";
import TestMenu from "./test-menu";
import {useErrorUpdate} from "../util/ErrorContext";
import {deleteLabelById, findLabelById} from "../util/api-calls";
import AllNist from "./AllNist";


const LabelCard = ({fetchLabels, setUrlParams, labelId}) => {
    const [label, setLabel] = useState("")
    const [isLoading, setLoading] = useState(false)
    const [deleteLoading, setDeleteLoading] = useState(false)
    const setError = useErrorUpdate()


    const fetchLabel = async () => {
        setLoading(true)

        const response = await findLabelById(labelId)
        if (response && response.error) {
            setError()
        } else {
            setLabel(response)
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchLabel()
    }, [labelId])

    const deleteLabel = async () => {
        setDeleteLoading(true)

        const response = await deleteLabelById(labelId)
        if (response && response.error) {
            setError(response)
            setDeleteLoading(false)
        } else {
            fetchLabels()
            setUrlParams({labelId: ""})
            setDeleteLoading(false)
        }
    }


    const renderLabelCard = () => (
        <div className="label-card">
            <div className="label-header-container">
                <div className="label-name">{label.label}</div>
                <div className="delete-container">
                    <Button
                        type={"primary"}
                        loading={deleteLoading}
                        onClick={deleteLabel}
                    >
                        Видалити
                    </Button>
                </div>
            </div>
            {!isLoading && label && <TestMenu label={label} setLabel={setLabel}/>}
        </div>
    )
    return (
        <div>
            {isLoading ? <Spinner size={40} height={"100px"}/> : renderLabelCard()}
            <div>
                <div className="section-header">Тестування в реальному часі</div>
                <AllNist label={labelId}/>
            </div>
        </div>
    );
}

export default LabelCard
