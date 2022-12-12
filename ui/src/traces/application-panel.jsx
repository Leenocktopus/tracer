import React, {useEffect, useState} from 'react';
import {Checkbox} from 'antd';
import Spinner from "../util/spinner";
import {useErrorUpdate} from "../util/ErrorContext";
import {findAllApplications} from "../util/api-calls";

const ApplicationPanel = ({filterParams, setFilterParams}) => {
    const [applications, setApplications] = useState([])
    const [isLoading, setLoading] = useState(false)
    const setError = useErrorUpdate()

    const fetchApplications = async () => {
        setLoading(true)
        const response = await findAllApplications()
        if (response && response.error) {
            setError()
        } else {
            setApplications(response.content)
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchApplications()
    }, [])


    const onChange = (checkedValues) => {
        if (checkedValues.length === 1) {
            checkedValues.push(" ")
        }
        setFilterParams({applications: checkedValues})
    };


    return (
        <div className="application-panel">
            {isLoading ? <Spinner size={25} height={"50px"}/> :
                <Checkbox.Group
                    options={applications.map(application => application.name)}
                    value={filterParams.applications}
                    onChange={onChange}
                />}
        </div>

    );
}

export default ApplicationPanel
