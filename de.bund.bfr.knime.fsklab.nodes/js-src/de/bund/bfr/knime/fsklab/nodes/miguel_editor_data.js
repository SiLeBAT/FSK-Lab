const ui = {
    "generalInformation": [{
        "id": "name",
        "label": "Name",
        "type": "text",
        "description": "A name given to the model or data"
    }, {
        "id": "source",
        "label": "Source",
        "type": "text",
        "description": "A source from which the model/data is derived"
    }, {
        "id": "identifier",
        "label": "Identifier",
        "type": "text",
        "description": "An unambiguous ID given to the model or data. This can also be created automatically by a software tool"
    }, {
        "id": "creationDate",
        "label": "Creation date",
        "type": "date",
        "description": "Creation date/time of the FSK file"
    }, {
        "id": "rights",
        "label": "Rights",
        "type": "text",
        "description": "Rights granted for usage, distribution and modification of this FSK file"
    }, {
        "id": "availability",
        "label": "Availability",
        "type": "boolean",
        "description": "Availability of data or model, i.e. if the annotated model code / data is included in this FSK file"
    }, {
        "id": "url",
        "label": "URL",
        "type": "url",
        "description": "Web address referencing the resource location (data for example)"
    }, {
        "id": "format",
        "label": "Format",
        "type": "text",
        "description": "File extension of the model or data file (including version number of format if applicable)"
    }, {
        "id": "language",
        "label": "Language",
        "type": "text",
        "description": "A language of the resource (some data or reports can be available in French language for example)"
    }, {
        "id": "software",
        "label": "Software",
        "type": "text",
        "description": "The program or software language in which the model has been implemented"
    }, {
        "id": "languageWrittenIn",
        "label": "Language written in",
        "type": "text",
        "description": "Software language used to write the model, e.g. R or MatLab"
    }, {
        "id": "status",
        "label": "Status",
        "type": "text",
        "description": "The curation status of the model"
    }, {
        "id": "objective",
        "label": "Objective",
        "type": "text",
        "description": "Objective of the model or data"
    }, {
        "id": "description",
        "label": "Description",
        "type": "text",
        "description": "General description of the study, data or model"
    }],
    "modelCategory": [{
        "id": "modelClass",
        "label": "Model class",
        "type": "text",
        "description": "Type of model according to RAKIP classification"
    }, {
        "id": "modelClassComment",
        "label": "Comment",
        "type": "text",
        "description": ""
    }, {
        "id": "modelSubClass",
        "label": "Subclass",
        "type": "text-array",
        "description": "Sub-cassification of the model given the Model Class"
    }, {
        "id": "basicProcess",
        "label": "Basic process",
        "type": "text-array",
        "description": "Defines the impact of the process on the hazard (so far only valid for process models and predictive models)"
    }],
    "contact": [{
        "id": "title",
        "label": "Title",
        "type": "text",
        "description": "Specifies the job title, functional position or function of the individual associated."
    }, {
        "id": "familyName",
        "label": "Family name",
        "type": "text",
        "description": "Family name or surname."
    }, {
        "id": "givenName",
        "label": "Given name",
        "type": "text",
        "description": "Given name"
    }, {
        "id": "email",
        "label": "Email",
        "type": "text",
        "description": "The address for eletronic mail communication."
    }, {
        "id": "telephone",
        "label": "Telephone",
        "type": "text",
        "description": "The canonical number string for a telephone number for telephony communication."
    }, {
        "id": "streetAddress",
        "label": "Address",
        "type": "text",
        "description": "Physical delivery address"
    }, {
        "id": "country",
        "label": "Country",
        "type": "text",
        "description": "Full name of the country in English"
    }, {
        "id": "zipCode",
        "label": "Zip code",
        "type": "text",
        "description": "Postal code"
    }, {
        "id": "region",
        "label": "Region",
        "type": "text",
        "description": "State or province"
    }, {
        "id": "timezone",
        "label": "Timezone",
        "type": "text",
        "description": "Time zone"
    }, {
        "id": "gender",
        "label": "Gender",
        "type": "text",
        "description": "Sex and gender identity. M (male), F (female), O (other), N (none or not applicable) and U (unknown)"
    }, {
        "id": "note",
        "label": "Note",
        "type": "text",
        "description": "Supplemental information or a comment"
    }, {
        "id": "organization",
        "label": "Organization",
        "type": "text",
        "description": "Organization information"
    }],
    "reference": [
        {
            "id": "isReferenceDescription",
            "label": "Is reference",
            "type": "boolean",
            "description": "Indicates whether this specific publication serves as the reference description for the model. There has to be at least one reference where this field is set to 'True'"
        }, {
            "id": "publicationType",
            "label": "Type",
            "type": "text",
            "description": "The type of publication, e.g. Report, Journal article, Book, Online database, ..."
        }, {
            "id": "date",
            "label": "Date",
            "type": "date",
            "description": "Temporal information on the publication date"
        }, {
            "id": "pmid",
            "label": "PubMed",
            "type": "text",
            "description": "The PubMed ID related to this publication"
        }, {
            "id": "doi",
            "label": "DOI",
            "type": "text",
            "description": "DOI related to this publication"
        }, {
            "id": "authorList",
            "label": "Author list",
            "type": "text",
            "description": "Name and surname of the authors who contributed to this publication"
        }, {
            "id": "title",
            "label": "Title",
            "type": "text",
            "description": "Title of the publication in which the model or the data has been described",
        }, {
            "id": "abstract",
            "label": "Abstract",
            "type": "text",
            "description": "Abstract of the publication in which the model or the data has been described"
        }, {
            "id": "journal",
            "label": "Journal",
            "type": "text",
            "description": "Data on the details of the journal in which the model or the data has been described"
        }, {
            "id": "volume",
            "label": "Volume",
            "type": "text",
            "description": "Data on the details of the volume in which the model or the data has been described"
        }, {
            "id": "issue",
            "label": "Issue",
            "type": "text",
            "description": "Data on the details of the issue in which the model or the data has been described"
        }, {
            "id": "status",
            "label": "Status",
            "type": "text",
            "description": "The status of this publication, e.g. Published, Submitted, etc."
        }, {
            "id": "website",
            "label": "Website",
            "type": "text",
            "description": "A link to the publication website (different from DOI)"
        }, {
            "id": "comment",
            "label": "Comment",
            "type": "text",
            "description": "Further comments related to the reference description, e.g. which section in there describes the specific model or which figure in there can be reproduced with the visualization script"
        }
    ],
    "product": [
        {
            "id": "name",
            "label": "Name",
            "type": "text",
            "description": "The product, matrix or environment (e.g food product, lab media, soil etc.) for which the model or data applies"
        }, {
            "id": "description",
            "label": "Description",
            "type": "text",
            "description": "Detailed description of the product, matrix or environment for which the model or data applies"
        }, {
            "id": "unit",
            "label": "Unit",
            "type": "text",
            "description": "Unit of the product, matrix or environment for which the model or data applies"
        }, {
            "id": "method",
            "label": "Method",
            "type": "text-array",
            "description": "Type of production for the product/ matrix"
        }, {
            "id": "packaging",
            "label": "Packaging",
            "type": "text-array",
            "description": "Describe container or wrapper that holds the product/matrix. Common type of packaging (paper or plastic bags, boxes, tinplate or aluminium cans, plastic trays, plastic bottles, glass bottles or jars)"
        }, {
            "id": "treatment",
            "label": "Treatment",
            "type": "text-array",
            "description": "Used to characterise a product/matrix based on the treatment or processes applied to the product or any indexed ingredient"
        }, {
            "id": "originCountry",
            "label": "Origin country",
            "type": "text",
            "description": "Country of origin of the food/product (ISO 3166-1-alpha-2 country code)"
        }, {
            "id": "originArea",
            "label": "Origin area",
            "type": "text",
            "description": "Area of origin of the food/product (Nomenclature of territorial units for statistics – NUTS – coding system valid only for EEA and Switzerland)"
        }, {
            "id": "fisheriesArea",
            "label": "Fisheries area",
            "type": "text",
            "description": "Fisheries or aquaculture area specifying the origin of the sample (FAO Fisheries areas)"
        }, {
            "id": "productionDate",
            "label": "Production date",
            "type": "date",
            "description": "Date of production of food/product"
        }, {
            "id": "expiryDate",
            "label": "Expiration date",
            "type": "text",
            "description": "Date of expiry of food/product"
        }
    ],
    "hazard": [
        {
            "id": "type",
            "label": "Type",
            "type": "text",
            "description": "General classification of the hazard for which the model or data applies"
        }, {
            "id": "name",
            "label": "Name",
            "type": "text",
            "description": "Name of the hazard (agent, contaminant, chemical) for which the data or model applies"
        }, {
            "id": "description",
            "label": "Description",
            "type": "text",
            "description": "Description of the hazard for which the model or data applies"
        }, {
            "id": "unit",
            "label": "Unit",
            "type": "text",
            "description": "Unit of the hazard for which the model or data applies"
        }, {
            "id": "adverseEffect",
            "label": "Adverse effect",
            "type": "text",
            "description": "Adverse effect induced by hazard about morbidity, mortality, and etcetera"
        }, {
            "id": "sourceOfContamination",
            "label": "Source of contamination",
            "type": "text",
            "description": "Origin of the contamination, source"
        }, {
            "id": "benchmarkDose",
            "label": "Benchmark dose",
            "type": "text",
            "description": "A dose or concentration that produces a predetermined change in response rate of an adverse effect (called the benchmark response or BMR) compared to background"
        }, {
            "id": "maximumResidueLimit",
            "label": "Maximum residue limit",
            "type": "text",
            "description": "International regulations and permissible maximum residue levels in food and drinking water"
        }, {
            "id": "noObservedAdverseAffectLevel",
            "label": "NOAEL",
            "type": "text",
            "description": "Level of exposure of an organism, found by experiment or observation, at which there is no biologically or statistically significant increase in the frequency or severity of any adverse effects in the exposed population when compared to its appropriate control"
        }, {
            "id": "lowestObservedAdverseAffectLevel",
            "label": "LOAEL",
            "type": "text",
            "description": "Lowest concentration or amount of a substance found by experiment or observation that causes an adverse alteration of morphology, function, capacity, growth, development, or lifespan of a target organism distinguished from normal organisms of the same species under defined conditions of exposure"
        }, {
            "id": "acceptableOperatorExposureLevel",
            "label": "AOEL",
            "type": "text",
            "description": "Maximum amount of active substance to which the operator may be exposed without any adverse health effects. The AOEL is expressed as milligrams of the chemical per kilogram body weight of the operator"
        }, {
            "id": "acuteReferenceDose",
            "label": "ARD",
            "type": "text",
            "description": "An estimate (with uncertainty spanning perhaps an order of magnitude) of a daily oral exposure for an acute duration (24 hours or less) to the human population (including sensitive subgroups) that is likely to be without an appreciable risk of deleterious effects during a lifetime"
        }, {
            "id": "acceptableDailyIntake",
            "label": "ADI",
            "type": "text",
            "description": "Acceptable daily intake: measure of amount of a specific substance in food or in drinking water tahta can be ingested (orally) on a daily basis over a lifetime without an appreciable health risk"
        }, {
            "id": "indSum",
            "label": "Ind sum",
            "type": "text",
            "description": "Define if the parameter reported is an individual residue/analyte, a summed residue definition or part of a sum a summed residue definition"
        }
    ],
    "populationGroup": [{
        "id": "name",
        "label": "Name",
        "type": "text",
        "description": "Name of the population for which the model or data applies"
    }, {
        "id": "targetPopulation",
        "label": "Target population",
        "type": "text",
        "description": "Population of individual that we are interested in describing and making statistical inferences about"
    }, {
        "id": "populationSpan",
        "label": "Span",
        "type": "text-array",
        "description": "Temporal information on the exposure duration"
    }, {
        "id": "populationDescription",
        "label": "Description",
        "type": "text-array",
        "description": "Description of the population for which the model applies (demographic and socio-economic characteristics for example). Background information that are needed in the data analysis phase, size of household, education level, employment status, professional category, ethnicity, etc."
    }, {
        "id": "populationAge",
        "label": "Age",
        "type": "text-array",
        "description": "Description of the range of age or group of age"
    }, {
        "id": "populationGender",
        "label": "Gender",
        "type": "text",
        "description": "Description of the percentage of gender"
    }, {
        "id": "specialDietGroups",
        "label": "Special diet groups",
        "type": "text-array",
        "description": "Description of sub-population with special diets (vegetarians, diabetics, group following special ethnic diets)"
    }, {
        "id": "patternConsumption",
        "label": "Pattern consumption",
        "type": "text-array",
        "description": "Description of the consumption of different food items, frequency, portion size"
    }, {
        "id": "region",
        "label": "Region",
        "type": "text-array",
        "description": "Spatial information (area) on which the population group of the model or data applies"
    }, {
        "id": "country",
        "label": "Country",
        "type": "text-array",
        "description": "Country on which the population group of the model or data applies"
    }, {
        "id": "populationRiskFactor",
        "label": "Risk factor",
        "type": "text-array",
        "description": "Population risk factor that may influence the outcomes of the study, confounder should be included"
    }, {
        "id": "season",
        "label": "Season",
        "type": "text-array",
        "description": "Distribution of surveyed people according to the season (influence consumption pattern)"
    }
    ]
}
