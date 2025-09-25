# Contributing Guide

Thank you for your interest in contributing to the E-commerce API! To get your development environment set up, you'll
need to generate credentials for the external services used in this project.

## Required Credentials

### 1. Google OAuth2 Credentials

This project uses Google for social login. You will need a **Client ID** and **Client Secret**.

- **Instructions:** Follow the official Google Cloud guide for "Setting up OAuth 2.0". You will need to create a project
  and then create credentials for a "Web application".
- **Official Guide:** [Google Cloud Console - Setting up OAuth 2.0](https://support.google.com/cloud/answer/6158849)
- **Required Redirect URI:** When creating your credentials, Google will ask for an "Authorized redirect URI". You must
  use: `http://localhost:4403/login/oauth2/code/google`

Once you have your credentials, set them as the `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` environment variables.

### 2. Paystack API Keys

This project uses Paystack for payment processing. You will need a **Test Secret Key** and a **Test Public Key**.

- **Instructions:** Sign up for a free Paystack account and find your API keys on the developer dashboard.
- **Official Guide:
  ** [Paystack Documentation - Your API Keys](https://paystack.com/docs/api/authentication/#your-api-keys)

Once you have your keys, set them as the `PAYSTACK_TEST_SECRET_KEY` and `PAYSTACK_TEST_PUBLIC_KEY` environment
variables.
