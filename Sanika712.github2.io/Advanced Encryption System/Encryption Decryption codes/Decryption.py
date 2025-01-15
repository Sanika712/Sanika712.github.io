import os
import sys
from cryptography.fernet import Fernet  # For encryption/decryption

# Function to encrypt the data
def encrypt_data(data, key):
    cipher = Fernet(key)
    encrypted_data = cipher.encrypt(data)
    return encrypted_data

# Function to decrypt the data
def decrypt_data(encrypted_data, key):
    cipher = Fernet(key)
    decrypted_data = cipher.decrypt(encrypted_data)
    return decrypted_data

# Generate encryption key
key = Fernet.generate_key()

# Write the key to the file
with open("C:\\Users\\tusha\\Desktop\\AES\\code\\key.txt", 'r') as key_file:
    # Code inside the 'with' block
    key=key_file.read()

# Function to encrypt files in a directory
def encrypt_files_in_directory(directory):
    # Loop through files in the directory
    for filename in os.listdir(directory):
        file_path = os.path.join(directory, filename)
        
        # Check if the file is a regular file
        if os.path.isfile(file_path):
            with open(file_path, 'rb') as file:
                data = file.read()
            
            # Encrypt the data
            encrypted_data = encrypt_data(data, key)
            
            # Write the encrypted data back to the file
            with open(file_path + '.encrypted', 'wb') as encrypted_file:
                encrypted_file.write(encrypted_data)
                
            # Optionally, delete the original unencrypted file
            # os.remove(file_path)

        # If the item is a directory, recursively encrypt its contents
        elif os.path.isdir(file_path):
            encrypt_files_in_directory(file_path)

# Function to decrypt files in a directory
def decrypt_files_in_directory(directory):
    # Loop through files in the directory
    for filename in os.listdir(directory):
        file_path = os.path.join(directory, filename)
        
        # Check if the file is an encrypted file
        if os.path.isfile(file_path) and file_path.endswith('.encrypted'):
            with open(file_path, 'rb') as encrypted_file:
                encrypted_data = encrypted_file.read()
            
            # Decrypt the data
            decrypted_data = decrypt_data(encrypted_data, key)
            
            # Create a directory for decrypted files if it doesn't exist
            decrypted_folder = os.path.join(directory, 'decrypted')
            if not os.path.exists(decrypted_folder):
                os.makedirs(decrypted_folder)
            
            # Write the decrypted data to a file in the decrypted directory
            decrypted_file_path = os.path.join(decrypted_folder, filename[:-10])  # Remove the '.encrypted' extension
            with open(decrypted_file_path, 'wb') as decrypted_file:
                decrypted_file.write(decrypted_data)
                
            # Optionally, delete the encrypted file
            # os.remove(file_path)

        # If the item is a directory, recursively decrypt its contents
        elif os.path.isdir(file_path):
            decrypt_files_in_directory(file_path)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python script.py <root_directory>")
        sys.exit(1)

    root_dir = sys.argv[1]

    # Encrypt files in the specified root directory and its subfolders
    # encrypt_files_in_directory(root_dir)

    # Decrypt files in the specified root directory and its subfolders
    decrypt_files_in_directory(root_dir)
