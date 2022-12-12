import React, {useEffect, useState} from 'react';
import './data.scss'
import {Card, Empty} from "antd/lib";
import Spinner from "../util/spinner";
import {useErrorUpdate} from "../util/ErrorContext";
import {getTestStatistics} from "../util/api-calls";


const TestUsage = () => {
    const [data, setData] = useState("")
    const [isLoading, setLoading] = useState(false)
    const setError = useErrorUpdate()

    const fetchTestUsage = async () => {
        setLoading(true)
        const response = await getTestStatistics()
        if (response && response.error) {
            setError()
        } else {
            setData(response)
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchTestUsage()
    }, [])

    const renderData = () => (
        <table className="test-table">
            <tbody>
            {

                data.map((testCount) => (
                    <tr key={testCount.testType}>
                        <td className="test-count">{testCount.count}</td>
                        <td className="test-title">{testCount.testType}</td>
                        <td className={`test-type ${testCount.type === "MD" ? "md-test" : "nist-test"}`}>{testCount.type}
                        </td>
                    </tr>
                ))

            }
            </tbody>
        </table>
    )
    return (
        <Card>
            <div className="chart-title">Найбільш популярні тести</div>
            {isLoading ? <Spinner size={30} height={"50px"}/>
                : data ? renderData()
                    : <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}/>}
        </Card>
    );
}

export default TestUsage
