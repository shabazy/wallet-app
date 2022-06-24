import React, { Component } from 'react';
import Transaction from "./Transaction";
import {Button, Icon} from "antd";
import LoadingIndicator from "../common/LoadingIndicator";
import {getTransactions} from "../util/APIUtils";
import './Transaction.css'

class TransactionList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            walletTransactions: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            isLoading: false
        };
        this.handleLoadMore = this.handleLoadMore.bind(this);
        this.loadTransactions = this.loadTransactions.bind(this);
    }

    componentDidMount() {
        const id = this.props.wallet;
        this.loadTransactions(id);
    }

    loadTransactions(walletId, page = 0, size = 10) {
        this.setState({
            isLoading: true
        });

        getTransactions(walletId, page, size).then(response => {
            const walletTransactions = this.state.walletTransactions.slice();
            this.setState({
                walletTransactions: walletTransactions.concat(response.data.walletTransactions),
                page: response.data.page,
                size: response.data.size,
                totalElements: response.data.total_element_size,
                totalPages: response.data.total_page_size,
                last: response.data.last,
                isLoading: false
            })
        })
    }

    handleLoadMore() {
        this.loadTransactions(this.props.wallet, this.state.page + 1);
    }

    render() {
        const transactionViews = [];
        this.state.walletTransactions.forEach((walletTransaction, transactionIndex) => {
            transactionViews.push(<Transaction key={transactionIndex} walletTransaction={walletTransaction}/>)
            console.log(transactionViews.length);
        });
        return (
            <div>
                {
                    this.state.walletTransactions.length > 0 &&
                        <table style={{margin: "0 auto"}}>
                            <tbody>
                                <tr>
                                    <td style={{fontWeight: "bold", fontSize: "16px"}}>Transaction History</td>
                                </tr>
                                <tr>
                                    <td className="tx-column" style={{fontWeight: "bold"}}>Reference</td>
                                    <td className="tx-column" style={{fontWeight: "bold"}}>Amount</td>
                                    <td className="tx-column" style={{fontWeight: "bold"}}>Status</td>
                                    <td className="tx-column" style={{fontWeight: "bold"}}>Type</td>
                                    <td className="tx-column" style={{fontWeight: "bold"}}>Description</td>
                                    <td className="tx-column" style={{fontWeight: "bold"}}>Date</td>
                                </tr>
                                {transactionViews}
                                {
                                    !this.state.isLoading && !this.state.last &&
                                        <tr>
                                            <td>
                                                <div className="load-more" style={{margin: "10px auto", width: "50%"}}>
                                                    <Button type="dashed" onClick={this.handleLoadMore} disabled={this.props.isLoading}>
                                                        <Icon type="plus" /> Load more
                                                    </Button>
                                                </div>
                                            </td>
                                        </tr>
                                }
                            </tbody>
                        </table>
                }

                {
                    !this.state.isLoading && this.state.walletTransactions.length === 0 ? (
                        <div className="no-tx-found" style={{margin: "0 auto", width: "50%"}}>
                            <span>No Transactions Found.</span>
                        </div>
                    ): null
                }

                {
                    this.state.isLoading ?
                        <LoadingIndicator />: null
                }
            </div>
        );
    }
}

export default TransactionList;