import React, {useEffect, useState} from 'react';

import {Select} from "antd";
import './data.scss'
import {Card} from "antd/lib";
import useUrlState from "@ahooksjs/use-url-state";
import TracesRate from "./traces-rate";
import TracesDistribution from "./traces-distribution";
import TotalRecords from "./total-records";
import TestUsage from "./test-usage";
import AlertEventsDistribution from "./alert-events-distribution";
import AlertEventsRate from "./alert-events-rate";
import LabelDistribution from "./label-distribution";
import {findAllLabels} from "../util/api-calls";
import {useErrorUpdate} from "../util/ErrorContext";

const {Option} = Select


const Data = () => {
    const [labels, setLabels] = useState([])
    const [selectedLabel, setSelectedLabel] = useState(1)
    const [filterParams, setFilterParams] = useUrlState({
            range: "hour",
            type: "application"
        }
    )
    const setError = useErrorUpdate()

    useEffect(() => {
        fetchLabels()
    }, [])
    const fetchLabels = async () => {
        const response = await findAllLabels()
        if (response && response.error) {
            setError()
        } else {
            setLabels(response.content)
        }
    }



    return (
        <Card>
            <div className="time-filter-container">
                <div className="label">Інтервал часу:</div>
                <Select
                    value={filterParams.range}
                    onChange={(value) => setFilterParams({range: value})}
                >
                    <Option value="hour">Минула година</Option>
                    <Option value="day">Минулий день</Option>
                    <Option value="week">Минулий тиждень</Option>
                    <Option value="month">Минулий місяць</Option>
                </Select>
                <div className="label">Групування:</div>
                <Select
                    value={filterParams.type}
                    onChange={(value) => setFilterParams({type: value})}
                >
                    <Option value="application">Програми</Option>
                    <Option value="label">Мітки</Option>
                </Select>
            </div>
            <div className={"chart-card"}>
                <div className={"statistics-area"}>
                    <div className="section-title">Статистика</div>
                    <TotalRecords/>
                    <TestUsage/>
                </div>
                <div className={"chart-area"}>
                    <div className="section-title">Графіки</div>
                    <Card>
                        <div className="chart-title">Випадкові числа</div>
                        {filterParams.range && <TracesRate range={filterParams.range} type={filterParams.type}/>}
                    </Card>
                    <Card>
                        <div className="chart-title">Розподіл випадкових чисел</div>
                        {filterParams.range && <TracesDistribution range={filterParams.range}/>}
                    </Card>
                    <Card>
                        <div className="chart-title">Оповіщення</div>
                        {filterParams.range && <AlertEventsRate range={filterParams.range} type={filterParams.type}/>}
                    </Card>
                    <Card>
                        <div className="chart-title">Розподіл оповіщень</div>
                        {filterParams.range && <AlertEventsDistribution range={filterParams.range}/>}
                    </Card>
                    <Card>
                        <div className="label-distribution">
                            <div className="chart-title">Розподіл випадкових чисел за міткою</div>
                            {labels.length > 0 &&
                            <Select
                                value={selectedLabel}
                                onChange={(value) => setSelectedLabel(value)}
                            >
                                {labels.map(label => (
                                    <Option value={label.id} key={label.id}>{label.label}</Option>
                                ))}
                            </Select>}
                        </div>
                        <LabelDistribution labelId={selectedLabel}/>
                    </Card>
                </div>
            </div>
        </Card>
    );
}

export default Data
