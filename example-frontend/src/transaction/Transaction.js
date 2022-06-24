import React, { Component } from 'react';
import "./Transaction.css"

class Transaction extends Component {

    render() {
        return (
            <tr style={{margin: "0 auto", width: "50%"}}>
                <td className="tx-column">{this.props.walletTransaction.reference}</td>
                <td className="tx-column">{this.props.walletTransaction.amount}</td>
                <td className="tx-column">{this.props.walletTransaction.walletTransactionStatus}</td>
                <td className="tx-column">{this.props.walletTransaction.transactionType}</td>
                <td className="tx-column">{this.props.walletTransaction.description}</td>
                <td className="tx-column">{this.props.walletTransaction.date}</td>
            </tr>
        );
    }

}

export default Transaction;