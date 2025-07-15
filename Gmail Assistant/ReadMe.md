# LLM Summarizer Gmail Assistant

## How to Run

1. **Clone the repository**  
2. Install dependencies
3. Enable Gmail API & Create Credentials
4.Go to Google Cloud Console
5.Create a new project or select existing
6.Navigate to APIs & Services > Library and enable Gmail API
7. Go to APIs & Services > Credentials
8.Create OAuth 2.0 Client IDs credentials (choose Desktop or Web app)
9.Download the JSON file and save it as credentials.json in your backend directory
10.Set up OAuth 2.0
Follow the OAuth flow in your app to authorize and generate tokens
Make sure the credentials.json file is accessible by the backend
11. Store and refresh tokens as required (your app should handle this)
12.Run the backend server