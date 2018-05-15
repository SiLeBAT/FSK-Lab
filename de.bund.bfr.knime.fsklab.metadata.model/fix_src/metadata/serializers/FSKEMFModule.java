package metadata.serializers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emfjson.jackson.module.EMFModule;
import org.emfjson.jackson.resource.JsonResourceFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import metadata.GeneralInformation;
import metadata.MetadataPackage;


public class FSKEMFModule extends SimpleModule{

	private static final long serialVersionUID = 1476306866950929955L;

	public FSKEMFModule() {
		super("FSKEMFModule", Version.unknownVersion());

	    addSerializer(GeneralInformation.class, new JsonSerializer<GeneralInformation>() {
	      @Override
	      public void serialize(GeneralInformation value, JsonGenerator gen, SerializerProvider serializers)
	          throws IOException, JsonProcessingException {

	          gen.writeStartObject();

	            ObjectMapper mapper = EMFModule.setupDefaultMapper();
	            String jsonStr = mapper.writeValueAsString(value);
	        	
	          System.out.println(jsonStr);
	          gen.writeStringField("generalInformation", jsonStr);
//	          gen.writeObjectField("generalInformation", value);

	        gen.writeEndObject();
	      }
	    });

	    addDeserializer(GeneralInformation.class, new JsonDeserializer<GeneralInformation>() {
	      @Override
	      public GeneralInformation deserialize(JsonParser p, DeserializationContext ctxt)
	          throws IOException, JsonProcessingException {

	        final JsonNode node = p.readValueAsTree();
	        final String gIString = node.get("generalInformation").asText();
	        System.out.println(gIString);
	        
	        final ResourceSet resourceSet = new ResourceSetImpl();
	        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
	          .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JsonResourceFactory());
		      resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
		          MetadataPackage.eINSTANCE);
		      Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
		      InputStream stream = new ByteArrayInputStream(gIString.getBytes(StandardCharsets.UTF_8));
		      resource.load(stream, null);
	
		      return (GeneralInformation) resource.getContents().get(0);
	      }
	    });
	}
}
