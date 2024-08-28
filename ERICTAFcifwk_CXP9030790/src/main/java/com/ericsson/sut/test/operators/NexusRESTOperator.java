package com.ericsson.sut.test.operators;

import java.io.*;
import java.net.*;
import java.security.*;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;

@Operator(context = Context.REST)
public class NexusRESTOperator implements NexusOperator {

    URL url;
    URLConnection connection;
    InputStream inStream;
    Logger logger = Logger.getLogger(JenkinsRESTOperator.class);

    @Override
    public boolean getChecksumMatch(String nexusHostName,
            String nexusBaseDirectory, String group, String artifact,
            String version, String fileType, String checksumType,
            String checksumInstance) throws NoSuchAlgorithmException,
            IOException {
        String actual = getArtifactChecksum(nexusHostName, nexusBaseDirectory,
                group, artifact, version, fileType, checksumType,
                checksumInstance);
        String expected = getExpectedChecksum(nexusHostName,
                nexusBaseDirectory, group, artifact, version, fileType,
                checksumType);
        return actual.equals(expected);

    }

    public String getArtifactChecksum(String nexusHostName,
            String nexusBaseDirectory, String group, String artifact,
            String version, String fileType, String checksumType,
            String checksumInstance) throws IOException,
            NoSuchAlgorithmException {

        String result;

        url = new URL(nexusHostName + "/" + nexusBaseDirectory + "/" + group
                + "/" + artifact + "/" + version + "/" + artifact + "-"
                + version + "." + fileType);

        connection = url.openConnection();
        MessageDigest messageDigest = MessageDigest
                .getInstance(checksumInstance);
        inStream = connection.getInputStream();

        messageDigest.reset();
        byte[] bytes = new byte[messageDigest.getDigestLength()];
        int numBytes;
        while ((numBytes = inStream.read(bytes)) != -1) {
            messageDigest.update(bytes, 0, numBytes);
        }
        byte[] digest = messageDigest.digest();
        result = new String(Hex.encodeHex(digest));

        inStream.close();

        return result;
    }

    public String getExpectedChecksum(String nexusHostName,
            String nexusBaseDirectory, String group, String artifact,
            String version, String fileType, String checksumType)
            throws IOException {

        String result;

        url = new URL(nexusHostName + "/" + nexusBaseDirectory + "/" + group
                + "/" + artifact + "/" + version + "/" + artifact + "-"
                + version + "." + fileType + "." + checksumType);

        connection = url.openConnection();
        connection.setDoInput(true);
        inStream = connection.getInputStream();
        BufferedReader input = new BufferedReader(new InputStreamReader(
                inStream));
        result = input.readLine();

        inStream.close();

        return result;
    }

}
