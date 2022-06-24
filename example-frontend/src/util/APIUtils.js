import { API_BASE_URL, WALLET_LIST_SIZE, ACCESS_TOKEN } from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    
    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response => 
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};

export function getAllWallets(page, size) {
    page = page || 0;
    size = size || WALLET_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/wallets?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function getWallet(id) {
    return request({
        url: API_BASE_URL + "/wallets/" + id,
        method: 'GET'
    });
}

export function createWallet(name) {
    return request({
        url: API_BASE_URL + "/wallets",
        method: 'POST',
        body: JSON.stringify({ name: name })
    });
}

export function getTransactions(walletId, page, size) {
    return request({
        url: API_BASE_URL + "/wallets/" + walletId + "/transactions?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function sendTransaction(walletId, transactionType, amount) {
    return request({
        url: API_BASE_URL + "/wallets/" + walletId + "/transactions",
        method: 'POST',
        body: JSON.stringify({ transaction_type: transactionType, amount: amount })
    });
}

export function sendMoney(walletId, walletSerialNumber, amount) {
    return request({
        url: API_BASE_URL + "/wallets/" + walletId + "/transactions/send-money",
        method: 'POST',
        body: JSON.stringify({ target_wallet_serial_number: walletSerialNumber, amount: amount })
    });
}

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/users/login",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/users/register",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/users/info",
        method: 'GET'
    });
}