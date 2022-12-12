import React, {useEffect, useRef, useState} from 'react';
import {Button, Card, Collapse} from "antd";
import './traces.scss'
import TimestampPanel from "./timestamp-panel";
import ApplicationPanel from "./application-panel";
import LabelPanel from "./label-panel";
import TracesTable from "./traces-table";
import Spinner from "../util/spinner";
import moment from "moment";
import useUrlState from "@ahooksjs/use-url-state";
import {useErrorUpdate} from "../util/ErrorContext";
import {findAllTraces} from "../util/api-calls";

const {Panel} = Collapse;
const timestampFormat = "YYYY-MM-DDTHH:mm:ss.SSSZ"

const Traces = () => {
    const [isFirstPageLoading, setFirstPageLoading] = useState(true)
    const [isLoading, setLoading] = useState(false)
    const [traces, setTraces] = useState([])
    const [currentPage, setCurrentPage] = useState(0)
    const [stopLoading, setStopLoading] = useState(false)
    const [currentTimestamp, setCurrentTimestamp] = useState(moment().format(timestampFormat))
    const setError = useErrorUpdate()

    const [filterParams, setFilterParams] = useUrlState(
        {
            startTimestamp: "",
            endTimestamp: "",
            labels: [],
            applications: [],
            traceId: ""
        },
        {
            parseOptions: {
                arrayFormat: 'comma',
            },
            stringifyOptions: {
                arrayFormat: 'comma',
            },
        }
    )
    const DEFAULT_PAGE_SIZE = 20;

    const dataCard = useRef(null);

    useEffect(() => {
        reload()
    }, [filterParams])


    useEffect(() => {
        if (currentPage !== 0 && !stopLoading) {
            fetchPage()
        }
    }, [currentPage])

    const reload = () => {
        setLoading(false)
        setStopLoading(false)
        setCurrentPage(0)
        fetchFirstPage()
    }

    const fetchFirstPage = () => {
        setFirstPageLoading(true)
        fetchTraces({
            startTimestamp: filterParams.startTimestamp && filterParams.startTimestamp,
            endTimestamp: filterParams.endTimestamp ? filterParams.endTimestamp : moment().format(timestampFormat),
            page: 0,
            size: DEFAULT_PAGE_SIZE,
            applications: filterParams.applications.toString(),
            labels: filterParams.labels.toString(),
            traceId: filterParams.traceId
        }).then(
            data => {
                setTraces(data.content)
                setFirstPageLoading(false)
                setCurrentTimestamp(moment().format(timestampFormat))
            }
        ).catch(() => setError())
    }

    const fetchTraces = async (params) => {
        setLoading(true)
        const response = await findAllTraces(params)
        if (response && response.error) {
            setError()
            setLoading(false)
        } else {
            setLoading(false)
            return response
        }
    }

    const fetchPage = () => {
        if (!stopLoading) {
            setLoading(true)
        }
        fetchTraces({
            startTimestamp: filterParams.startTimestamp && filterParams.startTimestamp,
            endTimestamp: filterParams.endTimestamp ? filterParams.endTimestamp : currentTimestamp,
            page: currentPage,
            size: DEFAULT_PAGE_SIZE,
            applications: filterParams.applications.toString(),
            labels: filterParams.labels.toString(),
            traceId: filterParams.traceId
        }).then(
            data => {
                setTraces((prev) => [...prev, ...data.content])
                setFirstPageLoading(false)
                setLoading(false)
                setStopLoading(data.content.length === 0)
            }
        ).catch(() => setError())
    }

    useEffect(() => {
        if (dataCard) {
            dataCard.current.addEventListener("scroll", scrollListener);
        }
    }, []);

    const scrollListener = () => {
        if ((dataCard.current.scrollTop / (dataCard.current.scrollHeight - dataCard.current.clientHeight)) * 100 === 100) {
            setCurrentPage((prev) => prev + 1)
        }
    }

    const resetFilter = () => {
        setFilterParams({
            startTimestamp: "",
            endTimestamp: "",
            activeLabels: [],
            applications: []
        })
    }

    const renderTracesTable = (traces) => (
        <TracesTable traces={traces} setTraces={setTraces}/>
    )

    return (
        <div className="traces-container">
            <Card className="filter-card">
                <div className="filter-title">Фільтр випадкових чисел</div>
                <Collapse defaultActiveKey={['1']}>
                    <Panel header="Позначка часу" key="1">
                        <TimestampPanel
                            filterParams={filterParams}
                            setFilterParams={setFilterParams}
                        />
                    </Panel>
                    <Panel header="Програми-клієнти" key="2">
                        <ApplicationPanel
                            filterParams={filterParams}
                            setFilterParams={setFilterParams}
                        />
                    </Panel>
                    <Panel header="Мітки" key="3">
                        <LabelPanel
                            filterParams={filterParams}
                            setFilterParams={setFilterParams}
                        />
                    </Panel>
                </Collapse>
                <div className="filter-buttons-container">
                    <Button onClick={resetFilter}>Очистити фільтр</Button>
                </div>
            </Card>
            <Card className={"data-card"} ref={dataCard}>
                <div className="traces-table-header">
                    <Button loading={isFirstPageLoading} type={"primary"} onClick={() => reload()}>Оновити дані</Button>
                </div>
                {isFirstPageLoading ? <Spinner size={50} height={"calc(100vh - 46px)"}/> : renderTracesTable(traces)}
                {isLoading && <Spinner size={25} height={"50px"}/>}
            </Card>
        </div>
    );
}

export default Traces
