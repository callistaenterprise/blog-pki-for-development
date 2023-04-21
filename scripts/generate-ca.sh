#!/bin/bash

CA_NAME=${1:-localCA}

# Generate CA private key
openssl genrsa -out ${CA_NAME}.key 4096

# Create CA certificate
openssl req -x509 -new -nodes -key ${CA_NAME}.key -config ${CA_NAME}.conf -sha256 -days 1024 -out ${CA_NAME}.crt

# Export public key into trustStore in p12 format
# Since Java doesn't accept default PKCS12 truststores produced with OpenSSL, using keytool is required
keytool -import -file ${CA_NAME}.crt -alias ${CA_NAME} -storepass secret -noprompt -storetype PKCS12 \
	-keystore ${CA_NAME}.p12
