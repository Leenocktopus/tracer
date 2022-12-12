import React, {Component, Fragment} from 'react';
import {
    validateEntropy,
    validateMaurers,
    validateSequence,
    validateSerial,
    validateWithBlock,
    validateWithMatrix,
    validateWithTemplate
} from "./Validator";
import {data} from "./NistData";
import {Badge, InputNumber, Table, Button} from "antd";

class AllNist extends Component {

    constructor(props) {
        super(props);
        this.state = {
            label: props.label,
            sequence: "",
            error: "",
            acceptance: 0.1,
            loading: false,
            tests: {
                frequency: {
                    checked: false,
                },
                block_frequency: {
                    checked: false,
                    blockSize: "",
                    error: ""
                },
                runs: {
                    checked: false,
                },
                longest_run_of_ones: {
                    checked: false,
                },
                binary_matrix: {
                    checked: false,
                    matrixSize: "",
                    error: ""
                },
                spectral: {
                    checked: false,
                    error: ""
                },
                non_overlapping_template: {
                    checked: false,
                    blockSize: "",
                    template: "",
                    error: ""
                },
                overlapping_template: {
                    checked: false,
                    blockSize: "",
                    template: "",
                    error: ""
                },
                maurers: {
                    checked: false,
                    blockSize: "",
                    blocksInInitSegment: "",
                    error: ""
                },
                linear_complexity: {
                    checked: false,
                    blockSize: "",
                    error: ""
                },
                serial: {
                    checked: false,
                    blockSize: "",
                    error: ""
                },
                entropy: {
                    checked: false,
                    blockSize: "",
                    error: ""
                },
                cumulative_sums: {
                    checked: false,
                },
                random_excursions: {
                    checked: false,
                },
                excursion_variant: {
                    checked: false,
                },
            }
        }

        this.min = React.createRef(null);
        this.max = React.createRef(null);
    }

    onChange = (event) => {
        const {name, value} = event.target;
        this.setState({[name]: value});
    }

    onTestParamsChange = (event) => {
        const {name, value} = event.target;
        this.setState((prevState) => {
            let {tests} = prevState;
            let path = name.split(".");
            tests[path[0]][path[1]] = value;
            return {tests};
        })
    }

    onClick = () => {
        const sequence = "00000000000000000000000000000000000000001111111100000111111111111110111111111111111100000000000000";
        //this.setState({error: validateSequence(sequence)})
        this.setState({loading: true})
        if (true) {
            let tests = JSON.parse(JSON.stringify(this.state.tests));
            let testParams = Object.keys(tests).filter(
                key => tests[key]["checked"] === true
            ).reduce((obj, key) => {
                obj[key] = tests[key];
                return obj;
            }, {})
            Object.keys(testParams).map((key) => {
                testParams[key]["sequence"] = sequence;
            });


            if (!Object.keys(testParams).length === false && testParams.constructor === Object) {
                Object.keys(testParams)
                    .map((key) => {
                        delete testParams[key]["checked"];
                        delete testParams[key]["error"];
                    });
                if (this.validator(testParams)) {

                    testParams["sequence_params"] = {
                        min: this.min.current.value,
                        max: this.max.current.value,
                        label: this.state.label
                    }
                    fetch(`http://localhost:7111/realtime-test`, {
                        method: "POST",
                        mode: "cors",
                        headers: {
                            'Content-Type': "application/json"
                        },
                        body: JSON.stringify(testParams)
                    }).then((response) => response.json())
                        .then((data) => {
                            this.setState({result: data})
                            this.setState({loading: false})
                            }
                        ).catch()
                }
            } else {
                this.setState({error: "Оберіть хоча б один метод"});

            }

        }
    }

    normalize = () => {
        let params = this.state.sequence
        params = params.replace(/\s/g, "")

        return params
    }

    onAllCheckboxChange = (event) => {
        let checked = event.target.checked;
        this.setState((prevState) => {
            let {tests} = prevState;
            Object.keys(tests).map((key) => {
                if (checked === false) { //// ?????????????
                    Object.keys(tests[key]).map(innerKey => {
                        tests[key][innerKey] = ""
                    })
                }
                tests[key]["checked"] = checked;
            })
            return {tests};
        })
    }
    onCheckboxChange = (event) => {
        let {checked, name} = event.target;
        this.setState((prevState) => {
            let {tests} = prevState;
            if (checked === false) { //// ?????????????
                Object.keys(tests[[name]]).map(key => {
                    tests[[name]][key] = ""
                })
            }
            tests[[name]]["checked"] = checked;

            return {tests};
        })
    }

    validator = (data) => {
        let counter = 0;
        Object.keys(data)
            .map((key) => {
                let validate
                if (["block_frequency", "linear_complexity"].includes(key)) {
                    validate = validateWithBlock
                } else if (key === "entropy") {
                    validate = validateEntropy
                } else if (key === "binary_matrix") {
                    validate = validateWithMatrix
                } else if (["overlapping_template", "non_overlapping_template"].includes(key)) {
                    validate = validateWithTemplate
                } else if (key === "serial") {
                    validate = validateSerial
                } else if (key === "maurers") {
                    validate = validateMaurers
                } else {
                    validate = validateSequence
                }
                let error = validate(data[key])
                this.setState((prevState) => {
                    let test = prevState.tests[key]
                    test["error"] = error
                    return {test};
                })
                if (error) {
                    counter++;
                }
            })
        return counter === 0
    }

    getNumbersPassed = () => {
        const numbers = Object.keys(this.state.result).map((key) => {
            if (Array.isArray(this.state.result[key])) {
                const val = this.state.result[key].map(item => {

                    if (item.pValue < this.state.acceptance) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                if (val.reduce((x1, x2) => x1 + x2, 0) === 0) {
                    return 1
                } else {
                    return 0;
                }
            } else {
                if (this.state.result[key].pValue < this.state.acceptance) {
                    return 0;
                } else {
                    return 1;
                }
            }
        })

        return numbers.reduce((x1, x2) => x1 + x2, 0);
    }

    renderResults = () => {
        const columns = [
            {title: "Тест", dataIndex: "test", key: "test"},
            {title: "Результат тесту", dataIndex: "pValue", key: "pValue"},
            {
                title: "Висновок", dataIndex: "sequenceRandom", key: "sequenceRandom",
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


        return (
            <Table
                columns={columns}
                dataSource={ Object.keys(this.state.result).flatMap(key => {
                        if (Array.isArray(this.state.result[key])) {
                            let i = 0
                            return this.state.result[key].map(value => {
                                i = i + 1
                                return ({
                                    test: data[key]["engName"] + " #" + i,
                                    ...value
                                })
                            })
                        } else {
                            return [{
                                test: data[key]["engName"],
                                ...this.state.result[key]
                            }]
                        }
                    }
                )}
                pagination={false}
            />

        );


    }

    render() {
        return (
            <>
                <div>
                    {this.state.error &&
                    <>
                        <div className={"error"}>{this.state.error}</div>
                        <br/>
                    </>
                    }
                    <div>
                        <span>Мінімальне значення:</span>
                        <InputNumber ref={this.min}/>
                    </div>
                    <div>
                        <span>Максимальне значення:</span>
                        <InputNumber ref={this.max}/>
                    </div>
                    <div className="note">
                        <i>Мінімільне та максимальне значення визначають які зі згенерованих чисел буде перетворено на 1
                            в створеній бітовій послідовності. Всі інші числа буде перетворено на 0.</i>
                    </div>
                    <div id={"nist-additional-fields-grid"}>

                        <div>
                            <input type={"checkbox"} name={"all"} onChange={this.onAllCheckboxChange}/>
                            <label htmlFor>{"Обрати всі"}</label></div>

                        {
                            Object.keys(data).map((key) => (
                                    <div key={key}>
                                        <input type={"checkbox"} name={key} checked={this.state.tests[key].checked}
                                               onChange={this.onCheckboxChange}/>
                                        <label>{data[key]["engName"]}</label><br/>
                                        {this.state.tests[key].error &&
                                        <>
                                            <div className={"error"}>{this.state.tests[key].error}</div>
                                            <br/>
                                        </>
                                        }
                                        <div className={"nist-additional-fields"}>
                                            <table>
                                                {this.state.tests[key].checked && Object.keys(data[key]["fields"]).map((innerKey) => (
                                                    <Fragment key={innerKey}>
                                                        <tr>
                                                            <td><label>{data[key]["fields"][innerKey]["ukrName"]}</label>
                                                            </td>
                                                            <td><input type={"text"} name={`${key}.${innerKey}`}
                                                                       onChange={this.onTestParamsChange}
                                                                       value={this.state.tests[key][innerKey]}
                                                                       placeholder={data[key]["fields"][innerKey]["tooltip"]}/>
                                                            </td>
                                                        </tr>

                                                    </Fragment>
                                                ))}
                                            </table>
                                        </div>
                                    </div>
                                )
                            )
                        }
                    </div>
                    <br/>
                    <Button className={"test-button"} onClick={this.onClick} loading={this.state.loading}>Тест</Button>
                </div>
                {this.state.result &&
                <>
                    <div className="section-header results-header">Результати тестування</div>
                    {this.renderResults()}
                </>
                }
            </>
        );
    }

}

export default AllNist;
