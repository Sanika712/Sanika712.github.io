import os
import pickle
import base64
from google.auth.transport.requests import Request
import google_auth_oauthlib.flow
import googleapiclient.discovery
import googleapiclient.errors
from transformers import pipeline

# ---- CONFIG ----
SCOPES = ['https://www.googleapis.com/auth/gmail.readonly']
CLIENT_SECRET_FILE = 'T:\\CS\\pulse\\CredentialsDesktop.json'  # ✅ Make sure this is correct (not .json.json)
API_NAME = 'gmail'
API_VERSION = 'v1'

# ---- GMAIL AUTH ----
def get_gmail_service():
    creds = None
    if os.path.exists('token.pickle'):
        with open('token.pickle', 'rb') as token:
            creds = pickle.load(token)
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = google_auth_oauthlib.flow.InstalledAppFlow.from_client_secrets_file(
                CLIENT_SECRET_FILE, SCOPES
            )
            creds = flow.run_local_server(port=0)
        with open('token.pickle', 'wb') as token:
            pickle.dump(creds, token)
    return googleapiclient.discovery.build(API_NAME, API_VERSION, credentials=creds)

# ---- FETCH EMAIL ----
def get_messages(service, num_messages=1):
    try:
        results = service.users().messages().list(userId='me', maxResults=num_messages).execute()
        return results.get('messages', [])
    except googleapiclient.errors.HttpError as error:
        print(f'Error fetching messages: {error}')
        return []

def get_email_body(service, message_id):
    try:
        msg = service.users().messages().get(userId='me', id=message_id, format='full').execute()
        payload = msg['payload']
        parts = payload.get('parts', [])
        email_body = ""
        for part in parts:
            if part['mimeType'] == 'text/plain':
                email_body = part['body']['data']
                email_body = base64.urlsafe_b64decode(email_body).decode('utf-8')
                break
        return email_body if email_body else msg.get('snippet', '')
    except googleapiclient.errors.HttpError as error:
        print(f'Error retrieving email {message_id}: {error}')
        return ""

# ---- SUMMARIZATION ----
def summarize_text(text):
    summarizer = pipeline("summarization", model="t5-small")
    input_text = "summarize: " + text.strip().replace('\n', ' ')
    input_text = input_text[:1000]  # Limit length for model input
    summary = summarizer(input_text, max_length=100, min_length=30, do_sample=False)
    return summary[0]['summary_text']

# ---- MAIN RUNNER ----
def run():
    service = get_gmail_service()
    messages = get_messages(service, num_messages=1)

    if not messages:
        print("No messages found.")
        return

    for msg in messages:
        email_body = get_email_body(service, msg['id'])
        print("\n📨 Original Email:\n")
        print(email_body.strip())

        print("\n🧠 Summary:\n")
        print(summarize_text(email_body))

if __name__ == '__main__':
    run()
