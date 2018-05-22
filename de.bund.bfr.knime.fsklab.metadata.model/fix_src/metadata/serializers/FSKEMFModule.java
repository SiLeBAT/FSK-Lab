package metadata.serializers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.MetadataFactory;
import metadata.MetadataPackage;
import metadata.ModelMath;
import metadata.PopulationGroup;
import metadata.Scope;
import metadata.StringObject;
import metadata.impl.PopulationGroupImpl;

public class FSKEMFModule extends SimpleModule {

	public FSKEMFModule() {
		super("FSKEMFModule", Version.unknownVersion());

		addSerializer(GeneralInformation.class, new JsonSerializer<GeneralInformation>() {
		      @Override
		      public void serialize(GeneralInformation value, JsonGenerator gen, SerializerProvider serializers)
		          throws IOException, JsonProcessingException {

		        gen.writeStartObject();
		        ObjectMapper mapper = EMFModule.setupDefaultMapper();
		      
		        String jsonStr = mapper.writeValueAsString(value);
		        System.out.println(" GeneralInformation serializer  "+jsonStr);
		        gen.writeStringField("generalInformation", jsonStr);
		        //from java to javascript part
		        gen.writeEndObject();
		      }
		    });

	    addDeserializer(GeneralInformation.class, new JsonDeserializer<GeneralInformation>() {
	      @Override
	      public GeneralInformation deserialize(JsonParser p, DeserializationContext ctxt)
	          throws IOException, JsonProcessingException {
	    	  //from javascript to javaObject
	    	  final JsonNode node = p.readValueAsTree();
		        final String gIString = node.get("generalInformation").textValue();
		        System.out.println(" GeneralInformation deserialize  "+gIString);
		        final ResourceSet resourceSet = new ResourceSetImpl();
		        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
		          .put(Resource.Factory.Registry.DEFAULT_CONTENT_TYPE_IDENTIFIER, new JsonResourceFactory());
			      resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
			          MetadataPackage.eINSTANCE);
			      Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
			      InputStream stream = new ByteArrayInputStream(gIString.getBytes(StandardCharsets.UTF_8));
			      resource.load(stream, null);
			      GeneralInformation gi = (GeneralInformation) resource.getContents().get(0);
			      return gi;
	      }
	    });
	    addSerializer(Scope.class, new JsonSerializer<Scope>() {
		      @Override
		      public void serialize(Scope value, JsonGenerator gen, SerializerProvider serializers)
		          throws IOException, JsonProcessingException {
		    	
		        gen.writeStartObject();
		        ObjectMapper mapper = EMFModule.setupDefaultMapper();
		        
		        String jsonStr = mapper.writeValueAsString(value);
		        System.out.println(" Scope serializer  "+jsonStr);
		        gen.writeStringField("scope", jsonStr);
		        //from java to javascript part
		        gen.writeEndObject();
		      }
		    });

	    addDeserializer(Scope.class, new JsonDeserializer<Scope>() {
	      @Override
	      public Scope deserialize(JsonParser p, DeserializationContext ctxt)
	          throws IOException, JsonProcessingException {
	    	  //from javascript to javaObject
	    	  final JsonNode node = p.readValueAsTree();
		        final String gIString = node.get("scope").textValue();
		        System.out.println(" Scope deserialize  "+gIString);
		        
		        final ResourceSet resourceSet = new ResourceSetImpl();
		        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
		          .put(Resource.Factory.Registry.DEFAULT_CONTENT_TYPE_IDENTIFIER, new JsonResourceFactory());
			    resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
			          MetadataPackage.eINSTANCE);
			    
			    Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
			      InputStream stream = new ByteArrayInputStream(gIString.getBytes(StandardCharsets.UTF_8));
			      resource.load(stream, null);
			      stream.close();
			      
			      Scope gi = (Scope) resource.getContents().get(0);
			      return gi;
	      }
	    });	    
	    addSerializer(DataBackground.class, new JsonSerializer<DataBackground>() {
		      @Override
		      public void serialize(DataBackground value, JsonGenerator gen, SerializerProvider serializers)
		          throws IOException, JsonProcessingException {

		        gen.writeStartObject();
		        ObjectMapper mapper = EMFModule.setupDefaultMapper();
		        String jsonStr = mapper.writeValueAsString(value);
		        
		        gen.writeStringField("dataBackground", jsonStr);
		        
		        gen.writeEndObject();
		      }
		    });

	    addDeserializer(DataBackground.class, new JsonDeserializer<DataBackground>() {
	      @Override
	      public DataBackground deserialize(JsonParser p, DeserializationContext ctxt)
	          throws IOException, JsonProcessingException {
	    	  final JsonNode node = p.readValueAsTree();
		        final String gIString = node.get("dataBackground").textValue();
		        
		        final ResourceSet resourceSet = new ResourceSetImpl();
		        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
		          .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JsonResourceFactory());
			      resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
			          MetadataPackage.eINSTANCE);
			      Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
			      InputStream stream = new ByteArrayInputStream(gIString.getBytes(StandardCharsets.UTF_8));
			      resource.load(stream, null);
			      DataBackground gi = (DataBackground) resource.getContents().get(0);
			      return gi;
	      }
	    });
	    addSerializer(ModelMath.class, new JsonSerializer<ModelMath>() {
		      @Override
		      public void serialize(ModelMath value, JsonGenerator gen, SerializerProvider serializers)
		          throws IOException, JsonProcessingException {

		        gen.writeStartObject();
		        ObjectMapper mapper = EMFModule.setupDefaultMapper();
		        String jsonStr = mapper.writeValueAsString(value);
		        
		        gen.writeStringField("modelMath", jsonStr);
		        
		        gen.writeEndObject();
		      }
		    });

	    addDeserializer(ModelMath.class, new JsonDeserializer<ModelMath>() {
	      @Override
	      public ModelMath deserialize(JsonParser p, DeserializationContext ctxt)
	          throws IOException, JsonProcessingException {
	    	  final JsonNode node = p.readValueAsTree();
		        final String gIString = node.get("modelMath").textValue();
		        
		        final ResourceSet resourceSet = new ResourceSetImpl();
		        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
		          .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JsonResourceFactory());
			      resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
			          MetadataPackage.eINSTANCE);
			      Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
			      InputStream stream = new ByteArrayInputStream(gIString.getBytes(StandardCharsets.UTF_8));
			      resource.load(stream, null);
			      ModelMath gi = (ModelMath) resource.getContents().get(0);
			      return gi;
	      }
	    });
	    
	   
	}
}
