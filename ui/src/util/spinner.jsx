import {LoadingOutlined} from '@ant-design/icons';
import {Spin} from 'antd';
import React from 'react';

const antIcon = (size, height) => (
    <div
        className="spinner-container"
        style={{
            height,
        }}
    >
        <LoadingOutlined
            style={{
                fontSize: size,
            }}
            spin
        />
    </div>
);

const Spinner = ({size, height}) => <Spin indicator={antIcon(size, height)}/>;

export default Spinner