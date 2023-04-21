#!/bin/bash

CERTIFICATE_NAME=$1
CA_NAME=${2:-localCA}

# Generate private key
openssl genrsa -out ${CERTIFICATE_NAME}.key 2048

# Create CSR
openssl req -new -key ${CERTIFICATE_NAME}.key -config ${CERTIFICATE_NAME}.conf -out ${CERTIFICATE_NAME}.csr

# Issue certificate from CSR
openssl x509 -req -in ${CERTIFICATE_NAME}.csr -CA ${CA_NAME}.crt -CAkey ${CA_NAME}.key -CAcreateserial \
-out ${CERTIFICATE_NAME}.crt -days 825 -sha256 -passin pass: -extfile ${CERTIFICATE_NAME}.ext

# Export private and public keys into p12 format
openssl pkcs12 -export -out ${CERTIFICATE_NAME}.p12 -inkey ${CERTIFICATE_NAME}.key -in ${CERTIFICATE_NAME}.crt \
 -passout pass:secret -name ${CERTIFICATE_NAME}

# Delete CSR
rm -f ${CERTIFICATE_NAME}.csr

# Delete serial
rm -f ${CA_NAME}.srl
