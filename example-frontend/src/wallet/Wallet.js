import React, { Component } from 'react';
import './Wallet.css';
import { Link } from 'react-router-dom';

class Wallet extends Component {
    render() {
        return (
            <div className="wallet-content">
                <Link className="creator-link" to={`/wallets/${this.props.wallet.id}`}>
                    <div className="wallet-header">
                        <div className="wallet-creator-info">
                            <span className="wallet-creator-name">
                                Wallet Name: {this.props.wallet.name}
                            </span>
                            <span className="wallet-creator-name">
                                Serial Number: {this.props.wallet.serialNumber}
                            </span>
                        </div>
                    </div>
                    <div className="wallet-footer">
                        <span className="total-votes">Balance: {this.props.wallet.balance}</span>
                    </div>
                </Link>
            </div>
        );
    }
}

export default Wallet;