package com.ericsson.sut.test.operators;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * NexusOperator is a Nexus TAF Operator interface.
 * 
 */
public interface NexusOperator {
    /**
     * 
     * @param nexusHostName             Name of Nexus Host Machine
     * @param nexusBaseDirectory        The Nexus base directory
     * @param group                     The Group which contains the Artifact
     * @param artifact                  The desired Atifact
     * @param version                   The desired Version
     * @param fileType                  The filetype of the Artifact
     * @param checksumType              The type of the checksum eg. md5 or sha1
     * @param checksumInstance          This is required to pares the checksum from the file. eg MD5 or SHA-1
     * 
     * @return String                   Checksum value of the file
     * @throws IOException 
     * @throws NoSuchAlgorithmException 
     */
    boolean getChecksumMatch (String nexusHostName, 
                                String nexusBaseDirectory, 
                                String group, 
                                String artifact,
                                String version,
                                String fileType,
                                String checksumType,
                                String checksumInstance) throws NoSuchAlgorithmException, IOException;
}

