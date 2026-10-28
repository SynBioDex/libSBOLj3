# libSBOLj3 - A Java library for the Synthetic Biology Open Language 3
The libSBOLj Java library has been developed for the [Synthetic Biology Open Language 3.0](https://sbolstandard.org/data-model-specification). The library is  under development and is currently available as an alpha release. 

SBOL represents data using RDF graphs, which can be serialised in different formats. The libSBOLj3 library supports the following RDF formats.
* RDF/XML (File extension: rdf)
* Turtle (File extension: ttl)
* N-Triples (File extension: nt)
* JSON-LD (File extension: jsonld)

## How to use libSBOLj3

### To use the latest version from the source as a Maven dependency in a Maven project
First, download the project and install it using Maven.
```
git clone https://github.com/SynBioDex/libSBOLj3.git
cd libSBOLj3
mvn install -DskipTests=true
```

Then include it as a Maven dependency in your project's POM file.
``` 
</dependencies>
	...
   <dependency>
      <groupId>org.sbolstandard</groupId>
      <artifactId>libSBOLj3</artifactId>
      <version>1.0.5.3</version>
   </dependency>
   ...
</dependencies>

```

### To use the released version as a Maven dependency in a Maven project
Use this option if you are developing a Java application using [Maven](https://maven.apache.org/). 

* Step 1: Add the following libSBOLj3 dependency to your Maven applications's POM file (pom.xml). 


``` 
</dependencies>
	...
   <dependency>
    		<groupId>org.sbolstandard</groupId>
   		<artifactId>libsbolj3</artifactId>
    		<version>1.0.5.3</version>
   </dependency>
   ...
</dependencies>
```

* Step 2: Make sure that you include the GitHub repository entry in your project's POM file. 
```
<repositories>
	<repository>
      <id>github</id>
      <name>GitHub Packages (Releases)</name>
		<url>https://maven.pkg.github.com/SynBioDex/libSBOLj3</url>
   </repository>
</repositories>
```

* Step 3: This step is required only once to download Maven artefacts for any project. Create a GitHub token using your GitHub account if you haven't done this before. Go to https://github.com/settings/tokens and generate a new classic token for general use.
Cick on the ```Generate new token``` button and choose the ```Generate new token (classics) For general use``` option.

Finally link this GitHub token with your Maven by updating Maven's settings.xml file (```~/.m2/settings.xml```). Add the server information as shown below. If settings.xml does not exist, create it with the conrent below and update it with your details. 

```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">


 <servers>
    <server>
      <id>github</id>
      <username>[YOUR_GITHUB_USER]</username>
      <password>[YOUR_GITHUB_TOKEN]</password>
    </server>
  </servers>
</settings>
```


### As a Java dependency in a non-Maven project
The libSBOLj3 library is available as a JAR file. Please download the file from the [releases page](https://github.com/SynBioDex/libSBOLj3/tags). A single JAR file (with the "withDepencencies" suffix), which includes all the required libSBOLj3 related dependencies, is also available.

## SBOL Examples
[Several SBOL3 examples](https://github.com/SynBioDex/libSBOLj3/tree/master/libSBOLj3/output) are available as part of the libSBOLj3 library. These examples have also been made available as part of the [SBOL Test Suite](https://github.com/SynBioDex/SBOLTestSuite/tree/master/SBOL3). Some of these examples have beeen explained in the recent SBOL3 paper, titled "[The Synthetic Biology Open Language (SBOL) Version 3: Simplified Data Exchange for Bioengineering](https://doi.org/10.3389/fbioe.2020.01009)".

## Getting Started
Please see the [tutorial code](https://github.com/SynBioDex/libSBOLj3/tree/feature/combine2020/libSBOLj3/output/combine2020) and the [COMBINE 2020 slides](https://github.com/SynBioDex/Community-Media/blob/master/2020/COMBINE20/pySBOL3-COMBINE-2020.pptx) for more details. The tutorial code includes additional examples to crete interactions, constraints, component references and so on.

### Creating a new SBOL document
The SBOLDocument class is used to create SBOL documents which act as containers to create and access other SBOL entities. Although not required, the base URI can be used as prefix for all new SBOL entities. 
```java
URI base=URI.create("https://synbiohub.org/public/igem/");
SBOLDocument doc=new SBOLDocument(base);
```	
The following sections summarises how to create the [i13504](http://parts.igem.org/Part:BBa_I13504) device, formed of an RBS, a CDS and a terminator parts. Both the device and the parts are represented as SBOL components.
### Creating parts and sequences
The newly created SBOLDocument object then be used as a *factory* to create new SBOL entities. These entities can then be defined via their different properties. The following example creates an RBS part, which is represented as a Component in SBOL. 
```java
//Create the RBS component
Component rbs = doc.createComponent("B0034", Arrays.asList(ComponentType.DNA.getUrl())); 
rbs.setName("B0034");
rbs.setDescription("RBS (Elowitz 1999)");
rbs.setRoles(Arrays.asList(Role.RBS));
		
//Create a sequence entity for the RBS component
Sequence rbs_seq=doc.createSequence("B0034_Sequence");
rbs_seq.setElements("aaagaggagaaa");
rbs_seq.setEncoding(Encoding.NucleicAcid);
rbs.setSequences(Arrays.asList(rbs_seq.getUri()));	
```
The libSBOLj3 library, which provide both high level and low level APIs to construct sequences and to annotate sequence features. The above code can also be written as below using the high level API.
```java
Component rbs=SBOLAPI.createDnaComponent(doc, "B0034", "rbs", "RBS (Elowitz 1999)", Role.RBS, "aaagaggagaaa");	
```

### Sequence construction
 For example, a composite device can be constructed from simpler building blocks (e.g. rbs or cds) and sequence features (scar sequences). 
 Let's first define our composite device component that we want to create from simpler building blocks.
```java
Component device= SBOLAPI.createDnaComponent(doc, "i13504", "i13504", "Screening plasmid intermediate", ComponentType.DNA.getUrl(), null);	
```

The device is then constructed by adding other parts (rbs, cds and termintor components) and sequence features (scar sequences).
```java
SBOLAPI.appendComponent(doc, device,rbs,Orientation.inline);	
SBOLAPI.appendSequenceFeature(doc, device, "tactag", Orientation.inline);
SBOLAPI.appendComponent(doc, device,gfp, Orientation.inline);
SBOLAPI.appendSequenceFeature(doc, device, "tactagag", Orientation.inline);
SBOLAPI.appendComponent(doc, device,term, Orientation.inline);
```

These subcomponents and fetatures can be iterated using related properties.
```java
for (SubComponent subComp: device.getSubComponents()){
   System.out.println(subComp.getIsInstanceOf());
}
```

### Reading and writing SBOL documents
 The libSBOLj3 library provides methods to store SBOL documents in memory variables and to read documents from these variables.
 ```java
 //Write using the RDF Turtle format
 String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
 //Read using the RDF Turtle format
 SBOLDocument doc2=SBOLIO.read(output, SBOLFormat.TURTLE); 
```
 The libSBOLj3 library alsoprovides methods to store SBOL documents in files and to read documents from these files.

```java
//Write
 SBOLIO.write(doc, new File("sbol.ttl"), SBOLFormat.TURTLE);
 //Read
 SBOLIO.read(doc, new File("sbol.ttl"), SBOLFormat.TURTLE);
``` 

The following constants can be used to set the RDF serialisation type:

```Turtle```, ```RDF/XML-ABBREV```, ```JSON-LD```, ```RDFJSON```, ```N-TRIPLES```.

### Looking up for SBOL entities
SBOL utilises URIs to link different entities. An SBOL entity may store a reference to another entity for more details. These additional details can be retrieved using the ```getIdentified``` method which expects the URI of the entity to retrieve, and its type. The followig example shows retrieving nucleotide sequences of the rbs component. The Sequence entity, the URI of which is referenced in the rbs component, is retrieved first. Its elements property is then used to read the nucleotides information.
```java
Sequence rbsSeq = doc.getIdentified(sequenceURI, Sequence.class);
String nucleotides = rbsSeq.getElements();	
```

### Looking up (Querying) using graph pattern matching
Multiple SBOL entities that can match to a given pattern can also be searched for. These enties are returned using SPARQL SELECT queries via the ```getIdentifieds``` method. This method expects a partial SPARQL query, which would normally be included between "```WHERE {```" and "```}```" in SPARQL queries. The rest of the query is constructed by libSBOLj3 using the URI prefixes that are already specified in SBOL documents. It is assumed that the first column of the SPARQL query result includes URIs of SBOL entities of one type only. For example, the following example retrieves all SBOL Component entities with the role:SO:0000141 (promoter) and type SBO:0000251 (DNA).
```java
List<Component> components=(List<Component>)doc.getIdentifieds("?identified a sbol:Component; sbol:role  SO:0000141; sbol:type SBO:0000251 .", Component.class);
   System.out.println("Graph query results:");
   for (Component component:components){
    	System.out.println("  " +  component.getDisplayId());
   }
```

The libSBOLj3 library creates the following SPARQL query to get the results using the code above.
```
PREFIX CHEBI: <https://identifiers.org/CHEBI:>
PREFIX GO: <https://identifiers.org/GO:>
PREFIX sbol: <http://sbols.org/v3#>
PREFIX EDAM: <https://identifiers.org/edam:>
PREFIX SO: <https://identifiers.org/SO:>
PREFIX prov: <http://www.w3.org/ns/prov#>
PREFIX om: <http://www.ontology-of-units-of-measure.org/resource/om-2/>
SELECT ?identified
WHERE {
   ?identified a sbol:Component; 
            sbol:role  SO:0000141; 
            sbol:type SBO:0000251 .
}
```


## Error reporting

The libSBOLj3 validation framework reports errors at entity or document level. Each error message has the following structure:

```
<Level> Validation test: <Message>,
   Value: <OPTIONAL - Set for offending literal value or values. Not included if the violation is due to a child entity>,
   Child Entity IRI: <OPTIONAL, the child entity's IRI. if the violation is due a child entity, in which case the Value property is not included.>,
   Child Entity Type: <OPTIONAL, SBOL entity type. Included if the Child Entity IRI is included.>
   Property: <REQUIRED - property path. The full path between the invalid top-level entity and the value (or the child entity) causing the violation.>,
   Entity URI: <REQUIRED - URI of the entity with the error>,
   Entity Type: <REQUIRED -  SBOL entity type>
```

where

* Level : ```Identified``` or ```Document```

* Message: Free text message. It starts with the SBOL validation code, if applicable

* Property path: Can be a property URI value or a path between start and end values or nodes. 
      
   * VALUE: A string property
      * E.g.: instanceOf
   * A_TOP_LEVEL_LIST[INDEX_OR_IRI].1ST_LEVEL_CHILD_LIST[INDEX_1_OR_IRI_1]...NTH_LEVEL_CHILD_LIST[INDEX_N_OR_IRI_N].VALUE: Each INDEX can be a numeric index value or a IRI
      * E.g.: components[0].subComponents[0].instanceOfURI
      * E.g.: components[3].hasFeature[https://synbiohub.org/public/igem/TetR/SubComponent1].instanceOf[https://synbiohub.org/public/igem/TetR_binding]



### Example error messages

**[Level: Identified] Missing properties** 
The example below is the result validating a SubComponent entity. The entity includes two validation errors which are reported as below. While the first error is directly related to the entity, the second entity is due to a Location child entity of the SubComponent entity.
```
Identified Validation test: SubComponent.isInstanceOf cannot be null.,
	Property: instanceOfURI,
	Entity URI: https://synbiohub.org/public/igem/i13504/SubComponent1,
	Entity Type: SubComponent
Identified Validation test: Range.end cannot be empty.,
	Property: locations[0].end,
	Entity URI: https://synbiohub.org/public/igem/i13504/SubComponent1/Range1,
	Entity Type: Range
```


**[Level: Document] Missing required property (`Range.end`), reported at document level**
The example below shows when the SBOLDocument including the SubComponent from the above example is validated. The property path now shows the link between the top-level parent Component entity and the child SubComponent entity's instanceOf property.
   ```components[0].subComponents[0].instanceOfURI```

Similarly, the property path for the second error now shows the link between the top-level parent Component entity and the child SubComponent entity's location entity's end property:
   ```components[0].subComponents[0].locations[0].end```

```
Document Validation test: SubComponent.isInstanceOf cannot be null.,
	Property: components[0].subComponents[0].instanceOfURI,
	Entity URI: https://synbiohub.org/public/igem/i13504/SubComponent1,
	Entity Type: SubComponent
Document Validation test: Range.end cannot be empty.,
	Property: components[0].subComponents[0].locations[0].end,
	Entity URI: https://synbiohub.org/public/igem/i13504/SubComponent1/Range1,
	Entity Type: Range
```
**Rule violation with error code (`sbol3-10802`)**
```
Identified Validation test: sbol3-10802 - The roleIntegration property of a SubComponent is REQUIRED if the SubComponent has one or more role properties.,
	Property: roleIntegration,
	Entity URI: https://synbiohub.org/public/igem/i13504/SubComponent1,
	Entity Type: SubComponent
```

**Reporting invalid values or invalid child entities**
```
Document Validation test: sbol3-10803 - The instanceOf property of a SubComponent MUST NOT refer to the same Component as the one that contains the SubComponent.,
	Value: https://synbiohub.org/public/igem/TetR,
	Property: components[2].hasFeature[https://synbiohub.org/public/igem/TetR/SubComponent2].instanceOf,
	Entity URI: https://synbiohub.org/public/igem/TetR,
	Entity Type: Component
```

Please note that values can be a set as shown below. The attachment has two further attachments, which have different entity types and hence are not valid.
```
Document Validation test: sbol3-10111 - An object's property values MUST have the type listed for the object type and property in Table 23.,
	Value: [https://sbolstandard.org/examples/BBa_R0040, https://sbolstandard.org/examples/pLacI],
	Property: attachments[0].hasAttachment,
	Entity URI: https://sbolstandard.org/examples/attachment1,
	Entity Type: Attachment
```

The following is also possible. The last property has a IRI key pointing to the invalid child entity which also has the same IRI.
```
	Child Entity URI: https://synbiohub.org/public/igem/i13504,
	Child Entity Type: Component,
	Property: hasFeature[https://synbiohub.org/public/igem/i13504/SubComponent1].instanceOf[https://synbiohub.org/public/igem/i13504],
	Entity URI: https://synbiohub.org/public/igem/i13504,
	Entity Type: Component
```

In the following example, the last property does not have a IRI key but the error message reports the child entity.
```
Document Validation test: sbol3-10901 - If a ComponentReference object is a child of a Component, then its inChildOf property MUST be a SubComponent of its parent.,
	Child Entity URI: https://synbiohub.org/public/igem/simpleDevice/SubComponent1,
	Child Entity Type: SubComponent,
	Property: components[1].hasFeature[https://synbiohub.org/public/igem/interlab16device1/ComponentReference1].inChildOf,
	Entity URI: https://synbiohub.org/public/igem/interlab16device1,
	Entity Type: Component
```

**Reporting invalid values involving multiple nodes and edges**
In the following example, the there are more nodes and edges involved. Component --> Interaction --> Participation --> invalidValue
```
Document Validation test: sbol3-11903 - The Interaction referenced by the higherOrderParticipant property of a Participation MUST be contained by the Component that contains the Interaction that contains the Participation. ,
	Value: http://someinvalidhigherorderparticipant.org,
	Property: components[2].hasInteraction[https://sbolstandard.org/examples/i13504_system/Interaction2].hasParticipation[https://sbolstandard.org/examples/i13504_system/Interaction2/Participation1].higherOrderParticipant,
	Entity URI: https://sbolstandard.org/examples/i13504_system,
	Entity Type: Component
```

```
Document Validation test: sbol3-11804 - If the hasParticipation properties of an Interaction refer to one or more Participation objects, and one of the type properties of this Interaction comes from Table 11, then the Participation objects SHOULD have a role from the set of role properties that is cross listed with this type in Table 12.,
	Property: components[2].interactions[0].hasParticipation[https://sbolstandard.org/examples/i13504_system/Interaction1/Participation1].role,
	Entity URI: https://sbolstandard.org/examples/i13504_system/Interaction1,
	Entity Type: Interaction
```


All validation errors produced during a test run are in `output/invalid/unit_tests/error_output_all.txt`. Individual per-entity error files are written to `output/invalid/unit_tests/error_output/`. Invalid SBOL files that triggered errors are stored under `output/invalid/unit_tests/invalid_files/` and are also organised by error codes under `output/invalid/unit_tests/error_codes/`.