import React, {useEffect, useState} from 'react';
import {Card, Menu} from "antd/lib";
import './setup.scss'
import Spinner from "../util/spinner";
import useUrlState from "@ahooksjs/use-url-state";
import LabelCard from "./label-card";
import NewLabelModal from "./new-label-modal";
import {findAllLabels} from "../util/api-calls";
import {useErrorUpdate} from "../util/ErrorContext";


const Setup = () => {
    const [labels, setLabels] = useState([])
    const [urlParams, setUrlParams] = useUrlState({labelId: ""})
    const [isLoading, setLoading] = useState(false)
    const setError = useErrorUpdate()

    const fetchLabels = async () => {
        setLoading(true)

        const response = await findAllLabels()
        if (response && response.error) {
            setError()
        } else {
            setLabels(response.content)
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchLabels()
    }, [])


    useEffect(() => {
        if (labels.length > 0 && !urlParams.labelId) {
            setUrlParams({labelId: labels[0].id})
        }
    }, [labels])


    const reloadLabels = async (id) => {
        setUrlParams({labelId: id})
        await fetchLabels()
    }


    const items = labels.map(label => ({key: label.id, label: label.label}))

    const onClick = (e) => {
        setUrlParams({labelId: e.key});
    }


    return (
        <div className="setup-container">
            <Card className="labels-card">
                <div className="title-container">
                    <span className="title">Мітки</span>
                    <NewLabelModal reloadLabels={reloadLabels}/>
                </div>
                {isLoading ? <Spinner size={30} height={"100px"}/> :
                    <Menu onClick={onClick} selectedKeys={[urlParams.labelId]} mode="vertical" items={items}/>}
            </Card>
            <Card className={"data-card"}>
                {isLoading ? <Spinner size={40} height={"100px"}/> : urlParams.labelId &&
                    <LabelCard fetchLabels={fetchLabels} setUrlParams={setUrlParams} labelId={urlParams.labelId}/>}
            </Card>
        </div>
    );
}

export default Setup
