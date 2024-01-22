# Instructions to Add the Certificate to Your Browser

This project uses a local certificate for development ease, so you need to manually add this certificate to your browser. (Note that without this certificate, you may be able to access the front end, but API calls will be impossible.)

Follow these steps:

## Step 1: Open the Browser

Open your web browser (e.g., Google Chrome, Mozilla Firefox, or Microsoft Edge).

## Step 2: Access Settings

- **Google Chrome:** Click on the three vertical dots in the top right, select "Settings," then scroll down and click on "Advanced." Under "Privacy and security," click on "Manage certificates."

- **Mozilla Firefox:** Click on the menu in the top right, select "Options," then click on "Privacy & Security." Under "Certificates," click on "View Certificates."

- **Microsoft Edge:** Click on the three horizontal dots in the top right, select "Settings," then scroll down and click on "View advanced settings." Under "Privacy, search, and services," click on "Manage certificates."

## Step 3: Import the Root Certificate

- **Google Chrome:** In the "Certificate Authorities" tab, click "Import," select the certificate (`RootCA.crt`) found in the Certificat folder at the root of the project, and follow the instructions to import it.

- **Mozilla Firefox:** In the "Authorities" tab, click "Import," select the certificate (`RootCA.crt`) found in the Certificat folder at the root of the project, and follow the instructions to import it.

- **Microsoft Edge:** In the "Trusted Root Certification Authorities" tab, click "Import," select the certificate (`RootCA.crt`) found in the Certificat folder at the root of the project, and follow the instructions to import it.

## Step 4: Restart the Browser

Restart your browser for the changes to take effect.

Now, your browser should trust the certificate (`RootCA.crt`), and you should no longer receive security warnings when accessing the website, and the front-end should make API calls via HTTPS without issues. Ensure you follow these steps carefully to ensure the security of your internet browsing.
