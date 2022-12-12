import React, {useEffect, useState} from 'react';
import {
    AlertOutlined,
    BarChartOutlined,
    DeploymentUnitOutlined,
    SettingOutlined,
    UnorderedListOutlined
} from '@ant-design/icons';
import {Menu} from 'antd';
import {useHistory, useLocation} from "react-router-dom";
import Alerts from "./alerts/alerts";
import Setup from "./setup/setup";
import Traces from "./traces/traces";
import Data from "./data/data";

export const items = [
    {
        label: 'Випадкові числа',
        key: 'traces',
        icon: <UnorderedListOutlined/>,
        component: Traces
    },
    {
        label: 'Дані',
        key: 'data',
        icon: <BarChartOutlined/>,
        component: Data
    },
    {
        label: 'Оповіщення',
        key: 'alerts',
        icon: <AlertOutlined/>,
        component: Alerts
    },
    {
        label: 'Налаштування',
        key: 'setup',
        icon: <SettingOutlined/>,
        component: Setup
    }
];

const NavigationPanel = () => {
    let history = useHistory();
    let location = useLocation();
    const [current, setCurrent] = useState(location.pathname.split("/")[1]);


    useEffect(() => {
        setCurrent(location.pathname.split("/")[1])
    }, [location])

    const onClick = (e) => {
        history.push(`/${e.key}`)
    };

    return (
        <div className="navigation-bar">
            <div className="logo-container">
                <DeploymentUnitOutlined/>
                <span className="logo-text">RNG Trace</span>
            </div>
            <Menu
                onClick={onClick}
                selectedKeys={[current]}
                mode="horizontal"
                theme="dark"
                items={items}
            />
        </div>
    );
}

export default NavigationPanel