import React, {useContext} from 'react';
import "antd/dist/antd.css";
import {BrowserRouter as Router, Route} from "react-router-dom";
import NavigationPanel, {items} from "./navigation-panel";
import './style.scss'
import {ErrorProvider} from "./util/ErrorContext";

const App = () => {
    return (
        <ErrorProvider>
            <Router>
                <NavigationPanel/>
                {items.map(item => (
                    <Route key={item.key} exact path={`/${item.key}`} component={item.component}/>
                ))}
                {/*<Redirect exact from="/" to="traces"/>*/} {/* TODO interferes with URL state*/}
            </Router>
        </ErrorProvider>
    );
}

export default App
