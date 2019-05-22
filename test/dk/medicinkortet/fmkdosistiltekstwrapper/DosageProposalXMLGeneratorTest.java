package dk.medicinkortet.fmkdosistiltekstwrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.FMKVersion;
import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;

public class DosageProposalXMLGeneratorTest extends DosisTilTekstWrapperTestBase { 
	
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void testBasic() throws ParseException {
		DosageProposalResult res = DosisTilTekstWrapper.getDosageProposalResult("PN", "1", "1", "tablet", "tabletter", "tages med rigeligt vand", Arrays.asList(SIMPLE_DATE_FORMAT.parse("2017-05-17")), Arrays.asList(SIMPLE_DATE_FORMAT.parse("2017-06-01")), FMKVersion.FMK146, 1);
		assertNotNull(res);
		assertNotNull(res.getLongText());
		assertEquals("Doseringsforløbet starter onsdag den 17. maj 2017, gentages hver dag, og ophører torsdag den 1. juni 2017:\n" +
				"   Doseringsforløb:\n" + 
				"   1 tablet efter behov højst 1 gang daglig.\n   Bemærk: tages med rigeligt vand", res.getLongText());
		assertNull(res.getShortText());
		assertEquals("<m16:Dosage xsi:schemaLocation=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01 ../../../2015/06/01/DosageForRequest.xsd\" xmlns:m16=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><m16:UnitTexts source=\"Doseringsforslag\"><m16:Singular>tablet</m16:Singular><m16:Plural>tabletter</m16:Plural></m16:UnitTexts><m16:StructuresAccordingToNeed><m16:Structure><m16:IterationInterval>1</m16:IterationInterval><m16:StartDate>2017-05-17</m16:StartDate><m16:EndDate>2017-06-01</m16:EndDate><m16:SupplementaryText>tages med rigeligt vand</m16:SupplementaryText><m16:Day><m16:Number>1</m16:Number><m16:Dose><m16:Quantity>1</m16:Quantity></m16:Dose></m16:Day></m16:Structure></m16:StructuresAccordingToNeed></m16:Dosage>", res.getXmlSnippet());
	}
	
	@Test
	public void testBasicWithoutEnddate() throws ParseException {
		ArrayList<Date> endDates = new ArrayList<Date>();
		endDates.add(null);
		
		DosageProposalResult res = DosisTilTekstWrapper.getDosageProposalResult("PN", "1", "1", "tablet", "tabletter", "tages med rigeligt vand", Arrays.asList(SIMPLE_DATE_FORMAT.parse("2017-05-17")), endDates, FMKVersion.FMK146, 1);
		assertNotNull(res);
		assertNotNull(res.getLongText());
		assertEquals("Doseringsforløbet starter onsdag den 17. maj 2017 og gentages hver dag:\n" +
				"   Doseringsforløb:\n" + 
				"   1 tablet efter behov højst 1 gang daglig.\n   Bemærk: tages med rigeligt vand", res.getLongText());
		assertNull(res.getShortText());
		assertEquals("<m16:Dosage xsi:schemaLocation=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01 ../../../2015/06/01/DosageForRequest.xsd\" xmlns:m16=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><m16:UnitTexts source=\"Doseringsforslag\"><m16:Singular>tablet</m16:Singular><m16:Plural>tabletter</m16:Plural></m16:UnitTexts><m16:StructuresAccordingToNeed><m16:Structure><m16:IterationInterval>1</m16:IterationInterval><m16:StartDate>2017-05-17</m16:StartDate><m16:DosageEndingUndetermined/><m16:SupplementaryText>tages med rigeligt vand</m16:SupplementaryText><m16:Day><m16:Number>1</m16:Number><m16:Dose><m16:Quantity>1</m16:Quantity></m16:Dose></m16:Day></m16:Structure></m16:StructuresAccordingToNeed></m16:Dosage>", res.getXmlSnippet());
	}
	

	@Test
	public void testBasicWithLongerShortText() throws ParseException {
		DosageProposalResult res = DosisTilTekstWrapper.getDosageProposalResult("PN", "1", "1", "tablet", "tabletter", "tages med rigeligt vand OG SÅ EN LAANG SUPPLERENDE TEKST SÅ DEN KORTE TEKST BLIVER LÆNGERE END 70 KARAKTERER", Arrays.asList(SIMPLE_DATE_FORMAT.parse("2017-05-17")), Arrays.asList(SIMPLE_DATE_FORMAT.parse("2017-06-01")), FMKVersion.FMK146, 1, 10000);
		assertNotNull(res);
		assertNotNull(res.getLongText());
		assertEquals("Doseringsforløbet starter onsdag den 17. maj 2017, gentages hver dag, og ophører torsdag den 1. juni 2017:\n" +
				"   Doseringsforløb:\n" + 
				"   1 tablet efter behov højst 1 gang daglig.\n   Bemærk: tages med rigeligt vand OG SÅ EN LAANG SUPPLERENDE TEKST SÅ DEN KORTE TEKST BLIVER LÆNGERE END 70 KARAKTERER", res.getLongText());
		assertEquals("1 tablet efter behov, højst 1 gang daglig.\n   Bemærk: tages med rigeligt vand OG SÅ EN LAANG SUPPLERENDE TEKST SÅ DEN KORTE TEKST BLIVER LÆNGERE END 70 KARAKTERER", res.getShortText());
		assertEquals("<m16:Dosage xsi:schemaLocation=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01 ../../../2015/06/01/DosageForRequest.xsd\" xmlns:m16=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><m16:UnitTexts source=\"Doseringsforslag\"><m16:Singular>tablet</m16:Singular><m16:Plural>tabletter</m16:Plural></m16:UnitTexts><m16:StructuresAccordingToNeed><m16:Structure><m16:IterationInterval>1</m16:IterationInterval><m16:StartDate>2017-05-17</m16:StartDate><m16:EndDate>2017-06-01</m16:EndDate><m16:SupplementaryText>tages med rigeligt vand OG SÅ EN LAANG SUPPLERENDE TEKST SÅ DEN KORTE TEKST BLIVER LÆNGERE END 70 KARAKTERER</m16:SupplementaryText><m16:Day><m16:Number>1</m16:Number><m16:Dose><m16:Quantity>1</m16:Quantity></m16:Dose></m16:Day></m16:Structure></m16:StructuresAccordingToNeed></m16:Dosage>", res.getXmlSnippet());
	}
	
	@Test
	public void testMultiPeriode() throws ParseException {
		
		DosageProposalResult res = DosisTilTekstWrapper.getDosageProposalResult("{M+M+A+N}{PN}{N daglig}", "{1}{2}{1}",
				"{1+2+3+4}{dag 1: 2 dag 2: 3}{2}", "tablet", "tabletter", "tages med rigeligt vand",
				Arrays.asList(SIMPLE_DATE_FORMAT.parse("2010-01-01"), SIMPLE_DATE_FORMAT.parse("2010-02-01"), SIMPLE_DATE_FORMAT.parse("2010-03-01")),
				Arrays.asList(SIMPLE_DATE_FORMAT.parse("2010-01-31"), SIMPLE_DATE_FORMAT.parse("2010-02-28"), SIMPLE_DATE_FORMAT.parse("2010-03-31")),
				FMKVersion.FMK146, 1);
		assertNotNull(res);
		assertNotNull(res.getLongText());
		assertEquals("<m16:Dosage xsi:schemaLocation=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01 ../../../2015/06/01/DosageForRequest.xsd\" xmlns:m16=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
	            "<m16:UnitTexts source=\"Doseringsforslag\">" +
	            "<m16:Singular>tablet</m16:Singular>" +
	            "<m16:Plural>tabletter</m16:Plural>" +
	            "</m16:UnitTexts>" +
	            "<m16:StructuresFixed>" +
	            "<m16:Structure>" +
	            "<m16:IterationInterval>1</m16:IterationInterval>" +
	            "<m16:StartDate>2010-01-01</m16:StartDate>" +
	            "<m16:EndDate>2010-01-31</m16:EndDate>" +
	            "<m16:SupplementaryText>tages med rigeligt vand</m16:SupplementaryText>" +
	            "<m16:Day>" +
	            "<m16:Number>1</m16:Number>" +
	            "<m16:Dose>" +
	            "<m16:Time>morning</m16:Time>" +
	            "<m16:Quantity>1</m16:Quantity>" +
	            "</m16:Dose>" +
	            "<m16:Dose>" +
	            "<m16:Time>noon</m16:Time>" +
	            "<m16:Quantity>2</m16:Quantity>" +
	            "</m16:Dose>" +
	            "<m16:Dose>" +
	            "<m16:Time>evening</m16:Time>" +
	            "<m16:Quantity>3</m16:Quantity>" +
	            "</m16:Dose>" +
	            "<m16:Dose>" +
	            "<m16:Time>night</m16:Time>" +
	            "<m16:Quantity>4</m16:Quantity>" +
	            "</m16:Dose>" +
	            "</m16:Day>" +
	            "</m16:Structure>" +
	            "<m16:Structure>" +
	            "<m16:IterationInterval>1</m16:IterationInterval>" +
	            "<m16:StartDate>2010-03-01</m16:StartDate>" +
	            "<m16:EndDate>2010-03-31</m16:EndDate>" +
	            "<m16:SupplementaryText>tages med rigeligt vand</m16:SupplementaryText>" +
	            "<m16:Day>" +
	            "<m16:Number>1</m16:Number>" +
	            "<m16:Dose><m16:Quantity>2</m16:Quantity></m16:Dose>" +
	            "</m16:Day>" +
	            "</m16:Structure>" +
	            "</m16:StructuresFixed>" +
	            "<m16:StructuresAccordingToNeed>" +
	            "<m16:Structure>" +
	            "<m16:IterationInterval>2</m16:IterationInterval>" +
	            "<m16:StartDate>2010-02-01</m16:StartDate>" +
	            "<m16:EndDate>2010-02-28</m16:EndDate>" +
	            "<m16:SupplementaryText>tages med rigeligt vand</m16:SupplementaryText>" +
	            "<m16:Day>" +
	            "<m16:Number>1</m16:Number>" +
	            "<m16:Dose><m16:Quantity>2</m16:Quantity></m16:Dose>" +
	            "</m16:Day>" +
	            "<m16:Day>" +
	            "<m16:Number>2</m16:Number>" +
	            "<m16:Dose><m16:Quantity>3</m16:Quantity></m16:Dose>" +
	            "</m16:Day>" +
	            "</m16:Structure>" +
	            "</m16:StructuresAccordingToNeed>" +
	            "</m16:Dosage>", res.getXmlSnippet());
		
			assertEquals("Doseringen indeholder flere perioder:\n\n" +
            "Doseringsforløbet starter fredag den 1. januar 2010, gentages hver dag, og ophører søndag den 31. januar 2010:\n" +
            "   Doseringsforløb:\n" +
            "   1 tablet morgen + 2 tabletter middag + 3 tabletter aften + 4 tabletter nat.\n   Bemærk: tages med rigeligt vand\n\n" +

            "Doseringsforløbet starter mandag den 1. februar 2010, forløbet gentages hver 2. dag, og ophører søndag den 28. februar 2010.\n" +
            "Bemærk at doseringen varierer:\n" +
            "   Doseringsforløb:\n" +
            "   Dag 1: 2 tabletter efter behov højst 1 gang\n" +
            "   Dag 2: 3 tabletter efter behov højst 1 gang.\n   Bemærk: tages med rigeligt vand\n\n" +
            "Doseringsforløbet starter mandag den 1. marts 2010, gentages hver dag, og ophører onsdag den 31. marts 2010:\n" +
            "   Doseringsforløb:\n" +
            "   2 tabletter 1 gang daglig.\n   Bemærk: tages med rigeligt vand", res.getLongText());
	}
}
