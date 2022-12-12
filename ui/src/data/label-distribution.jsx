import React, {useEffect, useState} from 'react';
import {TinyColumn} from '@ant-design/charts';
import {useErrorUpdate} from "../util/ErrorContext";
import {getLabelDistribution} from "../util/api-calls";

const LabelDistribution = ({labelId}) => {
    const [data, setData] = useState("")
    const [isLoading, setLoading] = useState(false)
    const setError = useErrorUpdate()

    useEffect(() => {
        fetchLabelDistribution()
    }, [labelId])


    const fetchLabelDistribution = async () => {
        setLoading(true)
        const response = await getLabelDistribution(labelId)
        if (response && response.error) {
            setError()
        } else {
            setData(response)
            setLoading(false)
        }
    }


    const config = {
        loading: isLoading,
        height: 256,
        data: data && data.map(element => element.count),
        columnWidthRatio: 0.8,
        tooltip: {
            customContent: function (x, data) {
                return `${data[0]?.data?.y}`;
            },
        },
    };
    return <TinyColumn {...config} />;

}

export default LabelDistribution
