#https://techsparx.com/software-development/docker/damp/mysql-ssl-connection.html

#sudo docker pull nginx

export OPENSSL_SUBJ="/C=IT/ST=Ireland/L=Cork"
export ENV OPENSSL_CA="${OPENSSL_SUBJ}/CN=ca.francescofiora.it"
export ENV OPENSSL_SERVER="${OPENSSL_SUBJ}/CN=dbbooks.francescofiora.it"
export ENV OPENSSL_MYSQL_CLIENT="${OPENSSL_SUBJ}/CN=dbbooks.francescofiora.it"
export ENV OPENSSL_MYADMIN_CLIENT="${OPENSSL_SUBJ}/CN=myadmin.francescofiora.it"
export ENV OPENSSL_CLIENT="${OPENSSL_SUBJ}/CN=localhost"

# Generate new CA certificate ca.pem file.
if [ ! -d ./certs ]; then
	mkdir certs
fi

# Generate new CA certificate ca.pem file.
if [ ! -f ./certs/ca-key.pem ]; then
	openssl genrsa -out ./certs/ca-key.pem 2048
fi

if [ ! -f ./certs/ca.pem ]; then
	openssl req -new -x509 -nodes -days 3600 -subj "${OPENSSL_CA}" -key ./certs/ca-key.pem -out ./certs/ca.pem
fi

# Create the server-side certificates
if [ ! -f ./certs/server-key.pem ]; then
	openssl req -newkey rsa:2048 -days 3600 -nodes -subj "${OPENSSL_SERVER}" -keyout ./certs/server-key.pem -out ./certs/server-req.pem
	openssl rsa -in ./certs/server-key.pem -out ./certs/server-key.pem
fi	

if [ ! -f ./certs/server-cert.pem ]; then
	openssl x509 -req -in ./certs/server-req.pem -days 3600 -CA ./certs/ca.pem -CAkey ./certs/ca-key.pem -set_serial 01 -out ./certs/server-cert.pem
fi

# Create the client-side certificates
if [ ! -f ./certs/client-key.pem ]; then
	openssl req -newkey rsa:2048 -days 3600 -nodes -subj "${OPENSSL_MYSQL_CLIENT}" -keyout ./certs/client-key.pem -out ./certs/client-req.pem
	openssl rsa -in ./certs/client-key.pem -out ./certs/client-key.pem
fi

if [ ! -f ./certs/client-cert.pem ]; then
	openssl x509 -req -in ./certs/client-req.pem -days 3600 -CA ./certs/ca.pem -CAkey ./certs/ca-key.pem -set_serial 01 -out ./certs/client-cert.pem
fi

# Create the myadmin client-side certificates
if [ ! -f ./certs/myadmin-key.pem ]; then
	openssl req -newkey rsa:2048 -days 3600 -nodes -subj "${OPENSSL_MYADMIN_CLIENT}" -keyout ./certs/myadmin-key.pem -out ./certs/myadmin-req.pem
	openssl rsa -in ./certs/myadmin-key.pem -out ./certs/myadmin-key.pem
fi

if [ ! -f ./certs/myadmin-cert.pem ]; then
	openssl x509 -req -in ./certs/myadmin-req.pem -days 3600 -CA ./certs/ca.pem -CAkey ./certs/ca-key.pem -set_serial 01 -out ./certs/myadmin-cert.pem
fi

# Create the localhost client-side certificates
if [ ! -f ./certs/localhost-key.pem ]; then
	openssl req -newkey rsa:2048 -days 3600 -nodes -subj "${OPENSSL_MYADMIN_CLIENT}" -keyout ./certs/localhost-key.pem -out ./certs/localhost-req.pem
	openssl rsa -in ./certs/localhost-key.pem -out ./certs/localhost-key.pem
fi

if [ ! -f ./certs/localhost-cert.pem ]; then
	openssl x509 -req -in ./certs/localhost-req.pem -days 3600 -CA ./certs/ca.pem -CAkey ./certs/ca-key.pem -set_serial 01 -out ./certs/localhost-cert.pem
fi

# Verify the certificates are correct
openssl verify -CAfile ./certs/ca.pem ./certs/server-cert.pem ./certs/client-cert.pem ./certs/myadmin-cert.pem ./certs/localhost-cert.pem


if [ ! -f ./certs/localhost-keystore.p12 ]; then
	openssl pkcs12 -export -in ./certs/localhost-cert.pem -inkey ./certs/localhost-key.pem -passout pass:mypass -out ./certs/localhost-keystore.p12
fi

if [ ! -f ./certs/truststore.ts ]; then
	keytool -importcert -file ./certs/ca.pem -keystore ./certs/truststore.ts -storepass mypass -noprompt
fi

if [ ! -f ./certs/localhost-keystore.jks ]; then
	keytool -importkeystore -srckeystore ./certs/localhost-keystore.p12 -srcstoretype pkcs12 -srcstorepass mypass -destkeystore ./certs/localhost-keystore.jks -deststoretype JKS -deststorepass mypass
fi

chmod a=r ./certs/ca.pem ./certs/server-key.pem ./certs/client-key.pem ./certs/myadmin-key.pem ./certs/localhost-key.pem
