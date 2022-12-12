import React from 'react';
import {DatePicker} from "antd";
import moment from "moment";

const timestampFormat = "YYYY-MM-DDTHH:mm:ss.SSSZ"

const TimestampPanel = ({filterParams, setFilterParams}) => {

    return (
        <div className="timestamp-panel">
            <div className="label">Початкова позначка часу:</div>
            <DatePicker
                showTime
                placeholder={"Початкова позначка часу"}
                value={filterParams.startTimestamp ? moment(filterParams.startTimestamp) : ""}
                onChange={(value) => setFilterParams({startTimestamp: value?.format(timestampFormat) || ""})}
            />
            <div className="label">Кінцева позначка часу:</div>
            <DatePicker
                showTime
                placeholder={"Кінцева позначка часу"}
                value={filterParams.endTimestamp ? moment(filterParams.endTimestamp) : ""}
                onChange={(value) => setFilterParams({endTimestamp: value?.format(timestampFormat) || ""})}
            />
        </div>

    );
}

export default TimestampPanel
