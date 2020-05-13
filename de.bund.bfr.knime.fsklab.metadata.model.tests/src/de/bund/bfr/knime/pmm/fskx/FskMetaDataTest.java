package de.bund.bfr.knime.pmm.fskx;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import org.junit.Test;

import de.bund.bfr.pmfml.ModelClass;

@SuppressWarnings("static-method")
public class FskMetaDataTest {

	@Test
	public void testConstructor() {

		final FskMetaData metaData = new FskMetaData();

		assertThat(metaData.modelName, is(nullValue()));		
		assertThat(metaData.modelId, is(nullValue()));
		assertThat(metaData.modelLink, is(nullValue()));
		assertThat(metaData.organism, is(nullValue()));
		assertThat(metaData.organismDetails, is(nullValue()));
		assertThat(metaData.matrix, is(nullValue()));
		assertThat(metaData.matrixDetails, is(nullValue()));
		assertThat(metaData.creator, is(nullValue()));
		assertThat(metaData.familyName, is(nullValue()));
		assertThat(metaData.contact, is(nullValue()));
		assertThat(metaData.software, is(nullValue()));
		assertThat(metaData.referenceDescription, is(nullValue()));
		assertThat(metaData.referenceDescriptionLink, is(nullValue()));
		assertThat(metaData.createdDate, is(nullValue()));
		assertThat(metaData.modifiedDate, is(nullValue()));
		assertThat(metaData.rights, is(nullValue()));
		assertThat(metaData.notes, is(nullValue()));
		assertThat(metaData.curated, is(false));
		assertThat(metaData.type, is(nullValue()));
		assertThat(metaData.subject, equalTo(ModelClass.UNKNOWN));
		assertThat(metaData.foodProcess, is(nullValue()));
		// dependentVariable
		assertThat(metaData.independentVariables, empty());
		assertThat(metaData.hasData, is(false));
	}

}
