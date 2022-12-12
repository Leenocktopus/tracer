import React, {useState} from 'react';
import {Badge, Table, Tag} from 'antd';
import moment from 'moment';
import {findAllTestRuns} from "../util/api-calls";
import {useErrorUpdate} from "../util/ErrorContext";

const {Column} = Table;


const TracesTable = ({traces}) => {

    const keyedTraces = traces.map(trace => ({
        ...trace,
        "key": trace.id
    }))

    const [isLoading, setIsLoading] = useState({});
    const [testRuns, setTestRuns] = useState({})
    const setError = useErrorUpdate()

    const expandedRowRender = record => {
        const columns = [
            {title: "Тест", dataIndex: "test", key: "test"},
            {title: "Результат тесту", dataIndex: "testResult", key: "testResult"},
            {
                title: "Висновок", dataIndex: "random", key: "random",
                render: (random) => (
                    <span>
                        {random === true ?
                            <Badge title={""} count={"Passed"} style={{backgroundColor: '#52c41a'}}/> :
                            random === false ?
                                <Badge title={""} count={"Failed"}/> :
                                <span>-</span>
                        }</span>
                )
            }
        ];

        const data = testRuns[record.id];

        return (
            <Table
                loading={isLoading[record.id] && !data}
                columns={columns}
                dataSource={testRuns[record.id]}
                pagination={false}
            />

        );
    };

    const fetchTestRun = async (record) => {
        const response = await findAllTestRuns({traceId: record.id})
        if (response && response.error) {
            setError()
        } else {
            setTestRuns(state => ({
                ...state,
                [record.key]: response.content.map(testRun => ({
                    ...testRun,
                    "key": testRun.id
                }))
            }));
            setIsLoading({
                [record.id]: false
            });

        }
    }

    const handleExpand = (expanded, record) => {
        if (expanded) {
            setIsLoading(state => ({
                ...state,
                [record.id]: true
            }));
            fetchTestRun(record)

        } else {
            setTestRuns(state => ({
                ...state,
                [record.key]: undefined
            }))
        }
    };

    return (
        <div className="traces-table-container">
            <Table
                expandedRowRender={expandedRowRender}
                onExpand={handleExpand}
                dataSource={keyedTraces}
                pagination={false}
            >
                <Column title="Значення" dataIndex="value" key="value"/>
                <Column
                    title="Програма-клієнт"
                    dataIndex="application"
                    key="application"
                    render={(application) => (
                        <span className="application-name">
                            {application}
                        </span>
                    )}
                />
                <Column
                    title="Позначка часу"
                    dataIndex="generatedAt"
                    key="generatedAt"
                    render={(generatedAt) => (
                        <span>
                            {moment(generatedAt).format("yyyy-MM-DD HH:mm:ss")}
                        </span>
                    )}
                />
                <Column
                    title="Мітки"
                    dataIndex="labels"
                    key="labels"
                    render={(labels) => (
                        <>
                            {labels.map((label) => (
                                <Tag color="#eac545" key={label.id}>
                                    {label.label}
                                </Tag>
                            ))}
                        </>
                    )}
                />
            </Table>
        </div>

    );
}

export default TracesTable
