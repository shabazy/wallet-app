export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8085/api';
export const ACCESS_TOKEN = 'accessToken';

export const WALLET_LIST_SIZE = 10;

export const NAME_MIN_LENGTH = 4;
export const NAME_MAX_LENGTH = 40;

export const EMAIL_MAX_LENGTH = 40;

export const PASSWORD_MIN_LENGTH = 6;
export const PASSWORD_MAX_LENGTH = 20;

export const PHONE_NUMBER_MIN_LENGTH = 12;
export const PHONE_NUMBER_MAX_LENGTH = 15;
export const TRANSACTION_TYPE_SEND_MONEY = "SEND_MONEY";
export const TRANSACTION_TYPE_TOP_UP = "TOP_UP";
export const TRANSACTION_TYPE_PAYMENT = "PAYMENT";