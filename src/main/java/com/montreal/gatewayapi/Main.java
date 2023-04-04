package com.montreal.gatewayapi;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.hyperledger.fabric.client.*;
import org.hyperledger.fabric.client.identity.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String PATH_CERTIFICATE = "/home/developer/fabric-samples/montreal-network/crypto-config/peerOrganizations/orgmibh.montreal.com.br/ca/ca.orgmibh.montreal.com.br-cert.pem";
    private static final String PATH_PRIVATE_KEY = "/home/developer/fabric-samples/montreal-network/crypto-config/peerOrganizations/orgmibh.montreal.com.br/ca/ca.orgmibh.montreal.com.br-cert.pem";

    public static void main(final String[] args) throws CommitException, GatewayException, InterruptedException, IOException, InvalidKeyException, CertificateException {
        Reader certReader = Files.newBufferedReader(Path.of(PATH_CERTIFICATE));
        X509Certificate certificate = Identities.readX509Certificate(certReader);
        Identity identity = new X509Identity("mspId", certificate);

        Reader keyReader = Files.newBufferedReader(Path.of(PATH_PRIVATE_KEY));
        PrivateKey privateKey = Identities.readPrivateKey(keyReader);
        Signer signer = Signers.newPrivateKeySigner(privateKey);

        ManagedChannel grpcChannel = ManagedChannelBuilder.forAddress("178.18.0.1", 7051)
                .usePlaintext()
                .build();

        Gateway.Builder builder = Gateway.newInstance()
                .identity(identity)
                .signer(signer)
                .connection(grpcChannel);

        try (Gateway gateway = builder.connect()) {

            Network network = gateway.getNetwork("channelName");
            Contract contract = network.getContract("chaincodeName");

            byte[] putResult = contract.submitTransaction("put", "time", LocalDateTime.now().toString());
            System.out.println(new String(putResult, StandardCharsets.UTF_8));

            byte[] getResult = contract.evaluateTransaction("get", "time");
            System.out.println(new String(getResult, StandardCharsets.UTF_8));

        } finally {
            grpcChannel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
