import React, {useEffect, useState} from 'react';
import {Line} from '@ant-design/charts';
import {Empty} from "antd";
import moment from "moment";
import {useErrorUpdate} from "../util/ErrorContext";
import {getTracesRate} from "../util/api-calls";

const TracesRate = ({range, type}) => {
    const [data, setData] = useState("")
    const [isLoading, setLoading] = useState(false)
    const setError = useErrorUpdate()

    const fetchTracesRate = async () => {
        setLoading(true)
        const response = await getTracesRate({
            interval: range.toUpperCase(),
            type: type.toUpperCase()
        })
        if (response && response.error) {
            setError()
        } else {
            setData(mapTracesRate(response))
            setLoading(false)
        }
    }

    const mapTracesRate = (data) => {
        return data.map(point => ({
            label: point.label,
            timestamp: moment(point.timestamp).format("yyyy-MM-DD HH:mm:ss"),
            count: point.count
        }));
    }


    useEffect(() => {
        fetchTracesRate()
    }, [range, type])


    const config = {
        data,
        loading: isLoading,
        xField: 'timestamp',
        yField: 'count',
        seriesField: "label"
    };

    const dataIsPresent = () => {
        return data.map((record) => record.count).reduce((partialSum, a) => partialSum + a, 0) > 0
    }

    return (data.length !== 0 && dataIsPresent()) || isLoading ?
        <Line {...config} />
        : <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}/>

}

export default TracesRate
