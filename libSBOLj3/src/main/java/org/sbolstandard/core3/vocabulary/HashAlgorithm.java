package org.sbolstandard.core3.vocabulary;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 *  Represents the hashing algorithms used in the SBOL data model.
 *
 */
public enum HashAlgorithm {
	sha_256("sha-256"), 
	sha_256_128("sha-256-128"),
	sha_256_120("sha-256-120"),
	sha_256_96("sha-256-96"),
	sha_256_64("sha-256-64"),
	sha_256_32("sha-256-32"),
	sha_384("sha-384"),
	sha_512("sha-512"),
	sha3_224("sha3-224"),
	sha3_256("sha3-256"),
	sha3_384("sha3-384"),
	sha3_512("sha3-512"),
	blake2s_256("blake2s-256"),
	blake2b_256("blake2b-256"),
	blake2b_512("blake2b-512"),
	k12_256("k12-256"),
	k12_512("k12-512");
	
	private String value;

	HashAlgorithm(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the value associated with the hash algorithm.
	 * @return The corresponding value in the HashAlgorithm object.
	 */
	public String getValue() {
		return value;
	}
	
	private static final Map<String, HashAlgorithm> lookup = new HashMap<>();
	  
    static
    {
        for(HashAlgorithm alg : HashAlgorithm.values())
        {
            lookup.put(alg.getValue(), alg);
        }        
    }
    
    /**
     * Gets the hashing algorithm identified by the supplied value.
     * @param value The value to be searched for.
     * @return The corresponding HashAlgorithm object.
     */
    public static HashAlgorithm get(String value) 
    {
        return lookup.get(value);
    }

}
