package org.sbolstandard.core3.entity.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.api.SBOLAPI;
import junit.framework.TestCase;

public class NetworkTest extends TestCase {

	public void testNetwork() throws SBOLGraphException, IOException, Exception {
		Configuration.getInstance().setValidateAfterReadingSBOLDocuments(false);
		/*File file = new File("/Users/goksel/Downloads/PoPSReceiverDetailed.ttl");
		SBOLDocument doc = SBOLIO.read(file);				
		SBOLAPI.printConnectivity(doc);
 		System.out.println("  Successfully printed the connectivity.");
		*/
		Path outputDir = Paths.get(TestUtil.baseOutput);
		List<Path> sbolFiles = Files.walk(outputDir)
				.filter(p -> p.toString().endsWith(".rdf"))
				.sorted()
				.collect(Collectors.toList());

		for (Path filePath : sbolFiles) {
			System.out.println("\n=== File: " + filePath + " ===");
			try {
				SBOLDocument doc = SBOLIO.read(filePath.toFile());				
				SBOLAPI.printConnectivity(doc);
                System.out.println("  Successfully printed the connectivity.");
			} catch (Exception e) {
				System.out.println("  Error reading file: " + e.getMessage());
			}
		}			

		Configuration.getInstance().setValidateAfterReadingSBOLDocuments(true);
	}

}
