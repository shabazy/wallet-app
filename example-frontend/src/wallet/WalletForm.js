import React, { Component } from 'react';
import {Button, Form, Input, notification} from "antd";
import { withRouter } from 'react-router-dom';
import {createWallet} from "../util/APIUtils";
const FormItem = Form.Item;

class WalletForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: ''
        }
        this.handleNameChange = this.handleNameChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleNameChange(event) {
        console.log(event);
        this.setState({
           name: event.target.value
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        createWallet(this.state.name).then(response => {
            this.props.history.push("/");
            notification.success({
                message: 'Wallet App',
                description: 'Wallet has been created.'
            });
        }).catch(err => {
            notification.error({
                message: 'Wallet App',
                description: err.result.message
            });
        });
    }

    render() {
        return (
            <div style={{width: "25%", margin: "20px auto"}}>
                <Form className="login-form">
                    <FormItem>
                        <Input
                            size="large"
                            name="name"
                            onChange={this.handleNameChange}
                            value={this.state.name}
                            placeholder="Wallet Name" />
                    </FormItem>
                    <FormItem>
                        <Button type="primary" onClick={this.handleSubmit} htmlType="submit" size="large" className="login-form-button">Create</Button>
                    </FormItem>
                </Form>
            </div>
        );
    }

}

export default withRouter(WalletForm);