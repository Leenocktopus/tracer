import React, {useEffect, useState} from 'react';
import {Checkbox} from 'antd';
import Spinner from "../util/spinner";
import {useErrorUpdate} from "../util/ErrorContext";
import {findAllLabels} from "../util/api-calls";

const LabelPanel = ({filterParams, setFilterParams}) => {
    const [labels, setLabels] = useState([])
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

    const onChange = (checkedValues) => {
        if (checkedValues.length === 1) {
            checkedValues.push(" ")
        }
        setFilterParams({labels: checkedValues})
    };

    return (
        <div className="application-panel">
            {isLoading ? <Spinner size={25} height={"50px"}/> :
                <Checkbox.Group
                    options={labels.map(label => label.label)}
                    value={filterParams.labels}
                    onChange={onChange}
                />
            }
        </div>

    );
}

export default LabelPanel
