import React, { Component } from 'react';
import './HomePage.css';
import { withRouter } from 'react-router-dom';


class HomePage extends Component {
    render() {
        return (
            <div className="page-not-found">
                <h1 className="title">
                    Wallet Home
                </h1>
                <div className="desc">
                    Welcome to the Wallet App!
                </div>
            </div>
        );
    }
}

export default withRouter(HomePage)