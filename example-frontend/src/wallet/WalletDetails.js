import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {getWallet, sendMoney, sendTransaction} from '../util/APIUtils';
import LoadingIndicator  from '../common/LoadingIndicator';
import './WalletDetails.css';
import NotFound from '../common/NotFound';
import ServerError from '../common/ServerError';
import {InputNumber, Input, Select, Button, notification} from 'antd';
import TransactionList from "../transaction/TransactionList";
import {TRANSACTION_TYPE_PAYMENT, TRANSACTION_TYPE_SEND_MONEY, TRANSACTION_TYPE_TOP_UP} from "../constants";
const { Option } = Select;

class WalletDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            wallet: null,
            isLoading: false,
            isSendMoneyActive: false,
            transactionType: TRANSACTION_TYPE_TOP_UP,
            serialNumber: '',
            transactions: [],
            amount: 100
        }
        this.loadWallet = this.loadWallet.bind(this);
        this.changePaymentAction = this.changePaymentAction.bind(this);
        this.performPaymentAction = this.performPaymentAction.bind(this);
        this.handleChangeAmount = this.handleChangeAmount.bind(this);
        this.serialNumberChange = this.serialNumberChange.bind(this);
    }

    loadWallet(id) {
        this.setState({
            isLoading: true,
        });

        getWallet(id)
            .then(response => {
                this.setState({
                    wallet: response.data,
                    isLoading: false
                });
            }).catch(error => {
            if(error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else {
                this.setState({
                    serverError: true,
                    isLoading: false
                });
            }
        });
    }

    changePaymentAction(value) {

        if (value === 'SEND_MONEY') {
            this.setState({
                isSendMoneyActive: true,
                transactionType: value
            })
        } else {
            this.setState({
                isSendMoneyActive: false,
                transactionType: value
            })
        }
    }

    handleChangeAmount(amount) {
        this.setState({
            amount: amount
        })
    }

    performPaymentAction(event) {
        event.preventDefault();
        if (this.state.transactionType === TRANSACTION_TYPE_SEND_MONEY) {
            sendMoney(this.props.match.params.id, this.state.serialNumber, this.state.amount)
                .then(response => {
                    this.loadWallet(this.props.match.params.id)
                    notification.success({
                        message: 'Wallet App',
                        description: response.result.message
                    });
                }).catch(error => {
                    notification.error({
                        message: 'Wallet App',
                        description: error.result.message
                    });
                });
        } else {
            sendTransaction(this.props.match.params.id, this.state.transactionType, this.state.amount)
                .then(response => {
                    this.loadWallet(this.props.match.params.id)
                    notification.success({
                        message: 'Wallet App',
                        description: response.result.message
                    });
                })
                .catch(error => {
                    notification.error({
                        message: 'Wallet App',
                        description: error.result.message || 'Sorry! Something went wrong. Please try again!'
                    });
                });
        }
    }

    serialNumberChange(event) {
        this.setState({
            serialNumber: event.target.value
        })
    }

    componentDidMount() {
        const id = this.props.match.params.id;
        this.loadWallet(id);
    }

    componentDidUpdate(nextProps) {
        if(this.props.match.params.id !== nextProps.match.params.id) {
            this.loadWallet(nextProps.match.params.id);
        }
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }

        if(this.state.notFound) {
            return <NotFound />;
        }

        if(this.state.serverError) {
            return <ServerError />;
        }

        return (
            <div className="profile">
                {
                    this.state.wallet ? (
                        <div className="user-profile">
                            <div className="user-details">
                                <div className="wallet-input">
                                   <span style={{float: "left"}}>
                                        <InputNumber
                                            addonBefore="+"
                                            addonAfter="$"
                                            step="0.50"
                                            defaultValue={100}
                                            onChange={this.handleChangeAmount}
                                        />
                                    </span>
                                    <Select
                                        style={{ width: '130px', float: "left"}}
                                        defaultValue={TRANSACTION_TYPE_TOP_UP}
                                        onChange={this.changePaymentAction}
                                    >
                                        <Option value={TRANSACTION_TYPE_TOP_UP}>Top-Up</Option>
                                        <Option value={TRANSACTION_TYPE_PAYMENT}>Payment</Option>
                                        <Option value={TRANSACTION_TYPE_SEND_MONEY}>Send Money</Option>
                                    </Select>
                                    { this.state.isSendMoneyActive &&
                                    <span style={{float: "left"}}>
                                        <Input placeholder="Wallet Serial Number"
                                               onChange={this.serialNumberChange}
                                               value={this.state.serialNumber}
                                        />
                                    </span>
                                    }
                                    <span>
                                        <Button type="primary"
                                                style={{marginLeft: '10px'}}
                                                onClick={this.performPaymentAction}
                                        >Complete</Button>
                                    </span>
                                </div>

                                <div className="user-summary">
                                    <div className="full-name">Wallet Name: {this.state.wallet.name}</div>
                                    <div>Balance: {this.state.wallet.balance}</div>
                                    <div className="user-joined">
                                        Serial Number: {this.state.wallet.serial_number}
                                    </div>
                                </div>
                            </div>
                            <div>
                                <TransactionList wallet={this.props.match.params.id} transactions={this.state.transactions} />
                            </div>
                        </div>
                    ): null
                }
            </div>
        );
    }
}

export default withRouter(WalletDetails);