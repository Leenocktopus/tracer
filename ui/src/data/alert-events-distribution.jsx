import React, {useEffect, useState} from 'react';
import axios from "axios";
import {Empty} from "antd";
import {Pie} from '@ant-design/plots';
import {useErrorUpdate} from "../util/ErrorContext";
import {getAlertEventsDistribution, getTracesDistribution} from "../util/api-calls";

const AlertEventsDistribution = ({range}) => {
    const [data, setData] = useState("")
    const [isLoading, setLoading] = useState(false)
    const setError = useErrorUpdate()

    const fetchTracesDistribution = async () => {
        setLoading(true)
        const response = await getAlertEventsDistribution({
            interval: range.toUpperCase()
        })
        if (response && response.error) {
            setError()
        } else {
            setData(response)
            setLoading(false)
        }
    }


    useEffect(() => {
        fetchTracesDistribution()
    }, [range])


    const config = {
        appendPadding: 10,
        data,
        loading: isLoading,
        angleField: 'percentage',
        colorField: 'application',
        radius: 0.75,
        label: {
            type: 'spider',
            labelHeight: 28,
            content: '{name}\n{percentage}',
        },
        interactions: [
            {
                type: 'element-selected',
            },
            {
                type: 'element-active',
            },
        ],
    };

    return data.length !== 0 || isLoading ? <Pie {...config} /> : <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}/>
}

export default AlertEventsDistribution
