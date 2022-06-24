import React, { Component } from 'react';
import { getAllWallets } from '../util/APIUtils';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon } from 'antd';
import Wallet from './Wallet'
import {ACCESS_TOKEN, WALLET_LIST_SIZE} from '../constants';
import { withRouter, Link } from 'react-router-dom';
import './WalletList.css';

class WalletList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            wallets: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            isLoading: false
        };
        this.loadWalletList = this.loadWalletList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadWalletList(page = 0, size = WALLET_LIST_SIZE) {
        let promise = getAllWallets(page, size);
        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                const wallets = this.state.wallets.slice();
                this.setState({
                    wallets: wallets.concat(response.data.wallets),
                    page: response.data.page,
                    size: response.data.size,
                    totalElements: response.data.total_element_size,
                    totalPages: response.data.total_page_size,
                    last: response.data.last,
                    isLoading: false
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });

    }

    componentDidMount() {

        if (!localStorage.getItem(ACCESS_TOKEN)) {
            this.props.history.push("/login")
        }  else {
            this.loadWalletList();
        }
    }

    componentDidUpdate(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                wallets: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                isLoading: false
            });
            this.loadWalletList();
        }
    }

    handleLoadMore() {
        this.loadWalletList(this.state.page + 1);
    }


    render() {
        const walletViews = [];
        this.state.wallets.forEach((wallet, walletIndex) => {
            walletViews.push(<Wallet
                key={wallet.id}
                wallet={wallet} />)
        });

        return (
            <div className="wallets-container">
                {localStorage.getItem(ACCESS_TOKEN) &&
                    <Link to="/wallet/create">
                        <div style={{margin: "0 auto"}}>
                            <Button style={{width: '100%', margin: "20px 0"}} type="primary">Create Wallet</Button>
                        </div>
                    </Link>
                }
                {walletViews}
                {
                    !this.state.isLoading && this.state.wallets.length === 0 && this.state.isAuthenticated ? (
                        <div className="no-wallets-found">
                            <span>No Wallets Found.</span>
                        </div>
                    ): null
                }
                {
                    !this.state.isLoading && !this.state.last ? (
                        <div className="load-more-wallets">
                            <Button type="dashed" onClick={this.handleLoadMore} disabled={this.state.isLoading}>
                                <Icon type="plus" /> Load more
                            </Button>
                        </div>): null
                }
                {
                    this.state.isLoading ?
                        <LoadingIndicator />: null
                }
            </div>
        );
    }
}

export default withRouter(WalletList);